package controller;

import dal.AcountDAO;
import dal.StaffActionHistoryDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.StaffActionHistory;
import model.Store;
import util.RoleHelper;

@WebServlet(name = "ManagerStaffController", urlPatterns = {"/managerStaff"})
public class ManagerStaffController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account ownerAcc = (Account) request.getSession().getAttribute("acc");
        if (ownerAcc == null || !RoleHelper.isOwner(ownerAcc)) {
            response.sendRedirect("login.jsp");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        Store store = storeDAO.getStoreByOwnerId(ownerAcc.getUid());
        if (store == null) {
            response.sendRedirect("home");
            return;
        }

        AcountDAO accountDAO = new AcountDAO();
        List<Account> staffList = accountDAO.getStaffByOwner(ownerAcc.getUid());

        StaffActionHistoryDAO historyDAO = new StaffActionHistoryDAO();
        List<StaffActionHistory> historyList = historyDAO.getHistoryByOwner(ownerAcc.getUid());

        request.setAttribute("staffList", staffList);
        request.setAttribute("historyList", historyList);
        request.setAttribute("store", store);
        request.getRequestDispatcher("ManagerStaff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account ownerAcc = (Account) request.getSession().getAttribute("acc");
        if (ownerAcc == null || !RoleHelper.isOwner(ownerAcc)) {
            response.sendRedirect("login.jsp");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        Store store = storeDAO.getStoreByOwnerId(ownerAcc.getUid());
        if (store == null) {
            response.sendRedirect("home");
            return;
        }

        AcountDAO accountDAO = new AcountDAO();
        StaffActionHistoryDAO historyDAO = new StaffActionHistoryDAO();
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String user = request.getParameter("user");
            String pass = request.getParameter("pass");
            String email = request.getParameter("email");
            String fullname = request.getParameter("fullname");
            String phone = request.getParameter("phone");
            String role = request.getParameter("role");

            // Validation
            if (accountDAO.checkAccountExist(user) != null) {
                request.getSession().setAttribute("error", "Tên đăng nhập đã tồn tại!");
                response.sendRedirect("managerStaff");
                return;
            }
            if (!email.isEmpty() && accountDAO.getAccountByEmail(email) != null) {
                request.getSession().setAttribute("error", "Email đã được sử dụng!");
                response.sendRedirect("managerStaff");
                return;
            }
            if (pass.length() < 6) {
                request.getSession().setAttribute("error", "Mật khẩu phải từ 6 ký tự trở lên!");
                response.sendRedirect("managerStaff");
                return;
            }

            int staffId = accountDAO.insertStaffAndReturnId(user, pass, email, fullname, phone, role);
            if (staffId > 0) {
                // Assign to store and preserve old staff in history
                if ("warehouse_manager".equals(role)) {
                    int oldStaffId = store.getWarehouseManagerId();
                    if (oldStaffId > 0 && !historyDAO.hasHistory(ownerAcc.getUid(), oldStaffId)) {
                        StaffActionHistory oldH = new StaffActionHistory();
                        oldH.setOwnerId(ownerAcc.getUid());
                        oldH.setStaffId(oldStaffId);
                        oldH.setActionType("UPDATE");
                        oldH.setDetails("Lưu vết nhân viên cũ (Quản lý kho) trước khi thay thế");
                        historyDAO.insert(oldH);
                    }
                    storeDAO.assignWarehouseManagerToStore(store.getId(), staffId);
                } else if ("shipper".equals(role)) {
                    int oldStaffId = store.getShipperId();
                    if (oldStaffId > 0 && !historyDAO.hasHistory(ownerAcc.getUid(), oldStaffId)) {
                        StaffActionHistory oldH = new StaffActionHistory();
                        oldH.setOwnerId(ownerAcc.getUid());
                        oldH.setStaffId(oldStaffId);
                        oldH.setActionType("UPDATE");
                        oldH.setDetails("Lưu vết nhân viên cũ (Shipper) trước khi thay thế");
                        historyDAO.insert(oldH);
                    }
                    storeDAO.assignShipperToStore(store.getId(), staffId);
                }

                // Log history
                StaffActionHistory h = new StaffActionHistory();
                h.setOwnerId(ownerAcc.getUid());
                h.setStaffId(staffId);
                h.setActionType("ADD");
                h.setDetails("Tạo tài khoản " + (role.equals("shipper") ? "Shipper" : "Quản lý kho") + ": " + user);
                historyDAO.insert(h);
                request.getSession().setAttribute("success", "Tạo tài khoản nhân viên thành công!");
            }
        } else if ("edit".equals(action)) {
            int staffId = Integer.parseInt(request.getParameter("id"));
            String fullname = request.getParameter("fullname");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            boolean active = request.getParameter("active") != null;

            // Simple validation for edit
            Account existing = accountDAO.getAccountByEmail(email);
            if (existing != null && existing.getUid() != staffId) {
                request.getSession().setAttribute("error", "Email này đã thuộc về tài khoản khác!");
                response.sendRedirect("managerStaff");
                return;
            }

            accountDAO.updateStaff(staffId, fullname, phone, email, active);
            request.getSession().setAttribute("success", "Cập nhật nhân viên thành công!");

            // Log history
            StaffActionHistory h = new StaffActionHistory();
            h.setOwnerId(ownerAcc.getUid());
            h.setStaffId(staffId);
            h.setActionType("UPDATE");
            h.setDetails("Cập nhật: " + fullname + " (SĐT: " + phone + ", Active: " + active + ")");
            historyDAO.insert(h);
        }

        response.sendRedirect("managerStaff");
    }
}
