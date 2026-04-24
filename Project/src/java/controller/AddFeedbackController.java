package controller;

import dal.FeedbackDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import util.ValidationUtil;

@WebServlet(name = "AddFeedbackController", urlPatterns = {"/addFeedback"})
public class AddFeedbackController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");
        
        if (acc == null) {
            response.sendRedirect("login");
            return;
        }

        String productIdStr = request.getParameter("productId");
        String storeIdStr = request.getParameter("storeId");
        String ratingStr = request.getParameter("rating");
        String content = request.getParameter("content");

        if (ValidationUtil.isBlank(productIdStr) || ValidationUtil.isBlank(storeIdStr) || ValidationUtil.isBlank(ratingStr) || ValidationUtil.isBlank(content)) {
            session.setAttribute("error", "Vui lòng nhập đầy đủ thông tin đánh giá.");
            response.sendRedirect("detail?productId=" + (productIdStr != null ? productIdStr : ""));
            return;
        }
        
        if (content.length() > 50) {
            session.setAttribute("error", "Nội dung đánh giá không được vượt quá 50 ký tự.");
            response.sendRedirect("detail?productId=" + productIdStr);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdStr);
            int storeId = Integer.parseInt(storeIdStr);
            int rating = Integer.parseInt(ratingStr);
            
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            
            // Requirement: Only customers who bought the product can comment
            if (!feedbackDAO.hasBoughtProduct(acc.getUid(), productId)) {
                session.setAttribute("error", "Bạn phải mua sản phẩm này mới có thể để lại đánh giá.");
                response.sendRedirect("detail?productId=" + productId);
                return;
            }
            
            // Check condition: maximum 2 comments per user per product
            int feedbackCount = feedbackDAO.countFeedbackByUserOnProduct(acc.getUid(), productId);
            if (feedbackCount >= 2) {
                session.setAttribute("error", "Bạn chỉ được phép gửi tối đa 2 đánh giá cho mỗi sản phẩm.");
                response.sendRedirect("detail?productId=" + productId);
                return;
            }
            
            boolean success = feedbackDAO.insertFeedback(acc.getUid(), productId, storeId, rating, content);
            
            if (success) {
                session.setAttribute("success", "Cảm ơn bạn đã gửi đánh giá!");
            } else {
                session.setAttribute("error", "Có lỗi xảy ra khi lưu đánh giá. Vui lòng thử lại sau.");
            }
            response.sendRedirect("detail?productId=" + productId);
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
