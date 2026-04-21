package controller;

import dal.AcountDAO;
import dal.OrderDAO;
import dal.ShippingDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Order;
import model.Shipping;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "OrderController", urlPatterns = {"/orders"})
public class OrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        OrderDAO orderDAO = new OrderDAO();
        ShippingDAO shippingDAO = new ShippingDAO();
        List<Order> orders;
        Map<Integer, Shipping> shippingByOrderId = new HashMap<>();

        if (RoleHelper.isOwner(account)) {
            Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
            if (store == null) {
                response.sendRedirect("home");
                return;
            }
            orders = orderDAO.getOrdersByStoreId(store.getId());
            request.setAttribute("storeShippers", new AcountDAO().getShippersByStoreId(store.getId()));
            for (Order order : orders) {
                shippingByOrderId.put(order.getId(), shippingDAO.getShippingByOrderIdAndStoreId(order.getId(), store.getId()));
            }
        } else if (RoleHelper.isShipper(account)) {
            orders = orderDAO.getOrdersByShipperId(account.getUid());
            for (Order order : orders) {
                shippingByOrderId.put(order.getId(), shippingDAO.getShippingByOrderIdAndShipperId(order.getId(), account.getUid()));
            }
        } else {
            response.sendRedirect("home");
            return;
        }

        final int PAGE_SIZE = 10;
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (Exception e) { page = 1; }

        int totalOrdersCount = orders.size();
        int totalPage = (int) Math.ceil((double) totalOrdersCount / PAGE_SIZE);
        if (page > totalPage && totalPage > 0) page = totalPage;
        if (page < 1) page = 1;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalOrdersCount);
        List<Order> listOrders = (fromIndex < totalOrdersCount) ? orders.subList(fromIndex, toIndex) : new java.util.ArrayList<>();

        request.setAttribute("orders", listOrders);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("shippingByOrderId", shippingByOrderId);
        request.getRequestDispatcher("order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        if (!RoleHelper.isOwner(account)) {
            response.sendRedirect("home");
            return;
        }

        Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
        if (store == null) {
            response.sendRedirect("home");
            return;
        }

        if (!"assignShipper".equals(request.getParameter("action"))) {
            response.sendRedirect("orders");
            return;
        }

        Integer orderId = ValidationUtil.parsePositiveInt(request.getParameter("orderId"));
        Integer shipperId = ValidationUtil.parsePositiveInt(request.getParameter("shipperId"));
        if (orderId == null || shipperId == null) {
            response.sendRedirect("orders");
            return;
        }

        Order order = new OrderDAO().getOrderByIdAndStoreId(orderId, store.getId());
        if (order == null) {
            response.sendRedirect("orders");
            return;
        }

        Account potentialShipper = new AcountDAO().getAccountById(shipperId);
        if (potentialShipper == null || !potentialShipper.isShipper()) {
            response.sendRedirect("orders");
            return;
        }

        ShippingDAO shippingDAO = new ShippingDAO();
        Shipping shipping = shippingDAO.getShippingByOrderIdAndStoreId(orderId, store.getId());
        if (shipping == null || "Shipped".equalsIgnoreCase(shipping.getStatus())) {
            response.sendRedirect("orders");
            return;
        }

        shippingDAO.assignShipperByStore(order.getShippingId(), store.getId(), shipperId);
        response.sendRedirect("orders");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}