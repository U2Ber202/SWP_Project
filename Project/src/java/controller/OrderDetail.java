package controller;

import dal.OrderDAO;
import dal.OrderDetailDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Order;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "OrderDetail", urlPatterns = {"/orderdetail"})
public class OrderDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account acc = (Account) request.getSession().getAttribute("acc");
        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer orderId = ValidationUtil.parsePositiveInt(request.getParameter("orderId"));
        if (orderId == null) {
            response.sendRedirect("orders");
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        Order order;
        
        if (RoleHelper.isOwner(acc)) {
            Store store = new StoreDAO().getStoreByOwnerId(acc.getUid());
            if (store == null) {
                response.sendRedirect("orders");
                return;
            }
            order = orderDAO.getOrderByIdAndStoreId(orderId, store.getId());
        } else if (RoleHelper.isAdmin(acc)) {
            order = orderDAO.getOrderById(orderId);
        } else if (RoleHelper.isShipper(acc)) {
            order = orderDAO.getOrderByIdAndShipperId(orderId, acc.getUid());
        } else {
            // Customer role
            order = orderDAO.getOrderById(orderId);
            if (order != null && order.getAccountId() != acc.getUid()) {
                order = null; // Security: cannot view others' orders
            }
        }

        if (order == null) {
            if (RoleHelper.isCustomer(acc)) {
                response.sendRedirect("purchaseHistory");
            } else {
                response.sendRedirect("orders");
            }
            return;
        }

        List<model.OrderDetail> orderDetails = new OrderDetailDAO().getAllOrderDetailById(orderId);
        request.setAttribute("orderDetails", orderDetails);
        request.setAttribute("order", order);
        request.setAttribute("orderId", orderId);
        request.getRequestDispatcher("orderDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
