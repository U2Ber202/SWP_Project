package controller;

import dal.AcountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ActivateAccountController", urlPatterns = {"/activate"})
public class ActivateAccountController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String token = request.getParameter("token");

        if (email == null || token == null) {
            request.setAttribute("mess", "Lời yêu cầu không hợp lệ.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        AcountDAO dao = new AcountDAO();
        boolean success = dao.activateAccount(email, token);

        if (success) {
            request.setAttribute("successMess", "Tài khoản của bạn đã được xác minh thành công! Bạn có thể đăng nhập ngay bây giờ.");
        } else {
            request.setAttribute("mess", "Liên kết xác minh không hợp lệ hoặc đã hết hạn.");
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
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
