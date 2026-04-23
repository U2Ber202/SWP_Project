package controller;

import dal.FeedbackDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Feedback;
import model.Store;
import util.RoleHelper;

@WebServlet(name = "ExportFeedbackController", urlPatterns = {"/exportFeedback"})
public class ExportFeedbackController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc == null || (!RoleHelper.isOwner(acc) && !RoleHelper.isAdmin(acc))) {
            response.sendRedirect("home");
            return;
        }

        FeedbackDAO feedbackDAO = new FeedbackDAO();
        List<Feedback> list;

        if (RoleHelper.isAdmin(acc)) {
            list = feedbackDAO.getAllFeedback();
        } else {
            StoreDAO storeDAO = new StoreDAO();
            Store store = storeDAO.getStoreByOwnerId(acc.getUid());
            if (store != null) {
                list = feedbackDAO.getFeedbackByStore(store.getId());
            } else {
                list = new java.util.ArrayList<>();
            }
        }

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"feedbacks.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            // Write BOM for Excel to recognize UTF-8
            writer.write('\ufeff');
            
            // Header
            writer.println("ID,Khách hàng,Sản phẩm,Cửa hàng,Số sao,Nội dung,Ngày tạo");

            for (Feedback f : list) {
                writer.println(String.format("%d,\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\"",
                        f.getId(),
                        escapeCsv(f.getUserName()),
                        escapeCsv(f.getProductName()),
                        escapeCsv(f.getStoreName()),
                        f.getRating(),
                        escapeCsv(f.getContent()),
                        f.getCreateDate().toString()
                ));
            }
        }
    }

    private String escapeCsv(String text) {
        if (text == null) return "";
        return text.replace("\"", "\"\"");
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
