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

@WebServlet(name = "DeleteCartController", urlPatterns = {"/delete-cart"})
public class DeleteCartController extends HttpServlet {

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

        Integer productId = ValidationUtil.parsePositiveInt(request.getParameter("productId"));
        if (productId == null) {
            session.setAttribute("cartMessage", "Sản phẩm không hợp lệ.");
            response.sendRedirect("carts");
            return;
        }

        Map<Integer, Cart> carts = CartService.getCartMap(session);
        Cart cart = carts.remove(productId);
        if (cart != null) {
            new ProductDAO().releaseStock(productId, cart.getQuantity());
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
