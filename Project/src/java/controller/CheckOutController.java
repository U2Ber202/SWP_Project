package controller;

import dal.OrderDAO;
import dal.OrderDetailDAO;
import dal.ShippingDAO;
import java.io.IOException;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Cart;
import model.Order;
import model.Shipping;
import util.CartService;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "CheckOutController", urlPatterns = {"/checkout"})
public class CheckOutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartService.expireCartItems(session);
        Account acc = (Account) session.getAttribute("acc");
        if (!RoleHelper.isCustomer(acc)) {
            response.sendRedirect("home");
            return;
        }

        Map<Integer, Cart> carts = CartService.getCartMap(session);
        int totalMoney = calculateTotal(carts);
        
        // Lay danh sach voucher kha dung cho cac store trong gio
        dal.VoucherDAO vDAO = new dal.VoucherDAO();
        Map<Integer, List<model.Voucher>> storeVouchers = new java.util.HashMap<>();
        Map<Integer, Map<Integer, Cart>> byStore = splitCartByStore(carts);
        for(Integer sid : byStore.keySet()) {
            storeVouchers.put(sid, vDAO.getVouchersByStoreId(sid));
        }
        
        request.setAttribute("storeVouchers", storeVouchers);
        request.setAttribute("totalMoney", totalMoney);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        CartService.expireCartItems(session);

        Account account = (Account) session.getAttribute("acc");
        if (!RoleHelper.isCustomer(account)) {
            response.sendRedirect("home");
            return;
        }

        String name = ValidationUtil.normalize(request.getParameter("name"));
        String phone = ValidationUtil.normalize(request.getParameter("phone"));
        String address = ValidationUtil.normalize(request.getParameter("address"));
        String note = ValidationUtil.normalize(request.getParameter("note"));

        Map<Integer, Cart> carts = CartService.getCartMap(session);
        if (carts.isEmpty()) {
            session.setAttribute("cartMessage", "Giỏ hàng dang trống.");
            response.sendRedirect("carts");
            return;
        }

        request.setAttribute("name", name);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
        request.setAttribute("note", note);

        if (ValidationUtil.isBlank(name) || ValidationUtil.isBlank(address)) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin người nhận và địa chỉ.");
            forwardCheckout(request, response, carts);
            return;
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("error", "Số điện thoại phải đúng 10 chữ số và không được nhập chữ.");
            forwardCheckout(request, response, carts);
            return;
        }

        Map<Integer, Map<Integer, Cart>> cartsByStore = splitCartByStore(carts);
        int originalTotalPrice = calculateTotal(carts);
        int finalTotalPrice = originalTotalPrice;
        int totalDiscount = 0;
        
        String voucherCode = ValidationUtil.normalize(request.getParameter("voucherCode"));
        dal.VoucherDAO voucherDAO = new dal.VoucherDAO();
        ShippingDAO shippingDAO = new ShippingDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

        for (Map.Entry<Integer, Map<Integer, Cart>> entry : cartsByStore.entrySet()) {
            int storeId = entry.getKey();
            Map<Integer, Cart> storeCarts = entry.getValue();
            int storeTotalPrice = calculateTotal(storeCarts);

            // Tim voucher hop le cho store nay (hoac voucher he thong)
            model.Voucher voucher = (voucherCode != null && !voucherCode.isEmpty()) ? voucherDAO.getVoucherByCodeAndStoreId(voucherCode, storeId) : null;

            if (voucher != null) {
                if (voucher.getMinOrderValue() == null || storeTotalPrice >= voucher.getMinOrderValue()) {
                    int discount = (int) Math.round(storeTotalPrice * (voucher.getDiscountPercent() / 100.0));
                    if (voucher.getMaxDiscount() != null && discount > voucher.getMaxDiscount()) {
                        discount = voucher.getMaxDiscount();
                    }
                    storeTotalPrice -= discount;
                    finalTotalPrice -= discount;
                    totalDiscount += discount;
                    note = (note == null ? "" : note) + " (Voucher: " + voucherCode + " -" + discount + "d)";
                }
            }

            Shipping shipping = new Shipping(name, phone, address, storeId);
            int shippingId = shippingDAO.createReturnId(shipping);
            Order order = new Order(account.getUid(), storeTotalPrice, note, shippingId, storeId);
            int orderId = orderDAO.createReturnId(order);
            orderDetailDAO.saveCart(orderId, storeCarts);
        }

        session.removeAttribute(CartService.CARTS_SESSION_KEY);
        CartService.clearPendingVnpay(session);
        request.setAttribute("cartss", carts);
        request.setAttribute("totalPrice", finalTotalPrice);
        request.setAttribute("originalTotalPrice", originalTotalPrice);
        request.setAttribute("totalDiscount", totalDiscount);
        request.getRequestDispatcher("thank").forward(request, response);
    }

    private void forwardCheckout(HttpServletRequest request, HttpServletResponse response, Map<Integer, Cart> carts)
            throws ServletException, IOException {
        request.setAttribute("totalMoney", calculateTotal(carts));
        request.setAttribute("carts", carts);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    static Map<Integer, Map<Integer, Cart>> splitCartByStore(Map<Integer, Cart> carts) {
        Map<Integer, Map<Integer, Cart>> cartsByStore = new LinkedHashMap<>();
        for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
            Cart cart = entry.getValue();
            int storeId = cart.getProduct().getStoreId();
            cartsByStore.computeIfAbsent(storeId, key -> new LinkedHashMap<>()).put(entry.getKey(), cart);
        }
        return cartsByStore;
    }

    static int calculateTotal(Map<Integer, Cart> carts) {
        int totalMoney = 0;
        for (Cart cart : carts.values()) {
            totalMoney += cart.getQuantity() * cart.getProduct().getPrice();
        }
        return totalMoney;
    }
}
