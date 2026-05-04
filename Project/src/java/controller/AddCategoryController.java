package controller;

import dal.CategoryDAO;
import dal.StoreDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "AddCategoryController", urlPatterns = {"/addcategory"})
public class AddCategoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("managerCategory");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = ValidationUtil.normalize(request.getParameter("name"));
        
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("acc");
        
        if (!RoleHelper.isOwner(a)) {
            response.sendRedirect("home");
            return;
        }
        
        if (ValidationUtil.isBlank(name)) {
            session.setAttribute("error", "Tên danh mục không được để trống!");
            response.sendRedirect("managerCategory");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        Store store = storeDAO.getStoreByOwnerId(a.getUid());

        if (store == null) {
            session.setAttribute("error", "Không tìm thấy thông tin cửa hàng!");
            response.sendRedirect("managerCategory");
            return;
        }

        CategoryDAO categoryDAO = new CategoryDAO();
        categoryDAO.insertCategory(name, store.getStoreId());
        
        session.setAttribute("success", "Thêm danh mục [" + name + "] thành công!");
        response.sendRedirect("managerCategory");
    }
}

