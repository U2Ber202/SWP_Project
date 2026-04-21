package controller;

import dal.AcountDAO;
import dal.CategoryDAO;
import dal.ProductDAO;
import dal.StockImportDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Category;
import model.Product;
import model.Store;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "ManagerController", urlPatterns = {"/manager"})
public class ManagerController extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");

        if (!(RoleHelper.isOwner(account) || RoleHelper.isWarehouseManager(account) || RoleHelper.isAdmin(account))) {
            response.sendRedirect("home");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        Store store = null;
        if (RoleHelper.isOwner(account)) {
            store = storeDAO.getStoreByOwnerId(account.getUid());
        } else {
            Integer storeId = ValidationUtil.parsePositiveInt(request.getParameter("storeId"));
            if (storeId == null) {
                storeId = ValidationUtil.parsePositiveInt((String) session.getAttribute("warehouseStoreId"));
            }
            if (storeId != null) {
                store = storeDAO.getStoreById(storeId);
                session.setAttribute("warehouseStoreId", String.valueOf(storeId));
            }
        }

        AcountDAO accountDAO = new AcountDAO();
        String shipperMessage = null;
        String shipperError = null;
        String warehouseMessage = null;
        String warehouseError = null;
        String action = request.getParameter("action");

        if (RoleHelper.isOwner(account) && "createWarehouseManager".equals(action)) {
            String user = ValidationUtil.normalize(request.getParameter("warehouseUser"));
            String pass = ValidationUtil.normalize(request.getParameter("warehousePass"));
            String email = ValidationUtil.normalize(request.getParameter("warehouseEmail")).toLowerCase();
            String fullname = ValidationUtil.normalize(request.getParameter("warehouseFullname"));
            String phone = ValidationUtil.normalize(request.getParameter("warehousePhone"));

            request.setAttribute("warehouseFormUser", user);
            request.setAttribute("warehouseFormEmail", email);
            request.setAttribute("warehouseFormFullname", fullname);
            request.setAttribute("warehouseFormPhone", phone);

            if (store == null) {
                warehouseError = "Bạn cần có cửa hàng trước khi tạo quản lý kho.";
            } else if (ValidationUtil.isBlank(user) || ValidationUtil.isBlank(pass) || ValidationUtil.isBlank(email)
                    || ValidationUtil.isBlank(fullname) || ValidationUtil.isBlank(phone)) {
                warehouseError = "Tên đăng nhập, họ tên, số điện thoại, email và mật khẩu quản lý kho không được để trống.";
            } else if (!ValidationUtil.isValidPhone(phone)) {
                warehouseError = "Số điện thoại quản lý kho không hợp lệ.";
            } else if (!ValidationUtil.isValidEmail(email)) {
                warehouseError = "Email quản lý kho không hợp lệ.";
            } else if (!ValidationUtil.isStrongPassword(pass)) {
                warehouseError = "Mật khẩu quản lý kho phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.";
            } else if (accountDAO.checkAccountExist(user) != null) {
                warehouseError = "Tên đăng nhập quản lý kho đã tồn tại.";
            } else if (accountDAO.getAccountByEmail(email) != null) {
                warehouseError = "Email quản lý kho đã được sử dụng.";
            } else {
                Account warehouseAccount = accountDAO.createWarehouseManagerAccount(user, pass, email, fullname, phone);
                if (warehouseAccount == null) {
                    warehouseError = "Không thể tạo tài khoản quản lý kho. Vui lòng thử lại.";
                } else if (storeDAO.updateStore(store.getId(), store.getName(), store.getOwnerId(), warehouseAccount.getUid())) {
                    store = storeDAO.getStoreById(store.getId());
                    warehouseMessage = "Đã tạo và gán tài khoản quản lý kho cho cửa hàng hiện tại.";
                } else {
                    warehouseError = "Đã tạo tài khoản nhưng không gán được vào cửa hàng. Vui lòng kiểm tra lại store.";
                }
            }
        }

        if (RoleHelper.isOwner(account) && "createShipper".equals(action)) {
            String user = ValidationUtil.normalize(request.getParameter("shipperUser"));
            String pass = ValidationUtil.normalize(request.getParameter("shipperPass"));
            String email = ValidationUtil.normalize(request.getParameter("shipperEmail")).toLowerCase();
            String fullname = ValidationUtil.normalize(request.getParameter("shipperFullname"));
            String phone = ValidationUtil.normalize(request.getParameter("shipperPhone"));

            request.setAttribute("shipperFormUser", user);
            request.setAttribute("shipperFormEmail", email);
            request.setAttribute("shipperFormFullname", fullname);
            request.setAttribute("shipperFormPhone", phone);

            if (store == null) {
                shipperError = "Bạn cần có cửa hàng trước khi tạo shipper.";
            } else if (ValidationUtil.isBlank(user) || ValidationUtil.isBlank(pass) || ValidationUtil.isBlank(email)
                    || ValidationUtil.isBlank(fullname) || ValidationUtil.isBlank(phone)) {
                shipperError = "Tên đăng nhập, họ tên, số điện thoại, email và mật khẩu shipper không được để trống.";
            } else if (!ValidationUtil.isValidPhone(phone)) {
                shipperError = "Số điện thoại shipper không hợp lệ.";
            } else if (!ValidationUtil.isValidEmail(email)) {
                shipperError = "Email shipper không hợp lệ.";
            } else if (!ValidationUtil.isStrongPassword(pass)) {
                shipperError = "Mật khẩu shipper phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.";
            } else if (accountDAO.checkAccountExist(user) != null) {
                shipperError = "Tên đăng nhập shipper đã tồn tại.";
            } else if (accountDAO.getAccountByEmail(email) != null) {
                shipperError = "Email shipper đã được sử dụng.";
            } else {
                Account shipperAccount = accountDAO.createShipperAccount(user, pass, email, fullname, phone);
                if (shipperAccount == null) {
                    shipperError = "Không thể tạo tài khoản shipper. Vui lòng thử lại.";
                } else if (storeDAO.assignShipperToStore(store.getId(), shipperAccount.getUid())) {
                    store = storeDAO.getStoreById(store.getId());
                    shipperMessage = "Tạo tài khoản shipper thành công và đã gán cho cửa hàng hiện tại.";
                } else {
                    shipperError = "Đã tạo tài khoản shipper nhưng không gán được vào cửa hàng.";
                }
            }
        }

        if (store != null) {
            ProductDAO productDAO = new ProductDAO();
            StockImportDAO stockImportDAO = new StockImportDAO();
            List<Product> allProducts = productDAO.getProductsByStoreId(store.getId());
            List<Category> listCategories = new CategoryDAO().getCategoriesByStore(store.getId());
            List<model.StockImport> allStockImports = stockImportDAO.getStockImportsByStoreId(store.getId());
            List<model.StockImport> allDailyStockImports = stockImportDAO.getDailyStockSummaryByStoreId(store.getId());
            int productPage = parsePage(request.getParameter("productPage"));
            int stockImportPage = parsePage(request.getParameter("stockImportPage"));
            int dailyStockPage = parsePage(request.getParameter("dailyStockPage"));
            int pageSize = PAGE_SIZE;
            List<Product> products = paginateList(allProducts, productPage, pageSize);
            List<model.StockImport> stockImports = paginateList(allStockImports, stockImportPage, pageSize);
            List<model.StockImport> dailyStockImports = paginateList(allDailyStockImports, dailyStockPage, pageSize);
            request.setAttribute("listCategories", listCategories);
            request.setAttribute("products", products);
            String stockProductId = ValidationUtil.normalize(request.getParameter("productId"));
            if (ValidationUtil.isBlank(stockProductId) && !allProducts.isEmpty()) {
                stockProductId = String.valueOf(allProducts.get(0).getId());
            }
            request.setAttribute("allProducts", allProducts);
            request.setAttribute("stockProductId", stockProductId);
            request.setAttribute("stockImports", stockImports);
            request.setAttribute("dailyStockImports", dailyStockImports);
            request.setAttribute("productPage", productPage);
            request.setAttribute("stockImportPage", stockImportPage);
            request.setAttribute("dailyStockPage", dailyStockPage);
            request.setAttribute("productTotalPage", getTotalPage(allProducts.size(), pageSize));
            request.setAttribute("stockImportTotalPage", getTotalPage(allStockImports.size(), pageSize));
            request.setAttribute("dailyStockTotalPage", getTotalPage(allDailyStockImports.size(), pageSize));
            request.setAttribute("storeShippers", accountDAO.getShippersByStoreId(store.getId()));
            request.setAttribute("storeWarehouseManagers", accountDAO.getAccountsByRole(Account.ROLE_WAREHOUSE_MANAGER));
            request.setAttribute("managedStore", store);
            request.setAttribute("sizeQuantitiesMap", productDAO.getSizeQuantitiesByStore(store.getId()));
            if (request.getParameter("stockSuccess") != null) {
                request.setAttribute("stockSuccess", "Nhập kho thành công. Hệ thống đã tự động lưu ngày giờ và người nhập.");
            }
            session.setAttribute("currentStore", store);
        } else {
            request.setAttribute("errorStore", "Bạn chưa chọn kho để quản lý.");
        }

        request.setAttribute("shipperMessage", shipperMessage);
        request.setAttribute("shipperError", shipperError);
        request.setAttribute("warehouseMessage", warehouseMessage);
        request.setAttribute("warehouseError", warehouseError);
        
        // Map specifically to standard keys for toast
        if (shipperMessage != null) request.setAttribute("success", shipperMessage);
        if (shipperError != null) request.setAttribute("error", shipperError);
        if (warehouseMessage != null) request.setAttribute("success", warehouseMessage);
        if (warehouseError != null) request.setAttribute("error", warehouseError);
        if (request.getAttribute("stockSuccess") != null) request.setAttribute("success", request.getAttribute("stockSuccess"));
        
        request.getRequestDispatcher("ManagerProduct.jsp").forward(request, response);
    }

    private int parsePage(String pageParam) {
        try {
            int parsed = Integer.parseInt(pageParam);
            return parsed > 0 ? parsed : 1;
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    private int getTotalPage(int totalItems, int pageSize) {
        return Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
    }

    private <T> List<T> paginateList(List<T> items, int page, int pageSize) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        int totalPage = getTotalPage(items.size(), pageSize);
        int safePage = Math.min(page, totalPage);
        int fromIndex = Math.max(0, (safePage - 1) * pageSize);
        int toIndex = Math.min(items.size(), fromIndex + pageSize);
        return new ArrayList<>(items.subList(fromIndex, toIndex));
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
