package controller;

import dal.ContactDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Contact;
import model.Store;
import util.RoleHelper;

@WebServlet(name = "ManagerContactController", urlPatterns = {"/managerContact"})
public class ManagerContactController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (account == null || (!RoleHelper.isAdmin(account) && !RoleHelper.isOwner(account))) {
            response.sendRedirect("login.jsp");
            return;
        }

        ContactDAO contactDAO = new ContactDAO();
        List<Contact> contactList;

        if (RoleHelper.isAdmin(account)) {
            contactList = contactDAO.getAllContacts();
        } else {
            Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
            if (store == null) {
                request.setAttribute("error", "Tài khoản Owner của bạn chưa được liên kết với cửa hàng nào. Vui lòng liên hệ Admin.");
                contactList = new java.util.ArrayList<>();
            } else {
                request.setAttribute("storeName", store.getName());
                contactList = contactDAO.getContactsByStore(store.getId());
            }
        }

        request.setAttribute("contactList", contactList);
        request.getRequestDispatcher("ManagerContact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (account == null || (!RoleHelper.isAdmin(account) && !RoleHelper.isOwner(account))) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        ContactDAO contactDAO = new ContactDAO();

        if ("updateStatus".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");
            contactDAO.updateStatus(id, status);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            contactDAO.delete(id);
        } else if ("respond".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String responseMessage = request.getParameter("responseMessage");
            contactDAO.updateResponse(id, responseMessage);
        }

        response.sendRedirect("managerContact");
    }
}
