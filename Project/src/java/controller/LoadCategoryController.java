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

@WebServlet(name = "LoadCategoryController", urlPatterns = {"/loadCategory"})
public class LoadCategoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Account account = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.isOwner(account)) {
            response.sendRedirect("home");
            return;
        }

        Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
        Integer categoryId = ValidationUtil.parsePositiveInt(request.getParameter("cid"));
        if (store == null || categoryId == null) {
            response.sendRedirect("managerCategory");
            return;
        }

        Category category = new CategoryDAO().getCategoryByIdAndStore(categoryId, store.getId());
        if (category == null) {
            response.sendRedirect("managerCategory");
            return;
        }
        request.setAttribute("category", category);
        request.getRequestDispatcher("EditCategory.jsp").forward(request, response);
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
