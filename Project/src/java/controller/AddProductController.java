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
        String priceStr = ValidationUtil.normalize(request.getParameter("price"));
        String title = ValidationUtil.normalize(request.getParameter("title")); // Sizes
        String manufacturerIdStr = ValidationUtil.normalize(request.getParameter("manufacturerId"));
        String colorIdStr = ValidationUtil.normalize(request.getParameter("colorId"));
        String cidStr = ValidationUtil.normalize(request.getParameter("category"));
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

        if (!isValidProductInput(pName, img, priceStr, title, manufacturerIdStr, colorIdStr, cidStr, des)) {
            forwardWithValidationError(request, response, store.getStoreId(),
                    "Vui lòng nhập đầy đủ thông tin hợp lệ.");
            return;
        }

        int categoryId = Integer.parseInt(cidStr);
        int manufacturerId = Integer.parseInt(manufacturerIdStr);
        int colorId = Integer.parseInt(colorIdStr);
        int price = Integer.parseInt(priceStr);

        ProductDAO productDAO = new ProductDAO();
        model.Product p = new model.Product();
        p.setName(pName);
        p.setDescription(des);
        p.setCategoryId(categoryId);
        p.setStoreId(store.getStoreId());
        p.setManufacturerId(manufacturerId);

        int productId = productDAO.insertProduct(p);
        if (productId > 0) {
            String[] sizes = title.split(",");
            for (String s : sizes) {
                model.ProductVariant v = new model.ProductVariant();
                v.setProductId(productId);
                v.setColorId(colorId);
                v.setSize(s.trim());
                v.setPrice(price);
                v.setQuantity(0); // Initial quantity is 0, updated via Stock Import
                v.setImage(img);
                v.setSku("P" + productId + "C" + colorId + "S" + s.trim());
                productDAO.insertVariant(v);
            }
            session.setAttribute("success", "Thêm sản phẩm và các biến thể thành công!");
        } else {
            session.setAttribute("error", "Lỗi khi thêm sản phẩm.");
        }
        
        response.sendRedirect("manager");
    }

    private boolean isValidProductInput(String pName, String img, String price, String title, String mid, String colid, String cid, String des) {
        return !ValidationUtil.isBlank(pName)
                && !ValidationUtil.isBlank(img)
                && ValidationUtil.isNonNegativeInteger(price)
                && ValidationUtil.isValidSizeList(title)
                && ValidationUtil.isNonNegativeInteger(mid)
                && ValidationUtil.isNonNegativeInteger(colid)
                && ValidationUtil.isNonNegativeInteger(cid)
                && !ValidationUtil.isBlank(des);
    }

    private void forwardWithValidationError(HttpServletRequest request, HttpServletResponse response, int storeId, String message)
            throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        StockImportDAO stockImportDAO = new StockImportDAO();
        dal.ColorDAO colorDAO = new dal.ColorDAO();
        dal.ManufacturerDAO manufacturerDAO = new dal.ManufacturerDAO();

        request.setAttribute("products", productDAO.getProductsByStoreId(storeId));
        request.setAttribute("listCategories", categoryDAO.getCategoriesByStore(storeId));
        request.setAttribute("listColors", colorDAO.getAll());
        request.setAttribute("listManufacturers", manufacturerDAO.getAll());
        request.setAttribute("stockImports", stockImportDAO.getStockImportsByStoreId(storeId));
        request.setAttribute("dailyStockImports", stockImportDAO.getDailyStockSummaryByStoreId(storeId));
        request.setAttribute("productError", message);
        request.setAttribute("formName", ValidationUtil.normalize(request.getParameter("name")));
        request.setAttribute("formImage", ValidationUtil.normalize(request.getParameter("image")));
        request.setAttribute("formPrice", ValidationUtil.normalize(request.getParameter("price")));
        request.setAttribute("formTitle", ValidationUtil.normalize(request.getParameter("title")));
        request.setAttribute("formManufacturerId", ValidationUtil.normalize(request.getParameter("manufacturerId")));
        request.setAttribute("formColorId", ValidationUtil.normalize(request.getParameter("colorId")));
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
