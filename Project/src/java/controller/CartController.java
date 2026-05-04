package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Cart;
import model.Product;
import util.CartService;
import util.RoleHelper;

@WebServlet(name = "CartController", urlPatterns = {"/carts"})
public class CartController extends BaseRequiredAuthenController {

    @Override
    protected void processRequests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartService.expireCartItems(session);
        Account acc = (Account) session.getAttribute("acc");
        if (!RoleHelper.isCustomer(acc)) {
            response.sendRedirect("home");
            return;
        }

        Map<Integer, Cart> carts = CartService.getCartMap(session);
        int totalMoney = 0;
        for (Cart cart : carts.values()) {
            totalMoney += cart.getQuantity() * cart.getVariant().getPrice();
        }
        
        ProductDAO pDao = new ProductDAO();
        List<Product> featuredProducts = pDao.getAllNewProducts();
        
        request.setAttribute("cartMessage", session.getAttribute(CartService.CART_MESSAGE_SESSION_KEY));
        session.removeAttribute(CartService.CART_MESSAGE_SESSION_KEY);
        request.setAttribute("totalMoney", totalMoney);
        request.setAttribute("carts", carts);
        request.setAttribute("featuredProducts", featuredProducts);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
}
