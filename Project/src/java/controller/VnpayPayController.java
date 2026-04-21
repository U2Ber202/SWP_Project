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
        int amount = CheckOutController.calculateTotal(carts);
        
        String voucherCode = ValidationUtil.normalize(req.getParameter("voucherCode"));
        dal.VoucherDAO voucherDAO = new dal.VoucherDAO();
        
        if (voucherCode != null && !voucherCode.isEmpty()) {
            for (Map.Entry<Integer, Map<Integer, Cart>> entry : cartsByStore.entrySet()) {
                int sid = entry.getKey();
                Map<Integer, Cart> storeCarts = entry.getValue();
                model.Voucher voucher = voucherDAO.getVoucherByCodeAndStoreId(voucherCode, sid);
                
                if (voucher != null) {
                    int storePrice = CheckOutController.calculateTotal(storeCarts);
                    if (voucher.getMinOrderValue() == null || storePrice >= voucher.getMinOrderValue()) {
                        double rawDiscount = storePrice * (voucher.getDiscountPercent() / 100.0);
                        int discount = (int) Math.round(rawDiscount);
                        if (voucher.getMaxDiscount() != null && discount > voucher.getMaxDiscount()) {
                            discount = voucher.getMaxDiscount();
                        }
                        amount -= discount;
                    }
                }
            }
            // Lam tron so tien xuong hang chuc de dep so khi gui sang VNPay
            amount = (amount / 10) * 10;
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
