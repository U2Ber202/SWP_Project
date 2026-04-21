package controller;

import dal.ProductDAO;
import java.io.IOException;
import java.util.Map;
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
import util.ValidationUtil;

@WebServlet(name = "AddToCartController", urlPatterns = {"/add-to-cart"})
public class AddToCartController extends BaseRequiredAuthenController {

    @Override
    protected void processRequests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        CartService.expireCartItems(session);
        Account acc = (Account) session.getAttribute("acc");
        if (!RoleHelper.isCustomer(acc)) {
            session.setAttribute("cartMessage", "Chỉ tài khoản customer mới được mua hàng và thành toán.");
            response.sendRedirect("home");
            return;
        }

        Integer productId = ValidationUtil.parsePositiveInt(request.getParameter("productId"));
        if (productId == null) {
            session.setAttribute("cartMessage", "Sản phẩm không hợp lệ.");
            response.sendRedirect("home");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getProductById(productId);
        if (product == null || product.getQuantity() <= 0) {
            session.setAttribute("cartMessage", "Sản phẩm đã hết hàng.");
            response.sendRedirect("home");
            return;
        }

        if (!productDAO.reserveStock(productId, 1)) {
            session.setAttribute("cartMessage", "Số lượng còn lại không đủ.");
            response.sendRedirect("home");
            return;
        }

        Map<Integer, Cart> carts = CartService.getCartMap(session);
        if (carts.containsKey(productId)) {
            Cart cart = carts.get(productId);
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setProduct(productDAO.getProductById(productId));
            cart.refreshTimeout();
        } else {
            Product reservedProduct = productDAO.getProductById(productId);
            Cart cart = new Cart(reservedProduct, 1);
            carts.put(productId, cart);
        }

        CartService.clearPendingVnpay(session);
        session.setAttribute("carts", carts);
        String urlHistory = (String) session.getAttribute("urlHistory");
        response.sendRedirect(urlHistory == null ? "home" : urlHistory);
    }
}
