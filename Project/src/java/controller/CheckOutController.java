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
        int totalDiscount = 0;
        Map<Integer, model.Voucher> bestVouchers = new java.util.HashMap<>();
        for(Integer sid : byStore.keySet()) {
            int storeTotal = calculateTotal(byStore.get(sid));
            
            // 1. Calculate Auto-gift discount
            int autoDiscountPercent = 0;
            if (storeTotal >= 20000000) autoDiscountPercent = 30;
            else if (storeTotal >= 10000000) autoDiscountPercent = 20;
            int autoDiscountValue = (int) Math.round(storeTotal * (autoDiscountPercent / 100.0));

            // 2. Calculate Best Voucher discount
            model.Voucher bestV = findBestVoucher(vDAO.getVouchersByStoreId(sid), storeTotal);
            int voucherDiscountValue = 0;
            if (bestV != null) {
                voucherDiscountValue = (int) Math.round(storeTotal * (bestV.getDiscountPercent() / 100.0));
                if (bestV.getMaxDiscount() != null && voucherDiscountValue > bestV.getMaxDiscount()) {
                    voucherDiscountValue = bestV.getMaxDiscount();
                }
            }

            // 3. Pick the best
            if (autoDiscountValue >= voucherDiscountValue && autoDiscountValue > 0) {
                totalDiscount += autoDiscountValue;
            } else if (voucherDiscountValue > 0) {
                totalDiscount += voucherDiscountValue;
                bestVouchers.put(sid, bestV);
            }
        }
        
        int totalVat = (int) Math.round(totalMoney * 0.10);
        int finalTotal = totalMoney - totalDiscount + totalVat;

        request.setAttribute("defaultName", acc.getFullname());
        request.setAttribute("defaultPhone", acc.getPhone());
        request.setAttribute("defaultAddress", acc.getAddress());

        request.setAttribute("storeVouchers", storeVouchers);
        request.setAttribute("totalMoney", totalMoney);
        request.setAttribute("totalDiscount", totalDiscount);
        request.setAttribute("totalVat", totalVat);
        request.setAttribute("finalTotal", finalTotal);
        request.setAttribute("bestVouchers", bestVouchers);
        
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
                List<model.Voucher> available = voucherDAO.getVouchersByStoreId(storeId);
                bestVoucher = findBestVoucher(available, storeTotalPrice);
            }

            if (bestVoucher != null && (bestVoucher.getMinOrderValue() == null || storeTotalPrice >= bestVoucher.getMinOrderValue())) {
                voucherDiscountValue = (int) Math.round(storeTotalPrice * (bestVoucher.getDiscountPercent() / 100.0));
                if (bestVoucher.getMaxDiscount() != null && voucherDiscountValue > bestVoucher.getMaxDiscount()) {
                    voucherDiscountValue = bestVoucher.getMaxDiscount();
                }
            }

            // 3. Choose the better discount
            if (autoDiscountValue >= voucherDiscountValue && autoDiscountValue > 0) {
                storeDiscount = autoDiscountValue;
                note = (note == null ? "" : note) + " (Auto-gift: -" + autoDiscountPercent + "%)";
            } else if (voucherDiscountValue > 0) {
                storeDiscount = voucherDiscountValue;
                note = (note == null ? "" : note) + " (Voucher: " + bestVoucher.getCode() + " -" + voucherDiscountValue + "d)";
            }

            storeTotalPrice -= storeDiscount;
            
            // 3. Add VAT (10%)
            int vatAmount = (int) Math.round(storeTotalPrice * 0.10);
            storeTotalPrice += vatAmount;
            
            totalDiscount += storeDiscount;
            finalTotalPrice = finalTotalPrice - storeDiscount + vatAmount; // Note: this calculation needs to be careful if multiple stores

            Shipping shipping = new Shipping(name, phone, address, storeId);
            int shippingId = shippingDAO.createReturnId(shipping);
            Order order = new Order(account.getUid(), storeTotalPrice, note, shippingId, storeId);
            order.setVatPercent(10);
            int orderId = orderDAO.createReturnId(order);
            orderDetailDAO.saveCart(orderId, storeCarts);
        }
        
        // Recalculate final total properly
        int originalTotal = calculateTotal(carts);
        int totalVat = (int) Math.round(originalTotal * 0.10);
        finalTotalPrice = originalTotal - totalDiscount + totalVat;

        session.removeAttribute(CartService.CARTS_SESSION_KEY);
        CartService.clearPendingVnpay(session);
        request.setAttribute("cartss", carts);
        request.setAttribute("totalPrice", finalTotalPrice);
        request.setAttribute("originalTotalPrice", originalTotalPrice);
        request.setAttribute("totalDiscount", totalDiscount);
        request.setAttribute("totalVat", totalVat);
        request.getRequestDispatcher("thanks.jsp").forward(request, response);
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

    private model.Voucher findBestVoucher(List<model.Voucher> vouchers, int orderTotal) {
        model.Voucher best = null;
        int maxDiscount = -1;
        
        java.time.LocalDate today = java.time.LocalDate.now();
        
        for (model.Voucher v : vouchers) {
            // Check validity (already filtered in DAO for code search, but here we have a list)
            try {
                java.time.LocalDate expiry = java.time.LocalDate.parse(v.getExpiryDate());
                java.time.LocalDate start = v.getStartDate() != null ? java.time.LocalDate.parse(v.getStartDate()) : today;
                if (today.isBefore(start) || today.isAfter(expiry)) continue;
            } catch (Exception e) {}

            if (v.getMinOrderValue() != null && orderTotal < v.getMinOrderValue()) continue;

            int discount = (int) Math.round(orderTotal * (v.getDiscountPercent() / 100.0));
            if (v.getMaxDiscount() != null && discount > v.getMaxDiscount()) {
                discount = v.getMaxDiscount();
            }

            if (discount > maxDiscount) {
                maxDiscount = discount;
                best = v;
            }
        }
        return best;
    }

    static int calculateTotal(Map<Integer, Cart> carts) {
        int totalMoney = 0;
        for (Cart cart : carts.values()) {
            totalMoney += cart.getQuantity() * cart.getProduct().getPrice();
        }
        return totalMoney;
    }
}
