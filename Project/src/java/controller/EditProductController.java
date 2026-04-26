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
        String img = ValidationUtil.normalize(request.getParameter("image"));
        String price = ValidationUtil.normalize(request.getParameter("price"));
        String title = ValidationUtil.normalize(request.getParameter("title"));
        String manufacturer = ValidationUtil.normalize(request.getParameter("manufacturer"));
        String cid = ValidationUtil.normalize(request.getParameter("category"));
        String des = ValidationUtil.normalize(request.getParameter("description"));

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        if (!RoleHelper.isOwner(account)) {
            response.sendRedirect("home");
            return;
        }

        Store store = new StoreDAO().getStoreByOwnerId(account.getUid());
        if (store == null) {
            response.sendRedirect("manager");
            return;
        }

        if (!ValidationUtil.isNonNegativeInteger(id)) {
            response.sendRedirect("manager");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        Product existingProduct = productDAO.getProductByIdAndStoreId(Integer.parseInt(id), store.getId());
        if (existingProduct == null) {
            response.sendRedirect("manager");
            return;
        }

        if (!isValidProductInput(pName, img, price, title, manufacturer, cid, des)) {
            forwardWithValidationError(request, response, store.getId(),
                    "Vui lòng nhập đầy đủ thông tin hợp lệ.",
                    existingProduct.getQuantity());
            return;
        }

        int productId = Integer.parseInt(id);
        int productPrice = Integer.parseInt(price);
        int categoryId = Integer.parseInt(cid);

        // Check for duplicate name
        if (productDAO.isProductNameExist(pName, store.getId(), productId)) {
            forwardWithValidationError(request, response, store.getId(),
                    "Tên sản phẩm này đã được sử dụng bởi một sản phẩm khác. Vui lòng chọn tên khác.",
                    existingProduct.getQuantity());
            return;
        }

        // Filter bad words
        pName = ValidationUtil.filterBadWords(pName);
        des = ValidationUtil.filterBadWords(des);

        productDAO.updateProductByStore(pName, img, productPrice, title, des, manufacturer, categoryId, existingProduct.getQuantity(), productId, store.getId());
        session.setAttribute("success", "Cập nhật sản phẩm thành công!");
        response.sendRedirect("manager");
    }

    private boolean isValidProductInput(String pName, String img, String price, String title, String manufacturer, String cid, String des) {
        return ValidationUtil.isValidLength(pName, 1, 100)
                && !ValidationUtil.isBlank(img)
                && ValidationUtil.isNonNegativeInteger(price)
                && ValidationUtil.isValidSizeList(title)
                && ValidationUtil.isValidLength(manufacturer, 1, 50)
                && ValidationUtil.isNonNegativeInteger(cid)
                && ValidationUtil.isValidLength(des, 1, 1000);
    }

    private void forwardWithValidationError(HttpServletRequest request, HttpServletResponse response, int storeId, String message, int quantity)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        int id = ValidationUtil.isNonNegativeInteger(idParam) ? Integer.parseInt(idParam) : 0;
        
        Product product = new Product();
        product.setId(id);
        product.setName(ValidationUtil.normalize(request.getParameter("name")));
        product.setImageUrl(ValidationUtil.normalize(request.getParameter("image")));
        product.setPrice(ValidationUtil.isNonNegativeInteger(request.getParameter("price"))
                ? Integer.parseInt(ValidationUtil.normalize(request.getParameter("price"))) : 0);
        product.setQuantity(quantity);
        product.setTiltle(ValidationUtil.normalize(request.getParameter("title")));
        product.setManufacturer(ValidationUtil.normalize(request.getParameter("manufacturer")));
        product.setDescription(ValidationUtil.normalize(request.getParameter("description")));
        product.setCategoryId(ValidationUtil.isNonNegativeInteger(request.getParameter("category"))
                ? Integer.parseInt(ValidationUtil.normalize(request.getParameter("category"))) : 0);

        request.setAttribute("product", product);
        request.setAttribute("productError", message);
        request.setAttribute("listCategories", new CategoryDAO().getCategoriesByStore(storeId));
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
