package controller;

import dal.OrderDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Order;

@WebServlet(name = "WalletController", urlPatterns = {"/wallet"})
public class WalletController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");
        
        if (acc == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            OrderDAO dao = new OrderDAO();
            List<Order> history = dao.getOrdersByAccountId(acc.getUid());
            
            int spent = 0;
            if (history != null) {
                for (Order o : history) {
                    spent += o.getTotalPrice();
                }
            }

            request.setAttribute("orderHistory", history);
            request.setAttribute("totalSpent", spent);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi nạp dữ liệu ví: " + e.getMessage());
        }
        
        request.getRequestDispatcher("Wallet.jsp").forward(request, response);
    }
}
