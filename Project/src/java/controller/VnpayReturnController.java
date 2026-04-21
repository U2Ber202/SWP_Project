package controller;

import dal.OrderDAO;
import dal.OrderDetailDAO;
import dal.ShippingDAO;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Cart;
import model.Order;
import model.Shipping;
import util.CartService;
import vnpay.VnPayConfig;

@WebServlet(name = "VnpayReturnController", urlPatterns = {"/vnpay_return"})
public class VnpayReturnController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        Map<Integer, Cart> carts = CartService.getExistingCartMap(session);
        if (account == null || carts == null || carts.isEmpty()) {
            CartService.clearPendingVnpay(session);
            response.sendRedirect("checkout");
            return;
        }

        Map<String, String> fields = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String fieldName = parameterNames.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String receivedHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        String expectedHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, VnPayConfig.buildHashData(fields));
        if (receivedHash == null || !receivedHash.equalsIgnoreCase(expectedHash)) {
            session.setAttribute("cartMessage", "Không thể xác minh giao dịch VNPay. Giỏ hàng vẫn được giữ tạm trong 15 phút.");
            CartService.clearPendingVnpay(session);
            response.sendRedirect("checkout");
            return;
        }

        String txnRef = request.getParameter("vnp_TxnRef");
        String amountParam = request.getParameter("vnp_Amount");
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        long requestAmount;
        try {
            requestAmount = Long.parseLong(amountParam);
        } catch (NumberFormatException ex) {
            session.setAttribute("cartMessage", "Số tiền giao dịch VNPay không hợp lệ.");
            CartService.clearPendingVnpay(session);
            response.sendRedirect("checkout");
            return;
        }
        Long sessionAmount = (Long) session.getAttribute(CartService.PENDING_VNPAY_AMOUNT);
        Integer sessionAccountId = (Integer) session.getAttribute(CartService.PENDING_VNPAY_ACCOUNT_ID);
        String sessionTxnRef = (String) session.getAttribute(CartService.PENDING_VNPAY_TXN_REF);

        if (sessionAmount == null || sessionAccountId == null || sessionTxnRef == null
                || !sessionTxnRef.equals(txnRef)
                || sessionAccountId.intValue() != account.getUid()
                || sessionAmount.longValue() != requestAmount) {
            session.setAttribute("cartMessage", "Dữ liệu giao dịch không khớp với phiên đăng nhập hiện tại.");
            CartService.clearPendingVnpay(session);
            response.sendRedirect("checkout");
            return;
        }

        if (!"00".equals(responseCode) || !"00".equals(transactionStatus)) {
            session.setAttribute("cartMessage", "Thanh toán chưa thành công. Sản phẩm trong giỏ hàng vẫn được giữ tạm trong 15 phút.");
            CartService.clearPendingVnpay(session);
            response.sendRedirect("checkout");
            return;
        }

        Shipping shippingInfo = (Shipping) session.getAttribute("pendingShipping");
        String note = (String) session.getAttribute("pendingNote");
        if (shippingInfo == null) {
            session.setAttribute("cartMessage", "Thiếu thông tin giao hàng cho giao dịch VNPay.");
            CartService.clearPendingVnpay(session);
            response.sendRedirect("checkout");
            return;
        }

        Map<Integer, Map<Integer, Cart>> cartsByStore = CheckOutController.splitCartByStore(carts);
        ShippingDAO shippingDAO = new ShippingDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        int originalTotalPrice = CheckOutController.calculateTotal(carts);
        int finalTotalPrice = 0;
        int totalDiscount = 0;
        String voucherCode = (String) session.getAttribute("appliedVoucherCode");
        dal.VoucherDAO vDAO = new dal.VoucherDAO();
        for (Map.Entry<Integer, Map<Integer, Cart>> entry : cartsByStore.entrySet()) {
            int storeId = entry.getKey();
            Map<Integer, Cart> storeCarts = entry.getValue();
            int storePrice = CheckOutController.calculateTotal(storeCarts);
            String finalNote = appendVnpayNote(note, txnRef);

            // Dung method moi de hien thi dung voucher cho tung store/system
            model.Voucher voucher = (voucherCode != null) ? vDAO.getVoucherByCodeAndStoreId(voucherCode, storeId) : null;

            if (voucher != null) {
                if (voucher.getMinOrderValue() == null || storePrice >= voucher.getMinOrderValue()) {
                    int discount = (int) Math.round(storePrice * (voucher.getDiscountPercent() / 100.0));
                    if (voucher.getMaxDiscount() != null && discount > voucher.getMaxDiscount()) {
                        discount = voucher.getMaxDiscount();
                    }
                    storePrice -= discount;
                    totalDiscount += discount;
                    finalNote += " (Voucher: " + voucherCode + " -" + discount + "d)";
                }
            }

            Shipping shipping = new Shipping(shippingInfo.getName(), shippingInfo.getPhone(), shippingInfo.getAddress(), storeId);
            int shippingId = shippingDAO.createReturnId(shipping);
            Order order = new Order(account.getUid(), storePrice, finalNote, shippingId, storeId);
            int orderId = orderDAO.createReturnId(order);
            orderDetailDAO.saveCart(orderId, storeCarts);
            finalTotalPrice += storePrice;
        }
        session.removeAttribute("appliedVoucherCode");

        session.removeAttribute(CartService.CARTS_SESSION_KEY);
        CartService.clearPendingVnpay(session);
        request.setAttribute("cartss", carts);
        request.setAttribute("totalPrice", finalTotalPrice);
        request.setAttribute("originalTotalPrice", originalTotalPrice);
        request.setAttribute("totalDiscount", totalDiscount);
        request.getRequestDispatcher("thank").forward(request, response);
    }

    private String appendVnpayNote(String note, String txnRef) {
        String base = note == null ? "" : note.trim();
        if (!base.isEmpty()) {
            return base + " (VNPAY:" + txnRef + ")";
        }
        return "VNPAY:" + txnRef;
    }
}
