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

@WebServlet(name = "LoadAccountController", urlPatterns = {"/loadAccount"})
public class LoadAccountController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Account currentAccount = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isAdmin(currentAccount)) {
            response.sendRedirect("home");
            return;
        }

        Integer accountId = ValidationUtil.parsePositiveInt(request.getParameter("pid"));
        if (accountId == null) {
            response.sendRedirect("managerAccount");
            return;
        }

        Account account = new AcountDAO().getAccountById(accountId);
        if (account == null) {
            response.sendRedirect("managerAccount");
            return;
        }

        request.setAttribute("account", account);
        request.getRequestDispatcher("EditAccount.jsp").forward(request, response);
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
