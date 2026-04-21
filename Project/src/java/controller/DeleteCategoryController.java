package controller;

import dal.CategoryDAO;
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

@WebServlet(name = "DeleteCategoryController", urlPatterns = {"/delete-category"})
public class DeleteCategoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isOwner(account)) {
            response.sendRedirect("home");
            return;
        }

        Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
        if (store == null) {
            response.sendRedirect("managerCategory");
            return;
        }

        Integer categoryId = ValidationUtil.parsePositiveInt(request.getParameter("cid"));
        if (categoryId == null) {
            response.sendRedirect("managerCategory");
            return;
        }

        new CategoryDAO().deleteCategoryByIdAndStore(categoryId, store.getId());
        request.getSession().setAttribute("success", "Xóa danh mục thành công!");
        response.sendRedirect("managerCategory");
    }
}
