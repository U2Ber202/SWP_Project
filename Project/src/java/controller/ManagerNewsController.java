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
            newsList = newsDAO.getNewsForAdmin(); // Admin thấy tất cả
        } else {
            Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
            if (store == null) {
                response.sendRedirect("home");
                return;
            }
            newsList = newsDAO.getOnlyStoreNews(store.getId()); // Owner thấy bài của mình
        }

        request.setAttribute("newsList", newsList);
        request.getRequestDispatcher("ManagerNews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Account account = (Account) request.getSession().getAttribute("acc");
        if (account == null || (!RoleHelper.isAdmin(account) && !RoleHelper.isOwner(account))) {
            response.sendRedirect("login.jsp");
            return;
        }

        NewsDAO newsDAO = new NewsDAO();
        String action = request.getParameter("action");

        // Xác định storeId nếu là owner
        Integer storeId = null;
        if (RoleHelper.isOwner(account)) {
            Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
            if (store != null) {
                storeId = store.getId();
            }
        }

        if ("add".equals(action)) {
            String title = request.getParameter("title") != null ? request.getParameter("title").trim() : "";
            String content = request.getParameter("content") != null ? request.getParameter("content").trim() : "";
            String image = request.getParameter("image") != null ? request.getParameter("image").trim() : "";

            // ✅ Validate title trùng
            if (newsDAO.isTitleExist(title)) {
                request.getSession().setAttribute("error", "Tiêu đề \"" + title + "\" đã tồn tại. Vui lòng chọn tiêu đề khác.");
                response.sendRedirect("managerNews");
                return;
            }

            News n = new News();
            n.setTitle(title);
            n.setContent(content);
            n.setImage(image.isEmpty() ? null : image);
            n.setStoreId(storeId); // NULL nếu là Admin
            n.setVisible(true);     // ✅ Đảm bảo visible = true

            newsDAO.insert(n);
            request.getSession().removeAttribute("error");
            request.getSession().setAttribute("success", "Đăng tin thành công!");

        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title") != null ? request.getParameter("title").trim() : "";
            String content = request.getParameter("content") != null ? request.getParameter("content").trim() : "";
            String image = request.getParameter("image") != null ? request.getParameter("image").trim() : "";

            System.out.println("ID: " + id + ", Title: " + title);

            News n = newsDAO.getNewsById(id);
            if (n != null) {
                boolean canEdit = RoleHelper.isAdmin(account)
                        || (storeId != null && storeId.equals(n.getStoreId()));

                if (canEdit) {
                    // ✅ Validate title trùng (bỏ qua bài hiện tại)
                    if (newsDAO.isTitleExistForAnother(title, id)) {
                        request.getSession().setAttribute("error", "Tiêu đề \"" + title + "\" đã được dùng bởi bài khác.");
                        response.sendRedirect("managerNews");
                        return;
                    }
                    n.setTitle(title);
                    n.setContent(content);
                    n.setImage(image.isEmpty() ? null : image);
                    newsDAO.update(n);
                    request.getSession().setAttribute("success", "Cập nhật tin tức thành công!");
                }
            } 

        } else if ("toggleVisible".equals(action)) {
            // ✅ Toggle visible thay vì xóa
            int id = Integer.parseInt(request.getParameter("id"));
            News n = newsDAO.getNewsById(id);
            if (n != null) {
                boolean canToggle = RoleHelper.isAdmin(account)
                        || (storeId != null && storeId.equals(n.getStoreId()));
                if (canToggle) {
                    newsDAO.toggleVisible(id);
                    String newState = n.isVisible() ? "ẩn" : "hiển thị";
                    request.getSession().setAttribute("success", "Đã chuyển bài viết sang trạng thái " + newState + ".");
                }
            }
        }

        response.sendRedirect("managerNews");
    }
}
