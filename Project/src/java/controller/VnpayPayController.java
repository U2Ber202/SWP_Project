package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.TimeZone;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Cart;
import model.Shipping;
import util.CartService;
import util.RoleHelper;
import util.ValidationUtil;
import vnpay.VnPayConfig;

@WebServlet(name = "VnpayPayController", urlPatterns = {"/vnpay_pay"})
public class VnpayPayController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        CartService.expireCartItems(session);
        Account account = (Account) session.getAttribute("acc");
        if (!RoleHelper.isCustomer(account)) {
            resp.sendRedirect("home");
            return;
        }

        Map<Integer, Cart> carts = CartService.getCartMap(session);
        if (carts.isEmpty()) {
            resp.sendRedirect("carts");
            return;
        }

        Map<Integer, Map<Integer, Cart>> cartsByStore = CheckOutController.splitCartByStore(carts);
        int originalTotal = CheckOutController.calculateTotal(carts);
        int totalDiscount = 0;
        
        String voucherCode = ValidationUtil.normalize(req.getParameter("voucherCode"));
        dal.VoucherDAO voucherDAO = new dal.VoucherDAO();
        
        for (Map.Entry<Integer, Map<Integer, Cart>> entry : cartsByStore.entrySet()) {
            int storeId = entry.getKey();
            Map<Integer, Cart> storeCarts = entry.getValue();
            int storeTotalPrice = CheckOutController.calculateTotal(storeCarts);
            int storeDiscount = 0;

            // 1. Calculate Auto-gift discount
            int autoDiscountPercent = 0;
            if (storeTotalPrice >= 20000000) autoDiscountPercent = 30;
            else if (storeTotalPrice >= 10000000) autoDiscountPercent = 20;
            int autoDiscountValue = (int) Math.round(storeTotalPrice * (autoDiscountPercent / 100.0));

            // 2. Calculate Best Voucher discount
            model.Voucher bestVoucher = null;
            int voucherDiscountValue = 0;
            if (voucherCode != null && !voucherCode.isEmpty()) {
                bestVoucher = voucherDAO.getVoucherByCodeAndStoreId(voucherCode, storeId);
            } else {
                // Since findBestVoucher is private in CheckOutController, we can either make it public or re-implement
                // To avoid breaking CheckOutController, let's just do a quick auto-selection here or just use voucherDAO
                java.util.List<model.Voucher> available = voucherDAO.getVouchersByStoreId(storeId);
                int maxVDiscount = -1;
                java.time.LocalDate today = java.time.LocalDate.now();
                for (model.Voucher v : available) {
                    try {
                        java.time.LocalDate expiry = java.time.LocalDate.parse(v.getExpiryDate());
                        java.time.LocalDate start = v.getStartDate() != null ? java.time.LocalDate.parse(v.getStartDate()) : today;
                        if (today.isBefore(start) || today.isAfter(expiry)) continue;
                    } catch (Exception e) {}
                    if (v.getMinOrderValue() != null && storeTotalPrice < v.getMinOrderValue()) continue;
                    int d = (int) Math.round(storeTotalPrice * (v.getDiscountPercent() / 100.0));
                    if (v.getMaxDiscount() != null && d > v.getMaxDiscount()) d = v.getMaxDiscount();
                    if (d > maxVDiscount) {
                        maxVDiscount = d;
                        bestVoucher = v;
                    }
                }
                voucherDiscountValue = maxVDiscount > 0 ? maxVDiscount : 0;
            }

            if (bestVoucher != null && voucherDiscountValue == 0) { // If code was provided but maxVDiscount not calculated
                if (bestVoucher.getMinOrderValue() == null || storeTotalPrice >= bestVoucher.getMinOrderValue()) {
                    voucherDiscountValue = (int) Math.round(storeTotalPrice * (bestVoucher.getDiscountPercent() / 100.0));
                    if (bestVoucher.getMaxDiscount() != null && voucherDiscountValue > bestVoucher.getMaxDiscount()) {
                        voucherDiscountValue = bestVoucher.getMaxDiscount();
                    }
                }
            }

            // 3. Choose the better discount
            if (autoDiscountValue >= voucherDiscountValue && autoDiscountValue > 0) {
                storeDiscount = autoDiscountValue;
            } else if (voucherDiscountValue > 0) {
                storeDiscount = voucherDiscountValue;
            }
            totalDiscount += storeDiscount;
        }

        int totalVat = (int) Math.round(originalTotal * 0.10);
        int amount = originalTotal - totalDiscount + totalVat;
        
        // Round for VNPay
        amount = (amount / 10) * 10;
        if (voucherCode != null && !voucherCode.isEmpty()) {
            session.setAttribute("appliedVoucherCode", voucherCode);
        }

        String name = ValidationUtil.normalize(req.getParameter("name"));
        String phone = ValidationUtil.normalize(req.getParameter("phone"));
        String address = ValidationUtil.normalize(req.getParameter("address"));
        String note = ValidationUtil.normalize(req.getParameter("note"));

        req.setAttribute("name", name);
        req.setAttribute("phone", phone);
        req.setAttribute("address", address);
        req.setAttribute("note", note);

        if (ValidationUtil.isBlank(name) || ValidationUtil.isBlank(address)) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin người nhận và địa chỉ.");
            forwardCheckout(req, resp, carts);
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            req.setAttribute("error", "Số điện thoại phải dùng 10 chữ số và không được nhập chữ.");
            forwardCheckout(req, resp, carts);
            return;
        }

        Shipping pendingShipping = new Shipping(name, phone, address);
        String txnRef = VnPayConfig.getRandomNumber(12);
        session.setAttribute("pendingShipping", pendingShipping);
        session.setAttribute("pendingNote", note);
        session.setAttribute(CartService.PENDING_VNPAY_TXN_REF, txnRef);
        session.setAttribute(CartService.PENDING_VNPAY_AMOUNT, amount * 100L);
        session.setAttribute(CartService.PENDING_VNPAY_ACCOUNT_ID, account.getUid());

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100L));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang:" + txnRef);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnpParams.put("vnp_IpAddr", req.getRemoteAddr());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnpParams.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnpParams.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        String hashData = VnPayConfig.buildHashData(vnpParams);
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            if (query.length() > 0) {
                query.append('&');
            }
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
        }

        String vnpSecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData);
        query.append("&vnp_SecureHash=").append(vnpSecureHash);
        resp.sendRedirect(VnPayConfig.vnp_PayUrl + "?" + query);
    }

    private void forwardCheckout(HttpServletRequest req, HttpServletResponse resp, Map<Integer, Cart> carts)
            throws ServletException, IOException {
        req.setAttribute("carts", carts);
        req.setAttribute("totalMoney", CheckOutController.calculateTotal(carts));
        req.getRequestDispatcher("checkout.jsp").forward(req, resp);
    }
}
