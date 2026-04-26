package controller;

import dal.CategoryDAO;
import dal.ProductDAO;
import dal.StockImportDAO;
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

@WebServlet(name = "AddProductController", urlPatterns = {"/add"})
public class AddProductController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

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

        if (!isValidProductInput(pName, img, price, title, manufacturer, cid, des)) {
            forwardWithValidationError(request, response, store.getId(),
                    "Vui long nhap day du thong tin hop le.");
            return;
        }

        int categoryId = Integer.parseInt(cid);
        CategoryDAO categoryDAO = new CategoryDAO();
        if (categoryDAO.getCategoryByIdAndStore(categoryId, store.getId()) == null) {
            response.sendRedirect("manager");
            return;
        }

        int productPrice = Integer.parseInt(price);
        ProductDAO productDAO = new ProductDAO();
        
        // Check for duplicate name
        if (productDAO.isProductNameExist(pName, store.getId(), 0)) {
            forwardWithValidationError(request, response, store.getId(),
                    "Tên sản phẩm này đã tồn tại trong cửa hàng của bạn. Vui lòng chọn tên khác.");
            return;
        }
        
        // Filter bad words
        pName = ValidationUtil.filterBadWords(pName);
        des = ValidationUtil.filterBadWords(des);
        
        productDAO.insertProduct(pName, img, productPrice, title, des, manufacturer, categoryId, 0, account.getUid(), store.getId());
        session.setAttribute("success", "Thêm sản phẩm mới thành công!");
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

    private void forwardWithValidationError(HttpServletRequest request, HttpServletResponse response, int storeId, String message)
            throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        StockImportDAO stockImportDAO = new StockImportDAO();
        request.setAttribute("products", productDAO.getProductsByStoreId(storeId));
        request.setAttribute("listCategories", categoryDAO.getCategoriesByStore(storeId));
        request.setAttribute("stockImports", stockImportDAO.getStockImportsByStoreId(storeId));
        request.setAttribute("dailyStockImports", stockImportDAO.getDailyStockSummaryByStoreId(storeId));
        request.setAttribute("productError", message);
        request.setAttribute("formName", ValidationUtil.normalize(request.getParameter("name")));
        request.setAttribute("formImage", ValidationUtil.normalize(request.getParameter("image")));
        request.setAttribute("formPrice", ValidationUtil.normalize(request.getParameter("price")));
        request.setAttribute("formTitle", ValidationUtil.normalize(request.getParameter("title")));
        request.setAttribute("formManufacturer", ValidationUtil.normalize(request.getParameter("manufacturer")));
        request.setAttribute("formCategory", ValidationUtil.normalize(request.getParameter("category")));
        request.setAttribute("formDescription", ValidationUtil.normalize(request.getParameter("description")));
        request.getRequestDispatcher("ManagerProduct.jsp").forward(request, response);
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
