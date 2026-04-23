package controller;

import dal.FeedbackDAO;
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
import model.Feedback;
import model.Store;
import util.RoleHelper;

@WebServlet(name = "FeedbackManagerController", urlPatterns = {"/feedbacks"})
public class FeedbackManagerController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc == null || (!RoleHelper.isOwner(acc) && !RoleHelper.isAdmin(acc))) {
            response.sendRedirect("home");
            return;
        }

        FeedbackDAO feedbackDAO = new FeedbackDAO();
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            feedbackDAO.deleteFeedback(id);
            session.setAttribute("success", "Xóa đánh giá thành công!");
            response.sendRedirect("feedbacks");
            return;
        }

        if ("statistic".equals(action)) {
            int storeId = -1;
            if (!RoleHelper.isAdmin(acc)) {
                StoreDAO storeDAO = new StoreDAO();
                Store store = storeDAO.getStoreByOwnerId(acc.getUid());
                if (store != null) storeId = store.getId();
            }
            java.util.Map<Integer, Integer> stats = feedbackDAO.getFeedbackStatistics(storeId);
            request.setAttribute("stats", stats);
            request.getRequestDispatcher("FeedbackStatistic.jsp").forward(request, response);
            return;
        }

        String ratingParam = request.getParameter("rating");
        int filterRating = -1;
        try {
            if (ratingParam != null && !ratingParam.isEmpty()) {
                filterRating = Integer.parseInt(ratingParam);
            }
        } catch (NumberFormatException e) {
            filterRating = -1;
        }

        List<Feedback> allFeedbacks;
        if (RoleHelper.isAdmin(acc)) {
            if (filterRating != -1) {
                allFeedbacks = feedbackDAO.getAllFeedbackByRating(filterRating);
            } else {
                allFeedbacks = feedbackDAO.getAllFeedback();
            }
            request.setAttribute("feedbackScope", "admin");
        } else {
            StoreDAO storeDAO = new StoreDAO();
            Store store = storeDAO.getStoreByOwnerId(acc.getUid());
            if (store != null) {
                if (filterRating != -1) {
                    allFeedbacks = feedbackDAO.getFeedbackByRating(store.getId(), filterRating);
                } else {
                    allFeedbacks = feedbackDAO.getFeedbackByStore(store.getId());
                }
                request.setAttribute("store", store);
            } else {
                allFeedbacks = new java.util.ArrayList<>();
            }
            request.setAttribute("feedbackScope", "owner");
        }

        request.setAttribute("currentRating", filterRating);

        final int PAGE_SIZE = 10;
        int page = 1;
        try {
            String p = request.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (Exception e) { page = 1; }

        int totalFeedbacks = allFeedbacks.size();
        int totalPage = (int) Math.ceil((double) totalFeedbacks / PAGE_SIZE);
        if (page > totalPage && totalPage > 0) page = totalPage;
        if (page < 1) page = 1;

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalFeedbacks);
        List<Feedback> listFeedbacks = (fromIndex < totalFeedbacks) ? allFeedbacks.subList(fromIndex, toIndex) : new java.util.ArrayList<>();

        request.setAttribute("listFeedbacks", listFeedbacks);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.getRequestDispatcher("FeedbackManager.jsp").forward(request, response);
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
