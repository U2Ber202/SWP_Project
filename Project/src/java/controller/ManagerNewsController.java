package controller;

import dal.NewsDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.News;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "ManagerNewsController", urlPatterns = {"/managerNews"})
public class ManagerNewsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (account == null || (!RoleHelper.isAdmin(account) && !RoleHelper.isOwner(account))) {
            response.sendRedirect("login.jsp");
            return;
        }

        NewsDAO newsDAO = new NewsDAO();
        List<News> newsList;

        if (RoleHelper.isAdmin(account)) {
            newsList = newsDAO.getAllNews();
        } else {
            Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
            if (store == null) {
                response.sendRedirect("home");
                return;
            }
            newsList = newsDAO.getOnlyStoreNews(store.getId());
        }

        request.setAttribute("newsList", newsList);
        request.getRequestDispatcher("ManagerNews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (account == null || (!RoleHelper.isAdmin(account) && !RoleHelper.isOwner(account))) {
            response.sendRedirect("login.jsp");
            return;
        }

        NewsDAO newsDAO = new NewsDAO();
        String action = request.getParameter("action");
        
        Integer storeId = null;
        if (RoleHelper.isOwner(account)) {
            Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
            if (store != null) storeId = store.getId();
        }

        if ("add".equals(action)) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String image = request.getParameter("image");
            
            News n = new News();
            n.setTitle(title);
            n.setContent(content);
            n.setImage(image);
            n.setStoreId(storeId); // NULL if Admin
            
            newsDAO.insert(n);
        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String image = request.getParameter("image");
            boolean isVisible = request.getParameter("isVisible") != null;
            
            News n = newsDAO.getNewsById(id);
            if (n != null) {
                // Security check: Admin can edit anything, Owner only theirs
                if (RoleHelper.isAdmin(account) || (storeId != null && storeId.equals(n.getStoreId()))) {
                    n.setTitle(title);
                    n.setContent(content);
                    n.setImage(image);
                    n.setIsVisible(isVisible);
                    newsDAO.update(n);
                }
            }
        } else if ("toggleStatus".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean currentStatus = Boolean.parseBoolean(request.getParameter("status"));
            News n = newsDAO.getNewsById(id);
            if (n != null) {
                if (RoleHelper.isAdmin(account) || (storeId != null && storeId.equals(n.getStoreId()))) {
                    newsDAO.updateStatus(id, !currentStatus);
                }
            }
        }

        response.sendRedirect("managerNews");
    }
}
