package controller;

import dal.AcountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.PasswordResetUtil;
import util.ValidationUtil;

@WebServlet(name = "ResetPasswordController", urlPatterns = {"/reset-password"})
public class ResetPasswordController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!PasswordResetUtil.isVerified(request.getSession())) {
            response.sendRedirect("forgot-password.jsp");
            return;
        }
        request.getRequestDispatcher("reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String newPass = ValidationUtil.normalize(request.getParameter("newPassword"));
        String confirmPass = ValidationUtil.normalize(request.getParameter("confirmPassword"));

        HttpSession session = request.getSession();
        String resetUser = (String) session.getAttribute(PasswordResetUtil.RESET_USER);

        if (ValidationUtil.isBlank(resetUser) || !PasswordResetUtil.isVerified(session)) {
            PasswordResetUtil.clear(session);
            response.sendRedirect("forgot-password.jsp");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            request.setAttribute("error", "Mật khẩu không khớp.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isStrongPassword(newPass)) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        AcountDAO dao = new AcountDAO();
        dao.UpDatePassWord(newPass, resetUser);
        PasswordResetUtil.clear(session);

        request.setAttribute("successMess", "Thay đổi mật khẩu thành công. Vui lòng đăng nhập lại.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
