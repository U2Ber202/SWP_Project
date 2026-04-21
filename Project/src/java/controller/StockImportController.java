package controller;

import dal.CategoryDAO;
import dal.ProductDAO;
import dal.StockImportDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Product;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "StockImportController", urlPatterns = {"/stock-import"})
public class StockImportController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.canManageInventory(account)) {
            response.sendRedirect("home");
            return;
        }

        Integer storeId = ValidationUtil.parsePositiveInt(request.getParameter("storeId"));
        if (storeId == null) {
            response.sendRedirect("manager");
            return;
        }

        Store store = new StoreDAO().getStoreById(storeId);
        if (store == null) {
            response.sendRedirect("manager");
            return;
        }

        Integer productId = ValidationUtil.parsePositiveInt(request.getParameter("productId"));
        String note = ValidationUtil.normalize(request.getParameter("note"));

        ProductDAO productDAO = new ProductDAO();
        Product product = productId == null ? null : productDAO.getProductByIdAndStoreId(productId, store.getId());
        if (product == null) {
            forwardWithError(request, response, store.getId(), productId, new LinkedHashMap<>(),
                    "Thông tin nhập kho không hợp lệ. Vui lòng chọn đúng sản phẩm của kho.");
            return;
        }

        List<String> sizeLines = new ArrayList<>();
        Map<String, String> submittedSizeValues = new LinkedHashMap<>();
        Map<String, Integer> sizeQuantities = new LinkedHashMap<>();
        int totalQuantity = 0;
        for (String size : parseProductSizes(product.getTiltle())) {
            String sizeParam = ValidationUtil.normalize(request.getParameter("size_" + size));
            submittedSizeValues.put(size, sizeParam);
            Integer qty = ValidationUtil.parsePositiveInt(sizeParam);
            if (qty != null) {
                totalQuantity += qty;
                sizeQuantities.put(size, qty);
                sizeLines.add("Size " + size + ": " + qty + " đôi");
            }
        }

        if (totalQuantity <= 0) {
            forwardWithError(request, response, store.getId(), productId, submittedSizeValues,
                    "Bạn cần nhập ít nhất một size với số lượng > 0.");
            return;
        }

        String combinedNote = String.join(", ", sizeLines);
        if (!ValidationUtil.isBlank(note)) {
            combinedNote = combinedNote + " | Ghi chu: " + note;
        }

        boolean success = new StockImportDAO().addStockImport(productId, store.getId(), totalQuantity, combinedNote, account.getUid(), sizeQuantities);
        if (!success) {
            forwardWithError(request, response, store.getId(), productId, submittedSizeValues,
                    "Không thể cập nhật kho lúc này. Vui lòng thử lại.");
            return;
        }

        response.sendRedirect("manager?stockSuccess=1");
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, int storeId,
            Integer productId, Map<String, String> submittedSizeValues, String message)
            throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        StockImportDAO stockImportDAO = new StockImportDAO();
        Store store = new StoreDAO().getStoreById(storeId);
        List<Product> products = productDAO.getProductsByStoreId(storeId);

        request.setAttribute("products", products);
        request.setAttribute("allProducts", products);
        request.setAttribute("listCategories", new CategoryDAO().getCategoriesByStore(storeId));
        request.setAttribute("stockImports", stockImportDAO.getStockImportsByStoreId(storeId));
        request.setAttribute("dailyStockImports", stockImportDAO.getDailyStockSummaryByStoreId(storeId));
        request.setAttribute("stockError", message);

        String stockProductId = productId == null ? "" : String.valueOf(productId);
        if (ValidationUtil.isBlank(stockProductId) && !products.isEmpty()) {
            stockProductId = String.valueOf(products.get(0).getId());
        }

        request.setAttribute("stockProductId", stockProductId);
        request.setAttribute("stockNote", ValidationUtil.normalize(request.getParameter("note")));
        request.setAttribute("stockSizeValues", submittedSizeValues == null ? new LinkedHashMap<>() : submittedSizeValues);
        request.setAttribute("productPage", 1);
        request.setAttribute("stockImportPage", 1);
        request.setAttribute("dailyStockPage", 1);
        request.setAttribute("productTotalPage", 1);
        request.setAttribute("stockImportTotalPage", 1);
        request.setAttribute("dailyStockTotalPage", 1);
        request.setAttribute("storeShippers", new ArrayList<>());
        request.setAttribute("storeWarehouseManagers", new ArrayList<>());
        request.setAttribute("managedStore", store);
        request.getRequestDispatcher("ManagerProduct.jsp").forward(request, response);
    }

    private List<String> parseProductSizes(String sizeValue) {
        String normalizedSizeValue = ValidationUtil.normalize(sizeValue);
        if (normalizedSizeValue.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> sizes = new ArrayList<>();
        for (String size : Arrays.asList(normalizedSizeValue.split(","))) {
            String normalizedSize = ValidationUtil.normalize(size);
            if (!normalizedSize.isEmpty()) {
                sizes.add(normalizedSize);
            }
        }
        return sizes;
    }
}
