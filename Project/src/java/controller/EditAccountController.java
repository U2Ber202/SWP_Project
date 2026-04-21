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
        Account currentAccount = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isAdmin(currentAccount)) {
            response.sendRedirect("home");
            return;
        }
        Integer accountId = ValidationUtil.parsePositiveInt(request.getParameter("id"));
        if (accountId == null) {
            response.sendRedirect("managerAccount");
            return;
        }

        Account account = new Account();
        account.setUid(accountId);
        account.setUser(request.getParameter("user"));
        account.setIsAdmin(0);
        boolean newStatus = "active".equalsIgnoreCase(request.getParameter("active"));
        account.setActive(newStatus);
        account.setEmail(request.getParameter("email"));
        
        AcountDAO adb = new AcountDAO();
        Account oldAccount = adb.getAccountById(accountId);
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
