package controller;

import dal.ContactDAO;
import dal.OrderDAO;
import dal.ShippingDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Contact;
import model.Order;
import model.Shipping;

@WebServlet(name = "AddContactController", urlPatterns = {"/addContact"})
public class AddContactController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String message = request.getParameter("message");

            OrderDAO orderDAO = new OrderDAO();
            ShippingDAO shippingDAO = new ShippingDAO();
            
            Order order = orderDAO.getOrderById(orderId);
            if (order == null || order.getAccountId() != account.getUid()) {
                response.sendRedirect("home");
                return;
            }

            Shipping shipping = shippingDAO.getShippingByOrderId(orderId);
            if (shipping == null || !"Shipped".equalsIgnoreCase(shipping.getStatus())) {
                // Not allowed if not received
                request.getSession().setAttribute("error", "Bạn chỉ có thể gửi liên hệ cho những đơn hàng đã giao thành công.");
                response.sendRedirect("purchaseHistory");
                return;
            }

            ContactDAO contactDAO = new ContactDAO();
            Contact c = new Contact();
            c.setAccountId(account.getUid());
            c.setOrderId(orderId);
            int storeId = orderDAO.getStoreIdByOrderId(orderId);
            if (storeId <= 0) {
                // Fallback: try to see if order itself has it
                Order o = orderDAO.getOrderById(orderId);
                if (o != null) storeId = o.getStoreId();
            }
            
            if (storeId <= 0) {
                request.getSession().setAttribute("error", "Không tìm thấy thông tin cửa hàng cho đơn hàng này.");
                response.sendRedirect("purchaseHistory");
                return;
            }
            
            c.setStoreId(storeId);
            c.setMessage(message);
            
            contactDAO.insert(c);
            request.getSession().setAttribute("success", "Gửi yêu cầu hỗ trợ thành công! Shop sẽ phản hồi sớm nhất có thể.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi gửi yêu cầu.");
        }
        
        response.sendRedirect("purchaseHistory");
    }
}
