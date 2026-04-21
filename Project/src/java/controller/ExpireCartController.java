package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import util.CartService;
import util.RoleHelper;

@WebServlet(name = "ExpireCartController", urlPatterns = {"/expire-cart"})
public class ExpireCartController extends BaseRequiredAuthenController {

    @Override
    protected void processRequests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (!RoleHelper.isCustomer(acc)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int expiredCount = CartService.expireCartItems(session);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"expiredCount\":" + expiredCount + "}");
    }
}
