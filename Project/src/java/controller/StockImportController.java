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
        Integer colorId = ValidationUtil.parsePositiveInt(request.getParameter("colorId"));
        String note = ValidationUtil.normalize(request.getParameter("note"));
        Integer unitCost = ValidationUtil.parsePositiveInt(request.getParameter("unitCost"));
        String batchNumber = ValidationUtil.normalize(request.getParameter("batchNumber"));

        ProductDAO productDAO = new ProductDAO();
        Product product = productId == null ? null : productDAO.getProductById(productId);
        
        if (product == null || colorId == null || unitCost == null || ValidationUtil.isBlank(batchNumber)) {
            forwardWithError(request, response, store.getStoreId(), productId, new LinkedHashMap<>(),
                    "Thông tin nhập kho không hợp lệ. Vui lòng nhập đầy đủ giá nhập và số lô.");
            return;
        }

        List<String> sizeLines = new ArrayList<>();
        Map<String, String> submittedSizeValues = new LinkedHashMap<>();
        Map<String, Integer> sizeQuantities = new LinkedHashMap<>();
        int totalQuantity = 0;
        
        // Get all sizes available for this product from its title (which aggregates all variant sizes)
        String sizesAttr = product.getTiltle() != null ? product.getTiltle() : "";
        String[] sizes = sizesAttr.split(",");
        
        for (String s : sizes) {
            String size = s.trim();
            if (size.isEmpty()) continue;
            
            String sizeParam = ValidationUtil.normalize(request.getParameter("size_" + size));
            submittedSizeValues.put(size, sizeParam);
            Integer qty = ValidationUtil.parsePositiveInt(sizeParam);
            
            if (qty != null && qty > 0) {
                totalQuantity += qty;
                sizeQuantities.put(size, qty);
                sizeLines.add("S" + size + ":" + qty);
            }
        }

        if (totalQuantity <= 0) {
            forwardWithError(request, response, store.getStoreId(), productId, submittedSizeValues,
                    "Bạn cần nhập ít nhất một size với số lượng > 0 cho màu đã chọn.");
            return;
        }

        String combinedNote = String.join(", ", sizeLines);
        if (!ValidationUtil.isBlank(note)) {
            combinedNote = combinedNote + " | " + note;
        }

        boolean success = new StockImportDAO().addStockImport(productId, colorId, store.getStoreId(), totalQuantity, unitCost, batchNumber, combinedNote, account.getUid(), sizeQuantities);
        if (!success) {
            forwardWithError(request, response, store.getStoreId(), productId, submittedSizeValues,
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
        dal.ColorDAO colorDAO = new dal.ColorDAO();
        dal.ManufacturerDAO manufacturerDAO = new dal.ManufacturerDAO();
        Store store = new StoreDAO().getStoreById(storeId);
        List<Product> products = productDAO.getProductsByStoreId(storeId);

        request.setAttribute("products", products);
        request.setAttribute("allProducts", products);
        request.setAttribute("listCategories", new CategoryDAO().getCategoriesByStore(storeId));
        request.setAttribute("listColors", colorDAO.getAll());
        request.setAttribute("listManufacturers", manufacturerDAO.getAll());
        request.setAttribute("stockImports", stockImportDAO.getStockImportsByStoreId(storeId));
        request.setAttribute("dailyStockImports", stockImportDAO.getDailyStockSummaryByStoreId(storeId));
        request.setAttribute("stockError", message);
        request.setAttribute("error", message);

        String stockProductId = productId == null ? "" : String.valueOf(productId);
        if (ValidationUtil.isBlank(stockProductId) && !products.isEmpty()) {
            stockProductId = String.valueOf(products.get(0).getId());
        }

        request.setAttribute("stockProductId", stockProductId);
        request.setAttribute("stockNote", ValidationUtil.normalize(request.getParameter("note")));
        request.setAttribute("stockSizeValues", submittedSizeValues == null ? new LinkedHashMap<>() : submittedSizeValues);
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
