package controller;

import dal.AcountDAO;
import dal.HomeSettingDAO;
import dal.ProductDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.HomeSetting;
import model.Store;
import util.RoleHelper;
import util.SendMail;
import util.ValidationUtil;

@WebServlet(name = "ManageStoreController", urlPatterns = {"/manageStore"})
public class ManageStoreController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        jakarta.servlet.http.HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        if (!RoleHelper.isAdmin(account)) {
            response.sendRedirect("home");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        AcountDAO accountDAO = new AcountDAO();
        HomeSettingDAO homeSettingDAO = new HomeSettingDAO();
        String message = null;
        String error = null;

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String storeName = ValidationUtil.normalize(request.getParameter("storeName"));
            Integer ownerId = ValidationUtil.parsePositiveInt(request.getParameter("ownerId"));
            Integer warehouseManagerId = ValidationUtil.parsePositiveInt(request.getParameter("warehouseManagerId"));

            if (ValidationUtil.isBlank(storeName) || ownerId == null) {
                error = "Tên cửa hàng và chủ sở hữu không được để trống.";
            } else if (storeName.length() < 5 || storeName.length() > 40) {
                error = "Tên cửa hàng phải từ 5 đến 40 ký tự.";
            } else if (storeDAO.isStoreNameExist(storeName)) {
                error = "Tên cửa hàng bị trùng lặp. Vui lòng nhập tên khác.";
            } else {
                Account owner = accountDAO.getAccountById(ownerId);
                Account warehouseManager = warehouseManagerId == null ? null : accountDAO.getAccountById(warehouseManagerId);
                if (owner == null || !RoleHelper.isOwner(owner)) {
                    error = "Chủ sở hữu phải là tài khoàn Owner hợp lệ.";
                } else if (warehouseManagerId != null && (warehouseManager == null || !RoleHelper.isWarehouseManager(warehouseManager))) {
                    error = "Quản lý kho phải là tài khoản Warehouse Manager hợp lệ.";
                } else if (storeDAO.ownerAlreadyHasStore(ownerId)) {
                    error = "Mỗi chủ sở hữu chỉ được tạo 1 cửa hàng.";
                } else if (warehouseManagerId != null && storeDAO.warehouseManagerAlreadyHasStore(warehouseManagerId)) {
                    error = "Mỗi quản lý kho chỉ được gán cho 1 cửa hàng.";
                } else if (storeDAO.insertStore(storeName, ownerId, warehouseManagerId)) {
                    message = "Tạo cửa hàng thành công.";
                    session.setAttribute("success", message);
                } else {
                    error = "Không thể tạo cửa hàng. Vui lòng thử lại.";
                }
            }
        } else if ("toggleStatus".equals(action)) {
            Integer storeId = ValidationUtil.parsePositiveInt(request.getParameter("storeId"));
            if (storeId == null) {
                error = "Cửa hàng không hợp lệ.";
            } else {
                Store store = storeDAO.getStoreById(storeId);
                if (store != null) {
                    boolean newStatus = !store.isActive();
                    if (storeDAO.toggleStoreStatus(storeId)) {
                        message = "Cập nhật trạng thái cửa hàng thành công.";
                        session.setAttribute("success", message);

                        // Notify Owner
                        Account owner = accountDAO.getAccountById(store.getOwnerId());
                        if (owner != null && owner.getEmail() != null) {
                            String subject = "[V-SNKR] Thông báo trạng thái cửa hàng " + store.getName();
                            String content = "Chào " + owner.getFullname() + ",\n\n"
                                    + "Cửa hàng '" + store.getName() + "' của bạn đã được chuyển sang trạng thái: "
                                    + (newStatus ? "ĐANG HOẠT ĐỘNG (Active)" : "NGỪNG HOẠT ĐỘNG (Inactive)") + ".\n\n"
                                    + "Vui lòng liên hệ Admin nếu có bất kỳ thắc mắc nào.";
                            SendMail.sendEmailWithContent(owner.getEmail(), subject, content);
                        }

                        // Notify Warehouse Manager
                        if (store.getWarehouseManagerId() > 0) {
                            Account wm = accountDAO.getAccountById(store.getWarehouseManagerId());
                            if (wm != null && wm.getEmail() != null) {
                                String subject = "[V-SNKR] Thông báo trạng thái cửa hàng " + store.getName();
                                String content = "Chào " + wm.getFullname() + ",\n\n"
                                        + "Cửa hàng '" + store.getName() + "' mà bạn đang quản lý đã được chuyển sang trạng thái: "
                                        + (newStatus ? "ĐANG HOẠT ĐỘNG (Active)" : "NGỪNG HOẠT ĐỘNG (Inactive)") + ".\n\n"
                                        + "Vui lòng liên hệ Admin hoặc Chủ cửa hàng để biết thêm chi tiết.";
                                SendMail.sendEmailWithContent(wm.getEmail(), subject, content);
                            }
                        }
                    } else {
                        error = "Không thể cập nhật trạng thái cửa hàng.";
                    }
                } else {
                    error = "Cửa hàng không tồn tại.";
                }
            }
        } else if ("update".equals(action)) {
            Integer storeId = ValidationUtil.parsePositiveInt(request.getParameter("storeId"));
            String storeName = ValidationUtil.normalize(request.getParameter("storeName"));
            Integer ownerId = ValidationUtil.parsePositiveInt(request.getParameter("ownerId"));
            Integer warehouseManagerId = ValidationUtil.parsePositiveInt(request.getParameter("warehouseManagerId"));

            if (storeId == null || ValidationUtil.isBlank(storeName) || ownerId == null) {
                error = "Thông tin cửa hàng không hợp lệ.";
            } else if (storeName.length() < 5 || storeName.length() > 40) {
                error = "Tên cửa hàng phải từ 5 đến 40 ký tự.";
            } else if (storeDAO.isStoreNameExist(storeName)) {                         // ← THÊM VÀO ĐÂY
                error = "Tên cửa hàng bị trùng lặp. Vui lòng nhập tên khác.";
            } else {
                Store store = storeDAO.getStoreById(storeId);
                Account owner = accountDAO.getAccountById(ownerId);
                Account warehouseManager = warehouseManagerId == null ? null : accountDAO.getAccountById(warehouseManagerId);
                if (store == null) {
                    error = "Cửa hàng không tồn tại.";
                } else if (owner == null || !RoleHelper.isOwner(owner)) {
                    error = "Chủ sở hữu phải là tài khoản Owner hợp lệ.";
                } else if (warehouseManagerId != null && (warehouseManager == null || !RoleHelper.isWarehouseManager(warehouseManager))) {
                    error = "Quản lý kho phải là tài khoản Warehouse Manager hợp lệ.";
                } else if (storeDAO.ownerAlreadyHasAnotherStore(ownerId, storeId)) {
                    error = "Mỗi chủ sở hữu chỉ được sở hữu 1 cửa hàng.";
                } else if (warehouseManagerId != null && storeDAO.warehouseManagerAlreadyHasAnotherStore(warehouseManagerId, storeId)) {
                    error = "Mỗi quản lý kho chỉ được gán cho 1 cửa hàng.";
                } else if (storeDAO.updateStore(storeId, storeName, ownerId, warehouseManagerId)) {
                    message = "Cập nhật cửa hàng thành công.";
                    session.setAttribute("success", message);
                } else {
                    error = "Không thể cập nhật cửa hàng. Vui lòng thử lại.";
                }
            }
        }

        List<Store> allStores = storeDAO.getAllStores();
        final int PAGE_SIZE = 10;
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) {
                page = Integer.parseInt(p);
            }
        } catch (Exception e) {
            page = 1;
        }

        int totalStores = allStores.size();
        int totalPage = (int) Math.ceil((double) totalStores / PAGE_SIZE);
        if (page > totalPage && totalPage > 0) {
            page = totalPage;
        }
        if (page < 1) {
            page = 1;
        }

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalStores);
        List<Store> listStores = (fromIndex < totalStores) ? allStores.subList(fromIndex, toIndex) : new java.util.ArrayList<>();

        List<Account> listAccounts = accountDAO.getAccountsByRole(Account.ROLE_OWNER);
        List<Account> listWarehouseManagers = accountDAO.getWarehouseManagersAvailableForStore(null);
        Map<Integer, List<Account>> warehouseManagersByStore = new LinkedHashMap<>();
        for (Store store : listStores) {
            warehouseManagersByStore.put(store.getId(), accountDAO.getWarehouseManagersAvailableForStore(store.getId()));
        }
        request.setAttribute("listStores", listStores);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("listAccounts", listAccounts);
        request.setAttribute("listWarehouseManagers", listWarehouseManagers);
        request.setAttribute("warehouseManagersByStore", warehouseManagersByStore);
        request.setAttribute("message", message);
        request.setAttribute("error", error);
        request.getRequestDispatcher("ManageStore.jsp").forward(request, response);
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
        return "ManageStoreController";
    }
}
