package controller;

import dal.CategoryDAO;
import dal.HomeSettingDAO;
import dal.NewsDAO;
import dal.ProductDAO;
import dal.ShippingDAO;
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
import model.HomeSetting;
import model.Product;
import model.Store;
import util.CartService;
import util.RoleHelper;

@WebServlet(name = "HomeController", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        final int PAGE_SIZE = 10;
        int page = parsePage(request.getParameter("page"));

        HttpSession session = request.getSession();
        CartService.expireCartItems(session);
        ProductDAO productDAO = new ProductDAO();
        StoreDAO storeDAO = new StoreDAO();
        HomeSetting homeSetting = new HomeSettingDAO().getHomeSetting();
        Account acc = (Account) session.getAttribute("acc");

        Store ownerStore = RoleHelper.isOwner(acc) ? storeDAO.getStoreByOwnerId(acc.getUid()) : null;
        Store warehouseStore = RoleHelper.isWarehouseManager(acc) ? storeDAO.getStoreByWarehouseManagerId(acc.getUid()) : null;
        Integer shipperStoreId = RoleHelper.isShipper(acc) ? new ShippingDAO().getStoreIdByShipperId(acc.getUid()) : null;
        Store shipperStore = shipperStoreId != null ? storeDAO.getStoreById(shipperStoreId) : null;
        Store scopedStore = ownerStore != null ? ownerStore : (warehouseStore != null ? warehouseStore : shipperStore);

        session.setAttribute("ownerStore", ownerStore);
        session.setAttribute("warehouseStore", warehouseStore);
        session.setAttribute("shipperStore", shipperStore);
        if (warehouseStore != null) {
            session.setAttribute("warehouseStoreId", String.valueOf(warehouseStore.getId()));
        }

        String keyword = request.getParameter("keyword");
        String categoryIdParam = request.getParameter("categoryId");
        String storeIdParam = request.getParameter("storeId");
        Integer categoryId = null;
        Integer selectedStoreId = null;

        if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
            categoryId = Integer.parseInt(categoryIdParam);
        }
        if (storeIdParam != null && !storeIdParam.trim().isEmpty()) {
            selectedStoreId = Integer.parseInt(storeIdParam);
        }

        List<Category> listCategories;
        List<Product> matchedProducts;
        List<Product> featuredProducts;

        if (scopedStore != null) {
            listCategories = new CategoryDAO().getCategoriesByStore(scopedStore.getId());
            featuredProducts = getFeaturedProducts(productDAO, homeSetting, scopedStore.getId());
            selectedStoreId = scopedStore.getId();
            if (keyword != null && !keyword.trim().isEmpty()) {
                matchedProducts = productDAO.search(keyword.trim(), scopedStore.getId());
            } else if (categoryId != null) {
                matchedProducts = productDAO.getProductsByCategoryIdAndStoreId(categoryId, scopedStore.getId());
            } else {
                matchedProducts = productDAO.getProductsByStoreId(scopedStore.getId());
            }
        } else {
            listCategories = new CategoryDAO().getAllCategories();
            featuredProducts = getFeaturedProducts(productDAO, homeSetting, null);
            if (keyword != null && !keyword.trim().isEmpty()) {
                matchedProducts = productDAO.search(keyword.trim(), selectedStoreId == null ? 0 : selectedStoreId);
            } else if (categoryId != null && selectedStoreId != null) {
                matchedProducts = productDAO.getProductsByCategoryIdAndStoreId(categoryId, selectedStoreId);
            } else if (categoryId != null) {
                matchedProducts = productDAO.getProductsByCategoryId(categoryId);
            } else if (selectedStoreId != null) {
                matchedProducts = productDAO.getProductsByStoreId(selectedStoreId);
            } else {
                matchedProducts = productDAO.getAllProducts();
            }
        }

        matchedProducts = prioritizeProducts(matchedProducts, homeSetting == null ? null : homeSetting.getFeaturedProductId());
        int totalProducts = matchedProducts.size();
        int totalPage = Math.max(1, (int) Math.ceil((double) totalProducts / PAGE_SIZE));
        page = Math.min(page, totalPage);
        List<Product> listProducts = paginateProducts(matchedProducts, page, PAGE_SIZE);

        NewsDAO newsDAO = new NewsDAO();
        List<model.News> listNews;

