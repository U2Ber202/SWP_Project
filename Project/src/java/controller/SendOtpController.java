package controller;

import dal.AcountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import util.PasswordResetUtil;
import util.PasswordUtil;
import util.SendMail;
import util.ValidationUtil;

@WebServlet(name = "SendOtpController", urlPatterns = {"/send-otp"})
public class SendOtpController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = ValidationUtil.normalize(request.getParameter("email"));
        AcountDAO dao = new AcountDAO();
        Account account = dao.getAccountByEmail(email);

        if (account == null) {
            request.setAttribute("error", "Email không tồn tại trong hệ thống.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        if (!account.isActive()) {
            request.setAttribute("error", "Tài khoản đã bị vô hiệu hóa, không thể reset mật khẩu.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        String otp = SendMail.generateOTP();
        boolean isSent = SendMail.sendEmail(email, otp);
        if (!isSent) {
            request.setAttribute("error", "Không gửi được OTP. Vui lòng kiểm tra cấu hình email và thử lại.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        PasswordResetUtil.clear(session);
        session.setAttribute(PasswordResetUtil.RESET_EMAIL, email);
        session.setAttribute(PasswordResetUtil.RESET_USER, account.getUser());
        session.setAttribute(PasswordResetUtil.RESET_OTP_HASH, PasswordUtil.hash(otp));
        session.setAttribute(PasswordResetUtil.RESET_OTP_EXPIRES_AT, System.currentTimeMillis() + PasswordResetUtil.OTP_TTL_MILLIS);
        session.setAttribute(PasswordResetUtil.RESET_OTP_VERIFIED, Boolean.FALSE);

        request.setAttribute("message", "Đã gửi mã OTP, vui lòng kiểm tra hộp thư.");
        request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
    }
}
