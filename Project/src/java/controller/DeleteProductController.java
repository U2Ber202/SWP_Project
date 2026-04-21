package controller;

import dal.ProductDAO;
import dal.StoreDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "DeleteProductController", urlPatterns = {"/delete"})
public class DeleteProductController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Account account = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isOwner(account)) {
            response.sendRedirect("home");
            return;
        }

        Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
        Integer productId = ValidationUtil.parsePositiveInt(request.getParameter("pid"));
        if (store == null || productId == null) {
            response.sendRedirect("manager");
            return;
        }

        if (new ProductDAO().deleteProductByStore(productId, store.getId())) {
            request.getSession().setAttribute("success", "Xóa sản phẩm thành công!");
        } else {
            request.getSession().setAttribute("error", "Không thể xóa mẫu giày này. Có thể sản phẩm đã có lịch sử đơn hàng hoặc đang trong giỏ hàng.");
        }
        response.sendRedirect("manager");
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
