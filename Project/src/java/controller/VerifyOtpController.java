package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.PasswordResetUtil;
import util.PasswordUtil;
import util.ValidationUtil;

@WebServlet(name = "VerifyOtpController", urlPatterns = {"/verify-otp"})
public class VerifyOtpController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userOtp = ValidationUtil.normalize(request.getParameter("otp"));
        HttpSession session = request.getSession();

        String otpHash = (String) session.getAttribute(PasswordResetUtil.RESET_OTP_HASH);
        if (ValidationUtil.isBlank((String) session.getAttribute(PasswordResetUtil.RESET_EMAIL)) || ValidationUtil.isBlank(otpHash)) {
            response.sendRedirect("forgot-password.jsp");
            return;
        }

        if (PasswordResetUtil.isOtpExpired(session)) {
            PasswordResetUtil.clear(session);
            request.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }

        if (PasswordUtil.matches(userOtp, otpHash)) {
            session.setAttribute(PasswordResetUtil.RESET_OTP_VERIFIED, Boolean.TRUE);
            session.removeAttribute(PasswordResetUtil.RESET_OTP_HASH);
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
            return;
        }

        request.setAttribute("error", "Mã OTP không đúng.");
        request.getRequestDispatcher("verify-otp.jsp").forward(request, response);

    }
}
