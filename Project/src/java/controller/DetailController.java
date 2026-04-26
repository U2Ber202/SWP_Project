package controller;

import dal.FeedbackDAO;
import dal.ProductDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import util.CartService;

@WebServlet(name = "DetailController", urlPatterns = {"/detail"})
public class DetailController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            CartService.expireCartItems(request.getSession());
            
            String productIdStr = request.getParameter("productId");
            if (productIdStr == null || productIdStr.isEmpty()) {
                response.sendRedirect("home");
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            request.getSession().setAttribute("urlHistory", "detail?productId=" + productId);
            
            ProductDAO pdb = new ProductDAO();
            Product product = pdb.getProductById(productId);
            
            if (product == null) {
                response.sendRedirect("home");
                return;
            }
            
            List<Product> listLast = pdb.getLatestProductsByStoreId(product.getStoreId(), productId, 4);
            
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            request.setAttribute("listFeedbacks", feedbackDAO.getAllFeedbackForProduct(productId));
            
            model.Account acc = (model.Account) request.getSession().getAttribute("acc");
            boolean hasBought = false;
            if (acc != null) {
                hasBought = feedbackDAO.hasBoughtProduct(acc.getUid(), productId);
            }
            request.setAttribute("hasBought", hasBought);
            
            request.setAttribute("product", product);
            request.setAttribute("listLast", listLast);
            
            request.getRequestDispatcher("detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
