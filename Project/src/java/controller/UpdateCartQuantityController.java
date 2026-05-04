package controller;

import dal.ProductDAO;
import java.io.IOException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Cart;
import util.CartService;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "UpdateCartQuantityController", urlPatterns = {"/update-quantity"})
public class UpdateCartQuantityController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processUpdate(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processUpdate(request, response);
    }

    private void processUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        CartService.expireCartItems(session);
        Account acc = (Account) session.getAttribute("acc");
        if (!RoleHelper.isCustomer(acc)) {
            response.sendRedirect("home");
            return;
        }

        Integer variantId = ValidationUtil.parsePositiveInt(request.getParameter("variantId"));
        Integer newQuantity = ValidationUtil.parseNonNegativeInt(request.getParameter("quantity"));
        if (variantId == null || newQuantity == null) {
            session.setAttribute("cartMessage", "Số lượng cập nhật không hợp lệ.");
            response.sendRedirect("carts");
            return;
        }

        Map<Integer, Cart> carts = CartService.getCartMap(session);
        Cart cart = carts.get(variantId);
        if (cart == null) {
            response.sendRedirect("carts");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        int currentQuantity = cart.getQuantity();
        if (newQuantity <= 0) {
            productDAO.releaseStock(variantId, currentQuantity);
            carts.remove(variantId);
        } else if (newQuantity > currentQuantity) {
            int increase = newQuantity - currentQuantity;
            if (!productDAO.reserveStock(variantId, increase)) {
                session.setAttribute("cartMessage", "Kho không đủ để tăng thêm số lượng này.");
                response.sendRedirect("carts");
                return;
            }
            cart.setQuantity(newQuantity);
            cart.setVariant(productDAO.getVariantById(variantId));
            cart.refreshTimeout();
        } else if (newQuantity < currentQuantity) {
            int decrease = currentQuantity - newQuantity;
            productDAO.releaseStock(variantId, decrease);
            cart.setQuantity(newQuantity);
            cart.setVariant(productDAO.getVariantById(variantId));
            cart.refreshTimeout();
        } else {
            cart.refreshTimeout();
        }


        CartService.clearPendingVnpay(session);
        if (carts.isEmpty()) {
            session.removeAttribute("carts");
        } else {
            session.setAttribute("carts", carts);
        }
        response.sendRedirect("carts");
    }
}
