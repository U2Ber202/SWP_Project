package controller;

import dal.CategoryDAO;
import dal.NewsDAO;
import dal.ProductDAO;
import dal.StoreDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;
import model.Store;

@WebServlet(name = "StoreFrontController", urlPatterns = {"/store-front"})
public class StoreFrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String storeIdParam = request.getParameter("id");
        if (storeIdParam == null) {
            response.sendRedirect("stores");
            return;
        }

        int storeId = Integer.parseInt(storeIdParam);
        StoreDAO storeDAO = new StoreDAO();
        Store store = storeDAO.getStoreById(storeId);
        
        if (store == null) {
            response.sendRedirect("stores");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        
        List<Product> products = productDAO.getProductsByStoreId(storeId);
        List<Category> categories = categoryDAO.getCategoriesByStore(storeId);
        List<model.News> listNews = new NewsDAO().getVisibleOnlyStoreNews(storeId);
        
        request.setAttribute("store", store);
        request.setAttribute("listProducts", products);
        request.setAttribute("listCategories", categories);
        request.setAttribute("listNews", listNews);
        
        request.getRequestDispatcher("StoreFront.jsp").forward(request, response);
    }
}
