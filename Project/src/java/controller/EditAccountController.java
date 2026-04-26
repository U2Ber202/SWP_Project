package controller;

import dal.AcountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "EditAccountController", urlPatterns = {"/EditAccount"})
public class EditAccountController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Kiểm tra quyền admin
        Account currentAccount = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isAdmin(currentAccount)) {
            response.sendRedirect("home");
            return;
        }

        // Kiểm tra ID hợp lệ
        Integer accountId = ValidationUtil.parsePositiveInt(request.getParameter("id"));
        if (accountId == null) {
            response.sendRedirect("managerAccount");
            return;
        }

        AcountDAO adb = new AcountDAO();
        Account oldAccount = adb.getAccountById(accountId);

        // Tài khoản không tồn tại
        if (oldAccount == null) {
            response.sendRedirect("managerAccount");
            return;
        }

        String newEmail = ValidationUtil.normalize(request.getParameter("email"));

        // Validate email không được trống
        if (ValidationUtil.isBlank(newEmail)) {
            request.setAttribute("account", oldAccount);
            request.setAttribute("error", "Email không được để trống.");
            request.getRequestDispatcher("EditAccount.jsp").forward(request, response);
            return;
        }

        // Validate định dạng email
        if (!ValidationUtil.isValidEmail(newEmail)) {
            request.setAttribute("account", oldAccount);
            request.setAttribute("error", "Email không hợp lệ, vui lòng nhập đúng định dạng.");
            request.getRequestDispatcher("EditAccount.jsp").forward(request, response);
            return;
        }

        // Validate email trùng với tài khoản khác (dùng method có sẵn trong DAO)
        if (adb.isEmailUsedByAnotherAccount(newEmail, accountId)) {
            request.setAttribute("account", oldAccount);
            request.setAttribute("error", "Email đã được sử dụng bởi tài khoản khác.");
            request.getRequestDispatcher("EditAccount.jsp").forward(request, response);
            return;
        }

        boolean newStatus;
        if ("admin".equalsIgnoreCase(oldAccount.getRole())) {
            newStatus = true;
        } else {
            newStatus = "active".equalsIgnoreCase(request.getParameter("active"));
        }
        
        Account account = new Account();
        account.setUid(accountId);
        account.setUser(request.getParameter("user"));
        account.setIsAdmin(0);
        account.setActive(newStatus);
        account.setEmail(newEmail);

        adb.updateAccount(account);

        if (oldAccount != null && oldAccount.isActive() != newStatus) {
            String reason = request.getParameter("reason");
            String statusVerb = newStatus ? "KÍCH HOẠT" : "KHÓA";
            String subject = "Thông báo trạng thái tài khoản - V-SNKR";
            String content = "Chào " + (oldAccount.getFullname() != null ? oldAccount.getFullname() : oldAccount.getUser()) + ",\n\n"
                    + "Tài khoản của bạn đã được " + statusVerb + " bởi quản trị viên.\n"
                    + "Lý do: " + (reason != null && !reason.trim().isEmpty() ? reason : "Không có lý do cụ thể.") + "\n\n"
                    + "Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với bộ phận hỗ trợ.\n"
                    + "Trân trọng,\nĐội ngũ V-SNKR";
            util.SendMail.sendEmailWithContent(oldAccount.getEmail(), subject, content);
        }

        request.getSession().setAttribute("success", "Cập nhật tài khoản thành công!");
        response.sendRedirect("managerAccount");
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
