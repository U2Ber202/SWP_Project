package controller;

import dal.ContactDAO;
import dal.OrderDAO;
import dal.ShippingDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Contact;
import model.Order;
import model.Shipping;

@WebServlet(name = "PurchaseHistoryController", urlPatterns = {"/purchaseHistory"})
public class PurchaseHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        ShippingDAO shippingDAO = new ShippingDAO();
        
        List<Order> orders = orderDAO.getOrdersByAccountId(account.getUid());
        Map<Integer, Shipping> shippingByOrderId = new HashMap<>();
        
        for (Order order : orders) {
            shippingByOrderId.put(order.getId(), shippingDAO.getShippingByOrderId(order.getId()));
        }

        request.setAttribute("orders", orders);
        request.setAttribute("shippingByOrderId", shippingByOrderId);
        
        ContactDAO contactDAO = new ContactDAO();
        List<Contact> contactList = contactDAO.getContactsByAccount(account.getUid());
        
        Set<Integer> supportedOrderIds = new HashSet<>();
        for (Contact c : contactList) {
            supportedOrderIds.add(c.getOrderId());
        }
        
        request.setAttribute("contactList", contactList);
        request.setAttribute("supportedOrderIds", supportedOrderIds);
        
        request.getRequestDispatcher("purchaseHistory.jsp").forward(request, response);
    }
}