// ✅ Phân biệt rõ ràng:
        if (RoleHelper.isAdmin(acc)) {
            // Admin thấy TẤT CẢ (kể cả invisible) để quản lý
            listNews = newsDAO.getAllNews();
        } else {
            // Tất cả các role khác (Customer, Owner, Shipper, Warehouse Manager, hoặc chưa đăng nhập)
            // chỉ thấy bài viết có visible = 1
            Integer storeId = null;

            if (acc == null || RoleHelper.isCustomer(acc)) {
                // Customer hoặc chưa đăng nhập: lấy tất cả bài visible (cả hệ thống và cửa hàng)
                listNews = newsDAO.getAllVisibleNews();
            } else if (RoleHelper.isOwner(acc)) {
                Store s = new StoreDAO().getStoreByOwnerId(acc.getUid());
                if (s != null) {
                    storeId = s.getId();
                }
                listNews = newsDAO.getNewsByStore(storeId);
            } else if (RoleHelper.isWarehouseManager(acc)) {
                Store s = new StoreDAO().getStoreByWarehouseManagerId(acc.getUid());
                if (s != null) {
                    storeId = s.getId();
                }
                listNews = newsDAO.getNewsByStore(storeId);
            } else if (RoleHelper.isShipper(acc)) {
                storeId = new dal.ShippingDAO().getStoreIdByShipperId(acc.getUid());
                listNews = newsDAO.getNewsByStore(storeId);
            } else {
                // Fallback: chỉ lấy tin hệ thống visible
                listNews = newsDAO.getSystemNews();
            }
        }

//        if (listNews.size() > 4) {
//            listNews = listNews.subList(0, 4);
//        }

        request.setAttribute("listCategories", listCategories != null ? listCategories : new ArrayList<>());
        request.setAttribute("listStores", (scopedStore != null) ? java.util.Arrays.asList(scopedStore) : (storeDAO.getAllStores() != null ? storeDAO.getAllStores() : new ArrayList<>()));
        request.setAttribute("featuredProducts", featuredProducts != null ? featuredProducts : new ArrayList<>());
        request.setAttribute("homeSetting", homeSetting != null ? homeSetting : new HomeSettingDAO().createDefaultSetting());
        request.setAttribute("activeSliders", new dal.SliderDAO().getActiveSliders());
        request.setAttribute("listNews", listNews);
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("listProducts", listProducts != null ? listProducts : new ArrayList<>());
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("selectedStoreId", selectedStoreId);
        request.setAttribute("key", keyword);
        request.setAttribute("productCount", totalProducts);

        String queryString = request.getQueryString();
        request.getSession().setAttribute("urlHistory", queryString == null ? "home" : "home?" + queryString);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    private List<Product> getFeaturedProducts(ProductDAO productDAO, HomeSetting homeSetting, Integer storeId) {
        String featuredMode = homeSetting == null ? HomeSetting.FEATURE_MODE_NEWEST : homeSetting.getFeaturedMode();
        boolean hasStoreId = storeId != null && storeId > 0;

        List<Product> products;
        if (HomeSetting.FEATURE_MODE_PRICE_ASC.equalsIgnoreCase(featuredMode)) {
            products = hasStoreId ? productDAO.getTopProductsByPriceAsc(storeId) : productDAO.getTopProductsByPriceAsc();
        } else if (HomeSetting.FEATURE_MODE_PRICE_DESC.equalsIgnoreCase(featuredMode)) {
            products = hasStoreId ? productDAO.getTopProductsByPriceDesc(storeId) : productDAO.getTopProductsByPriceDesc();
        } else {
            products = hasStoreId ? productDAO.getAllNewProductsByStoreId(storeId) : productDAO.getAllNewProducts();
        }
        return prioritizeProducts(products, homeSetting == null ? null : homeSetting.getFeaturedProductId());
    }

    private List<Product> prioritizeProducts(List<Product> products, Integer featuredProductId) {
        if (products == null) {
            return new ArrayList<>();
        }
        if (featuredProductId == null || featuredProductId <= 0) {
            return products;
        }

        int featuredIndex = -1;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == featuredProductId) {
                featuredIndex = i;
                break;
            }
        }
        if (featuredIndex <= 0) {
            return products;
        }

        List<Product> orderedProducts = new ArrayList<>(products);
        Product featuredProduct = orderedProducts.remove(featuredIndex);
        orderedProducts.add(0, featuredProduct);
        return orderedProducts;
    }

    private int parsePage(String pageParam) {
        try {
            int parsed = Integer.parseInt(pageParam);
            return parsed > 0 ? parsed : 1;
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    private List<Product> paginateProducts(List<Product> products, int page, int pageSize) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }
        int fromIndex = Math.max(0, (page - 1) * pageSize);
        if (fromIndex >= products.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(products.size(), fromIndex + pageSize);
        return new ArrayList<>(products.subList(fromIndex, toIndex));
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
