package controller;

import dal.StockImportDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.StockImport;
import model.Store;
import util.RoleHelper;

@WebServlet(name = "StockHistoryController", urlPatterns = {"/stockHistory"})
public class StockHistoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");
        
        if (!RoleHelper.isWarehouseManager(acc) && !RoleHelper.isOwner(acc) && !RoleHelper.isAdmin(acc)) {
            response.sendRedirect("home");
            return;
        }

        StoreDAO storeDAO = new StoreDAO();
        Store store = null;
        if (RoleHelper.isAdmin(acc)) {
            String sid = request.getParameter("storeId");
            if (sid != null) {
                store = storeDAO.getStoreById(Integer.parseInt(sid));
            }
        } else if (RoleHelper.isWarehouseManager(acc)) {
            store = storeDAO.getStoreByWarehouseManagerId(acc.getUid());
        } else if (RoleHelper.isOwner(acc)) {
            store = storeDAO.getStoreByOwnerId(acc.getUid());
        }

        if (store != null) {
            StockImportDAO stockDAO = new StockImportDAO();
            List<StockImport> history = stockDAO.getStockImportsByStoreId(store.getId());
            request.setAttribute("stockHistory", history);
            request.setAttribute("storeName", store.getName());
        }

        request.getRequestDispatcher("StockHistory.jsp").forward(request, response);
    }
}
