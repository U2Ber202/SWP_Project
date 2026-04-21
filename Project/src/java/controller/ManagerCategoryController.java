package controller;

import dal.CategoryDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Category;
import model.Store;
import util.RoleHelper;

@WebServlet(name = "ManagerCategoryController", urlPatterns = {"/managerCategory"})
public class ManagerCategoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("acc");
        if (!RoleHelper.isOwner(a)) {
            response.sendRedirect("home");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        Store store = storeDAO.getStoreByOwnerId(a.getUid());

        List<Category> allCategories;
        if (store != null) {
            allCategories = new CategoryDAO().getCategoriesByStore(store.getId());
        } else {
            allCategories = new java.util.ArrayList<>();
        }

        final int PAGE_SIZE = 10;
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (Exception e) { page = 1; }

        int totalCategories = allCategories.size();
        int totalPage = (int) Math.ceil((double) totalCategories / PAGE_SIZE);
        if (page > totalPage && totalPage > 0) page = totalPage;
        if (page < 1) page = 1;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalCategories);
        List<Category> listCategories = (fromIndex < totalCategories) ? allCategories.subList(fromIndex, toIndex) : new java.util.ArrayList<>();

        request.setAttribute("listCategories", listCategories);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.getRequestDispatcher("ManagerCategory.jsp").forward(request, response);
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
