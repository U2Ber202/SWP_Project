package controller;

import dal.HomeSettingDAO;
import dal.ProductDAO;
import dal.SliderDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.HomeSetting;
import model.Slider;
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
        SliderDAO sliderDAO = new SliderDAO();
        String message = (String) session.getAttribute("message");
        String error = (String) session.getAttribute("error");
        session.removeAttribute("message");
        session.removeAttribute("error");

        String action = request.getParameter("action");

        if (request.getMethod().equalsIgnoreCase("POST")) {
            if ("updateGeneral".equals(action) || action == null) {
                // Original logic for home setting update
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
            } else if ("addSlider".equals(action)) {
                Slider s = new Slider();
                s.setTitle(request.getParameter("title"));
                s.setImageUrl(request.getParameter("imageUrl"));
                s.setProductId(ValidationUtil.parsePositiveInt(request.getParameter("productId")) != null ? ValidationUtil.parsePositiveInt(request.getParameter("productId")) : 0);
                s.setDescription(request.getParameter("description"));

                s.setStatus(request.getParameter("status") != null);

                if (sliderDAO.addSlider(s)) {
                    message = "Thêm slider thành công.";
                } else {
                    error = "Thêm slider thất bại.";
                }
            } else if ("updateSlider".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Slider s = sliderDAO.getSliderById(id);
                if (s != null) {
                    s.setTitle(request.getParameter("title"));
                    s.setImageUrl(request.getParameter("imageUrl"));
                    s.setProductId(ValidationUtil.parsePositiveInt(request.getParameter("productId")) != null ? ValidationUtil.parsePositiveInt(request.getParameter("productId")) : 0);
                    s.setDescription(request.getParameter("description"));

                    s.setStatus(request.getParameter("status") != null);
                    if (sliderDAO.updateSlider(s)) {
                        message = "Cập nhật slider thành công.";
                    } else {
                        error = "Cập nhật slider thất bại.";
                    }
                }
            } else if ("deleteSlider".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (sliderDAO.deleteSlider(id)) {
                    message = "Xóa slider thành công.";
                } else {
                    error = "Xóa slider thất bại.";
                }
            }

            session.setAttribute("message", message);
            session.setAttribute("error", error);
            response.sendRedirect("homeSetting");
            return;
        }

        request.setAttribute("allProducts", new ProductDAO().getAllProducts());
        request.setAttribute("homeSetting", homeSettingDAO.getHomeSetting());
        request.setAttribute("sliders", sliderDAO.getAllSliders());
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

