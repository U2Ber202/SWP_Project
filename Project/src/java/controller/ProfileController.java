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
import util.ValidationUtil;

@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String fullname = ValidationUtil.normalize(request.getParameter("fullname"));
        String phone = ValidationUtil.normalize(request.getParameter("phone"));
        String email = ValidationUtil.normalize(request.getParameter("email"));
        String address = ValidationUtil.normalize(request.getParameter("address"));

        request.setAttribute("formFullname", fullname);
        request.setAttribute("formPhone", phone);
        request.setAttribute("formEmail", email);
        request.setAttribute("formAddress", address);

        if (!ValidationUtil.isBlank(phone) && !ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("error", "Số điện thoại phải đúng 10 chữ số và không được nhập chữ.");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isBlank(email) && !ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Email không hợp lệ, vui lòng nhập đúng định dạng có ký tự @.");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }

        AcountDAO dao = new AcountDAO();
        if (!ValidationUtil.isBlank(email) && dao.isEmailUsedByAnotherAccount(email, account.getUid())) {
            request.setAttribute("error", "Email này đã được tài khoản khác sử dụng.");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }

        account.setFullname(fullname);
        account.setPhone(phone);
        account.setEmail(email);
        account.setAddress(address);

        dao.updateProfile(account);
        session.setAttribute("acc", account);
        request.setAttribute("message", "Cập nhật hồ sơ thành công!");
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}
