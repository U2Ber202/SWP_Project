package controller;

import dal.NewsDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.News;

@WebServlet(name = "NewsDetailController", urlPatterns = {"/newsDetail"})
public class NewsDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            NewsDAO newsDAO = new NewsDAO();
            News news = newsDAO.getNewsById(id);

            // ✅ KIỂM TRA VISIBLE
            if (news == null || !news.isVisible()) {
                response.sendRedirect("home");
                return;
            }

            request.setAttribute("news", news);
            request.getRequestDispatcher("NewsDetail.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("home");
        }
    }
}
