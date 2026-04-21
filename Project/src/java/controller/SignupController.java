package controller;

import dal.AcountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import util.ValidationUtil;

@WebServlet(name = "SignupController", urlPatterns = {"/signup"})
public class SignupController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String user = ValidationUtil.normalize(request.getParameter("user"));
        String pass = ValidationUtil.normalize(request.getParameter("pass"));
        String repass = ValidationUtil.normalize(request.getParameter("repass"));
        String email = ValidationUtil.normalize(request.getParameter("email"));

        request.setAttribute("formUser", user);
        request.setAttribute("formEmail", email);

        if (ValidationUtil.isBlank(user) || ValidationUtil.isBlank(pass)
                || ValidationUtil.isBlank(repass) || ValidationUtil.isBlank(email)) {
            request.setAttribute("mess", "Vui lòng nhập đầy đủ thông tin.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }


        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("mess", "Email không hợp lệ, vui lòng nhập đúng định dạng có ký tự @.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isStrongPassword(pass)) {
            request.setAttribute("mess", "Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (!pass.equals(repass)) {
            request.setAttribute("mess", "Mật khẩu không hợp lệ.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        AcountDAO accountDAO = new AcountDAO();
        if (accountDAO.checkAccountExist(user) != null) {
            request.setAttribute("mess", "Tài khoản đã tồn tại.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (accountDAO.getAccountByEmail(email) != null) {
            request.setAttribute("mess", "Email đã được sử dụng, vui lòng nhập email khác.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        Account account = new Account();
        account.setUser(user);
        account.setPass(pass);
        account.setEmail(email);
        account.setRole(Account.ROLE_CUSTOMER);
        account.setActive(false);
        String token = java.util.UUID.randomUUID().toString();
        accountDAO.insertAccountWithStatus(user, pass, email, false, token);

        String verifyLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
                + request.getContextPath() + "/activate?email=" + email + "&token=" + token;

        String subject = "Xác minh tài khoản - V-SNKR";
        String content = "Chào " + user + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tại V-SNKR. Vui lòng nhấn vào liên kết bên dưới để xác minh tài khoản của bạn:\n"
                + verifyLink + "\n\n"
                + "Liên kết này sẽ có hiệu lực cho đến khi bạn xác minh.\n\n"
                + "Trân trọng,\nĐội ngũ V-SNKR";
        util.SendMail.sendEmailWithContent(email, subject, content);

        request.setAttribute("successMess", "Đăng ký thành công! Vui lòng kiểm tra email để nhấn vào link xác minh tài khoản.");
        request.getRequestDispatcher("login.jsp").forward(request, response);


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
