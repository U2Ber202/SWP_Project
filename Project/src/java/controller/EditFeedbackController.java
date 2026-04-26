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
import model.Feedback;
import util.ValidationUtil;

@WebServlet(name = "EditFeedbackController", urlPatterns = {"/editFeedback"})
public class EditFeedbackController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");
        
        if (acc == null) {
            response.sendRedirect("login");
            return;
        }

        String idStr = request.getParameter("feedbackId");
        String ratingStr = request.getParameter("rating");
        String content = request.getParameter("content");
        String productIdStr = request.getParameter("productId");

        if (ValidationUtil.isBlank(idStr) || ValidationUtil.isBlank(ratingStr) || ValidationUtil.isBlank(content)) {
            session.setAttribute("error", "Vui lòng nhập đầy đủ thông tin cập nhật.");
            response.sendRedirect("detail?productId=" + productIdStr);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            int rating = Integer.parseInt(ratingStr);
            int productId = Integer.parseInt(productIdStr);
            
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            Feedback f = feedbackDAO.getFeedbackById(id);
            
            if (f != null && f.getAccountId() == acc.getUid()) {
                if (f.isEdited()) {
                    session.setAttribute("error", "Đánh giá này đã được sửa một lần, không thể sửa thêm.");
                } else {
                    feedbackDAO.updateFeedback(id, rating, content);
                    session.setAttribute("success", "Cập nhật đánh giá thành công!");
                }
            } else {
                session.setAttribute("error", "Bạn không có quyền sửa đánh giá này.");
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
