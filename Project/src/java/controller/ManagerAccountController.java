package controller;

import dal.AcountDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "ManagerAccountController", urlPatterns = {"/managerAccount"})
public class ManagerAccountController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Account account = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isAdmin(account)) {
            response.sendRedirect("login.jsp");
            return;
        }

        AcountDAO accountDAO = new AcountDAO();
        String action = request.getParameter("action");
        String search = ValidationUtil.normalize(request.getParameter("search"));
        String message = null;
        String error = null;

        if ("createOwner".equals(action)) {
            String user = ValidationUtil.normalize(request.getParameter("user"));
            String pass = ValidationUtil.normalize(request.getParameter("pass"));
            String email = ValidationUtil.normalize(request.getParameter("email")).toLowerCase();

            request.setAttribute("formUser", user);
            request.setAttribute("formEmail", email);

            if (ValidationUtil.isBlank(user) || ValidationUtil.isBlank(pass) || ValidationUtil.isBlank(email)) {
                error = "Tên đăng nhập, email và mật khẩu không được dể trống.";
            } else if (!ValidationUtil.isValidEmail(email)) {
                error = "Email không hợp lệ, vui lòng nhập đúng định dạng có ký tự @.";
            } else if (!ValidationUtil.isStrongPassword(pass)) {
                error = "Mật khẩu phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.";
            } else if (accountDAO.checkAccountExist(user) != null) {
                error = "Tên đăng nhạp đã tồn tại.";
            } else if (accountDAO.getAccountByEmail(email) != null) {
                error = "Email đã được sử dụng.";
            } else {
                accountDAO.insertOwnerAccount(user, pass, email);
                message = "Tạo tài khoản owner thành công. Admin có thể gán tài khoản này cho cửa hàng.";
            }
            request.setAttribute("accountCreateRole", "owner");
        }

        final int PAGE_SIZE = 10;
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (Exception e) { page = 1; }

        List<Account> allAccounts = accountDAO.searchAccounts(search);
        int totalAccounts = allAccounts.size();
        int totalPage = (int) Math.ceil((double) totalAccounts / PAGE_SIZE);
        if (page > totalPage && totalPage > 0) page = totalPage;
        if (page < 1) page = 1;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalAccounts);
        List<Account> accounts = (fromIndex < totalAccounts) ? allAccounts.subList(fromIndex, toIndex) : new java.util.ArrayList<>();

        request.setAttribute("accounts", accounts);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("ownerAccounts", accountDAO.getAccountsByRole(Account.ROLE_OWNER));
        request.setAttribute("message", message);
        request.setAttribute("error", error);
        request.setAttribute("search", search);
        request.getRequestDispatcher("ManagerAccount.jsp").forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
