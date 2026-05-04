package controller;

import dal.CategoryDAO;
import dal.ProductDAO;
import dal.StoreDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Product;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "EditProductController", urlPatterns = {"/edit"})
public class EditProductController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String id = ValidationUtil.normalize(request.getParameter("id"));
        String pName = ValidationUtil.normalize(request.getParameter("name"));
        String manufacturerIdStr = ValidationUtil.normalize(request.getParameter("manufacturerId"));
        String cid = ValidationUtil.normalize(request.getParameter("category"));
        String des = ValidationUtil.normalize(request.getParameter("description"));

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        if (!RoleHelper.canManageProduct(account)) {
            response.sendRedirect("home");
            return;
        }

        Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
        if (store == null) {
            response.sendRedirect("manager");
            return;
        }

        Integer productId = ValidationUtil.parsePositiveInt(id);
        if (productId == null) {
            response.sendRedirect("manager");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        Product existingProduct = productDAO.getProductById(productId);
        if (existingProduct == null || existingProduct.getStoreId() != store.getStoreId()) {
            response.sendRedirect("manager");
            return;
        }

        if (!isValidProductInput(pName, manufacturerIdStr, cid, des)) {
            forwardWithValidationError(request, response, store.getStoreId(),
                    "Vui lòng nhập đầy đủ thông tin hợp lệ.");
            return;
        }

        int manufacturerId = Integer.parseInt(manufacturerIdStr);
        int categoryId = Integer.parseInt(cid);

        existingProduct.setName(pName);
        existingProduct.setManufacturerId(manufacturerId);
        existingProduct.setCategoryId(categoryId);
        existingProduct.setDescription(des);

        productDAO.updateProduct(existingProduct);
        session.setAttribute("success", "Cập nhật sản phẩm thành công!");
        response.sendRedirect("manager");
    }

    private boolean isValidProductInput(String pName, String mId, String cid, String des) {
        return !ValidationUtil.isBlank(pName)
                && ValidationUtil.isNonNegativeInteger(mId)
                && ValidationUtil.isNonNegativeInteger(cid)
                && !ValidationUtil.isBlank(des);
    }

    private void forwardWithValidationError(HttpServletRequest request, HttpServletResponse response, int storeId, String message)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        int id = ValidationUtil.parsePositiveInt(idParam);
        
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getProductById(id);
        
        dal.ColorDAO colorDAO = new dal.ColorDAO();
        dal.ManufacturerDAO manufacturerDAO = new dal.ManufacturerDAO();

        request.setAttribute("product", product);
        request.setAttribute("productError", message);
        request.setAttribute("listCategories", new CategoryDAO().getCategoriesByStore(storeId));
        request.setAttribute("listColors", colorDAO.getAll());
        request.setAttribute("listManufacturers", manufacturerDAO.getAll());
        request.getRequestDispatcher("Edit.jsp").forward(request, response);
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
}
