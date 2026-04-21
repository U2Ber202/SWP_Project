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
import model.Category;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "EditCategoryController", urlPatterns = {"/EditCategory"})
public class EditCategoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Account account = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isOwner(account)) {
            response.sendRedirect("home");
            return;
        }

        Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
        Integer categoryId = ValidationUtil.parsePositiveInt(request.getParameter("id"));
        String categoryName = ValidationUtil.normalize(request.getParameter("name"));
        String manufacturer = ValidationUtil.normalize(request.getParameter("manufacturer"));
        if (store == null || categoryId == null || ValidationUtil.isBlank(categoryName) || ValidationUtil.isBlank(manufacturer)) {
            response.sendRedirect("managerCategory");
            return;
        }

        Category category = new Category();
        category.setCid(categoryId);
        category.setCname(categoryName);
        category.setManufacturer(manufacturer);
        new CategoryDAO().updateCategoryByStore(category, store.getId());
        request.getSession().setAttribute("success", "Cập nhật danh mục thành công!");
        response.sendRedirect("managerCategory");
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
