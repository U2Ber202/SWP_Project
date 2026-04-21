package controller;

import dal.AcountDAO;
import dal.StoreDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Store;
import util.CartService;
import util.ValidationUtil;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] arr = request.getCookies();
        if (arr != null) {
            for (Cookie cookie : arr) {
                if ("userC".equals(cookie.getName())) {
                    request.setAttribute("username", cookie.getValue());
                    request.setAttribute("rememberUsername", true);
                }
            }
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = ValidationUtil.normalize(request.getParameter("Username"));
        String pass = request.getParameter("Password");
        String remember = request.getParameter("r");

        AcountDAO accountDAO = new AcountDAO();
        Account account = accountDAO.login(user, pass);
        if (account == null) {
            request.setAttribute("mess", "Sai mật khẩu hoặc tên người dùng không tồn tại.");
            request.setAttribute("username", user);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (!account.isActive()) {
            request.setAttribute("mess", "Tài khoản của bạn đang bị khóa hoặc chưa được xác minh. Vui lòng liên hệ Admin.");
            request.setAttribute("username", user);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }


        HttpSession session = request.getSession();
        CartService.expireCartItems(session);
        StoreDAO storeDAO = new StoreDAO();
        Store ownerStore = storeDAO.getStoreByOwnerId(account.getUid());
        Store warehouseStore = storeDAO.getStoreByWarehouseManagerId(account.getUid());
        Store shipperStore = storeDAO.getStoreByShipperId(account.getUid());
        session.setAttribute("acc", account);
        session.setAttribute("isAdmin", account.getIsAdmin());
        session.setAttribute("role", account.getRole());
        session.setAttribute("ownerStore", ownerStore);
        session.setAttribute("warehouseStore", warehouseStore);
        session.setAttribute("shipperStore", shipperStore);
        if (warehouseStore != null) {
            session.setAttribute("warehouseStoreId", String.valueOf(warehouseStore.getId()));
        }

        Cookie userCookie = new Cookie("userC", user);
        userCookie.setHttpOnly(true);
        userCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        if (remember != null) {
            userCookie.setMaxAge(60 * 60 * 24 * 30);
        } else {
            userCookie.setMaxAge(0);
        }
        response.addCookie(userCookie);
        response.sendRedirect("home");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}