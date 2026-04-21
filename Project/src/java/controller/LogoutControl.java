package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.CartService;
import util.PasswordResetUtil;

@WebServlet(name = "LogoutControl", urlPatterns = {"/logout"})
public class LogoutControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session != null) {
            CartService.releaseAllCartItems(session);
            PasswordResetUtil.clear(session);
            session.removeAttribute("acc");
            session.removeAttribute("ownerStore");
            session.removeAttribute("role");
            session.removeAttribute("cartMessage");
            session.removeAttribute("pendingShipping");
            session.removeAttribute("pendingNote");
            session.removeAttribute("pendingVnpayTxnRef");
            session.removeAttribute("pendingVnpayAmount");
            session.removeAttribute("pendingVnpayAccountId");
            session.invalidate();
        }
        response.sendRedirect("home");
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
