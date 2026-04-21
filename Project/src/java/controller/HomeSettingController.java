package controller;

import dal.HomeSettingDAO;
import dal.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.HomeSetting;
import util.RoleHelper;
import util.ValidationUtil;

@WebServlet(name = "HomeSettingController", urlPatterns = {"/homeSetting"})
public class HomeSettingController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        jakarta.servlet.http.HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        if (!RoleHelper.isAdmin(account)) {
            response.sendRedirect("home");
            return;
        }

        HomeSettingDAO homeSettingDAO = new HomeSettingDAO();
        String message = (String) session.getAttribute("message");
        String error = (String) session.getAttribute("error");
        session.removeAttribute("message");
        session.removeAttribute("error");

        if (request.getMethod().equalsIgnoreCase("POST")) {
            HomeSetting currentSetting = homeSettingDAO.getHomeSetting();
            HomeSetting newSetting = new HomeSetting();
            newSetting.setId(currentSetting.getId());
            newSetting.setHeroBadge(ValidationUtil.normalize(request.getParameter("heroBadge")));
            newSetting.setHeroTitle(ValidationUtil.normalize(request.getParameter("heroTitle")));
            newSetting.setHeroHighlight(ValidationUtil.normalize(request.getParameter("heroHighlight")));
            newSetting.setHeroDescription(ValidationUtil.normalize(request.getParameter("heroDescription")));
            newSetting.setPrimaryButtonText(ValidationUtil.normalize(request.getParameter("primaryButtonText")));
            newSetting.setSecondaryButtonText(ValidationUtil.normalize(request.getParameter("secondaryButtonText")));
            newSetting.setFeaturedTitle(ValidationUtil.normalize(request.getParameter("featuredTitle")));
            newSetting.setShowStats(request.getParameter("showStats") != null);
            newSetting.setShowFilterSidebar(request.getParameter("showFilterSidebar") != null);
            newSetting.setShowFeaturedSection(request.getParameter("showFeaturedSection") != null);
            newSetting.setFeaturedMode(ValidationUtil.normalize(request.getParameter("featuredMode")));
            newSetting.setFeaturedProductId(ValidationUtil.parsePositiveInt(request.getParameter("featuredProductId")));

            if (ValidationUtil.isBlank(newSetting.getHeroTitle())
                    || ValidationUtil.isBlank(newSetting.getHeroDescription())
                    || ValidationUtil.isBlank(newSetting.getPrimaryButtonText())
                    || ValidationUtil.isBlank(newSetting.getFeaturedTitle())) {
                error = "Nội dung trang home không được để trống các mục chính.";
            } else if (homeSettingDAO.updateHomeSetting(newSetting)) {
                message = "Cập nhật cấu hình trang chủ thành công.";
            } else {
                error = "Không thể lưu cấu hình trang chủ. Vui lòng thử lại.";
            }
            
            session.setAttribute("message", message);
            session.setAttribute("error", error);
            response.sendRedirect("homeSetting");
            return;
        }

        request.setAttribute("allProducts", new ProductDAO().getAllProducts());
        request.setAttribute("homeSetting", homeSettingDAO.getHomeSetting());
        request.setAttribute("message", message);
        request.setAttribute("error", error);
        request.getRequestDispatcher("HomeSetting.jsp").forward(request, response);
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
