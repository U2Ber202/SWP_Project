package controller;

import dal.StoreDAO;
import dal.VoucherDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Store;
import model.Voucher;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "VoucherController", urlPatterns = {"/vouchers"})
public class VoucherController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc == null || (!RoleHelper.isOwner(acc) && !RoleHelper.isAdmin(acc))) {
            response.sendRedirect("home");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        VoucherDAO voucherDAO = new VoucherDAO();

        List<Voucher> allVouchers;
        if (RoleHelper.isAdmin(acc)) {
            allVouchers = voucherDAO.getAllVouchers();
            request.setAttribute("allStores", storeDAO.getAllStores());
            request.setAttribute("voucherScope", "admin");
        } else {
            Store store = storeDAO.getStoreByOwnerId(acc.getUid());
            if (store != null) {
                allVouchers = voucherDAO.getVouchersByStoreId(store.getId());
                request.setAttribute("store", store);
            } else {
                allVouchers = new java.util.ArrayList<>();
            }
            request.setAttribute("voucherScope", "owner");
        }

        final int PAGE_SIZE = 10;
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (Exception e) { page = 1; }

        int totalVouchers = allVouchers.size();
        int totalPage = (int) Math.ceil((double) totalVouchers / PAGE_SIZE);
        if (page > totalPage && totalPage > 0) page = totalPage;
        if (page < 1) page = 1;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalVouchers);
        List<Voucher> listVouchers = (fromIndex < totalVouchers) ? allVouchers.subList(fromIndex, toIndex) : new java.util.ArrayList<>();

        request.setAttribute("listVouchers", listVouchers);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);

        request.getRequestDispatcher("VoucherManager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc == null || (!RoleHelper.isOwner(acc) && !RoleHelper.isAdmin(acc))) {
            response.sendRedirect("home");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        VoucherDAO voucherDAO = new VoucherDAO();
        boolean isAdmin = RoleHelper.isAdmin(acc);
        String action = ValidationUtil.normalize(request.getParameter("action"));

        Integer targetStoreId = resolveTargetStoreId(request, acc, storeDAO);
        if (targetStoreId == null) {
            session.setAttribute("error", "Không xác định được cửa hàng để thao tác voucher.");
            response.sendRedirect("vouchers");
            return;
        }

        try {
            if ("add".equals(action)) {
                Voucher voucher = buildVoucherFromRequest(request, targetStoreId);
                if (voucherDAO.addVoucher(voucher)) {
                    session.setAttribute("success", "Tạo voucher thành công.");
                } else {
                    session.setAttribute("error", "Không thể tạo voucher. Vui lòng thử lại.");
                }
            } else if ("update".equals(action)) {
                Integer id = ValidationUtil.parsePositiveInt(request.getParameter("id"));
                if (id == null) {
                    session.setAttribute("error", "ID voucher không hợp lệ.");
                } else {
                    Voucher existingVoucher = voucherDAO.getVoucherById(id);
                    if (existingVoucher == null) {
                        session.setAttribute("error", "Voucher không tồn tại.");
                    } else if (!isAdmin && existingVoucher.getStoreId() != targetStoreId) {
                        session.setAttribute("error", "Bạn không có quyền cập nhật voucher này.");
                    } else {
                        Voucher voucher = buildVoucherFromRequest(request, targetStoreId);
                        voucher.setId(id);
                        if (voucherDAO.updateVoucher(voucher)) {
                            session.setAttribute("success", "Cập nhật voucher thành công.");
                        } else {
                            session.setAttribute("error", "Không thể cập nhật voucher này.");
                        }
                    }
                }
            } else if ("delete".equals(action)) {
                Integer id = ValidationUtil.parsePositiveInt(request.getParameter("id"));
                if (id == null) {
                    session.setAttribute("error", "ID voucher không hợp lệ.");
                } else if (isAdmin ? voucherDAO.deleteVoucher(id) : voucherDAO.deleteVoucher(id, targetStoreId)) {
                    session.setAttribute("success", "Xóa voucher thành công.");
                } else {
                    session.setAttribute("error", "Không thể xóa voucher này.");
                }
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("error", "Lỗi thao tác Voucher: " + e.getMessage());
        }


        response.sendRedirect("vouchers");
    }

    private Integer resolveTargetStoreId(HttpServletRequest request, Account acc, StoreDAO storeDAO) {
        if (RoleHelper.isAdmin(acc)) {
            Integer storeId = ValidationUtil.parsePositiveInt(request.getParameter("storeId"));
            if (storeId == null) {
                return null;
            }
            return storeDAO.getStoreById(storeId) != null ? storeId : null;
        }

        Store store = storeDAO.getStoreByOwnerId(acc.getUid());
        return store != null ? store.getId() : null;
    }

    private Voucher buildVoucherFromRequest(HttpServletRequest request, int storeId) {
        String code = ValidationUtil.normalize(request.getParameter("code")).toUpperCase();
        Integer discountPercent = ValidationUtil.parsePositiveInt(request.getParameter("discountPercent"));
        Integer maxDiscount = ValidationUtil.parsePositiveInt(request.getParameter("maxDiscount"));
        Integer minOrderValue = ValidationUtil.parsePositiveInt(request.getParameter("minOrderValue"));
        String expiryDate = ValidationUtil.normalize(request.getParameter("expiryDate"));

        if (ValidationUtil.isBlank(code)) {
            throw new IllegalArgumentException("Ma voucher khong duoc de trong.");
        }
        if (discountPercent == null || discountPercent < 1 || discountPercent > 100) {
            throw new IllegalArgumentException("Phan tram giam phai nam trong khoang tu 1 den 100.");
        }
        if (ValidationUtil.isBlank(expiryDate)) {
            throw new IllegalArgumentException("Ngay het han khong duoc de trong.");
        }

        Voucher voucher = new Voucher();
        voucher.setCode(code);
        voucher.setDiscountPercent(discountPercent);
        voucher.setMaxDiscount(maxDiscount);
        voucher.setMinOrderValue(minOrderValue);
        voucher.setExpiryDate(expiryDate);
        voucher.setStoreId(storeId);
        return voucher;
    }
}
