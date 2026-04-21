package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.HomeSetting;

public class HomeSettingDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(HomeSettingDAO.class.getName());

    public HomeSetting getHomeSetting() {
        String sql = "SELECT TOP 1 * FROM HomeSetting ORDER BY id ASC";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql);
                ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return mapSetting(rs);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return createDefaultSetting();
    }

    public boolean updateHomeSetting(HomeSetting setting) {
        String sql = "UPDATE HomeSetting SET hero_badge = ?, hero_title = ?, hero_highlight = ?, "
                + "hero_description = ?, primary_button_text = ?, secondary_button_text = ?, "
                + "featured_title = ?, show_stats = ?, show_filter_sidebar = ?, "
                + "show_featured_section = ?, featured_mode = ?, featured_product_id = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, setting.getHeroBadge());
            stm.setString(2, setting.getHeroTitle());
            stm.setString(3, setting.getHeroHighlight());
            stm.setString(4, setting.getHeroDescription());
            stm.setString(5, setting.getPrimaryButtonText());
            stm.setString(6, setting.getSecondaryButtonText());
            stm.setString(7, setting.getFeaturedTitle());
            stm.setBoolean(8, setting.isShowStats());
            stm.setBoolean(9, setting.isShowFilterSidebar());
            stm.setBoolean(10, setting.isShowFeaturedSection());
            stm.setString(11, normalizeMode(setting.getFeaturedMode()));
            if (setting.getFeaturedProductId() != null && setting.getFeaturedProductId() > 0) {
                stm.setInt(12, setting.getFeaturedProductId());
            } else {
                stm.setNull(12, java.sql.Types.INTEGER);
            }
            stm.setInt(13, setting.getId() > 0 ? setting.getId() : 1);
            if (stm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return insertHomeSetting(setting);
    }

    private HomeSetting mapSetting(ResultSet rs) throws SQLException {
        HomeSetting setting = new HomeSetting();
        setting.setId(rs.getInt("id"));
        setting.setHeroBadge(rs.getString("hero_badge"));
        setting.setHeroTitle(rs.getString("hero_title"));
        setting.setHeroHighlight(rs.getString("hero_highlight"));
        setting.setHeroDescription(rs.getString("hero_description"));
        setting.setPrimaryButtonText(rs.getString("primary_button_text"));
        setting.setSecondaryButtonText(rs.getString("secondary_button_text"));
        setting.setFeaturedTitle(rs.getString("featured_title"));
        setting.setShowStats(rs.getBoolean("show_stats"));
        setting.setShowFilterSidebar(rs.getBoolean("show_filter_sidebar"));
        setting.setShowFeaturedSection(rs.getBoolean("show_featured_section"));
        setting.setFeaturedMode(normalizeMode(rs.getString("featured_mode")));
        int featuredProductId = rs.getInt("featured_product_id");
        setting.setFeaturedProductId(rs.wasNull() ? null : featuredProductId);
        return setting;
    }

    public HomeSetting createDefaultSetting() {
        HomeSetting setting = new HomeSetting();
        setting.setId(1);
        setting.setHeroBadge("Bộ sưu tập nổi bật");
        setting.setHeroTitle("Nâng cấp phong cách mỗi ngày");
        setting.setHeroHighlight("Sneaker");
        setting.setHeroDescription("Admin có thể thay đổi nội dung trang home, bật tắt bộ lọc và chọn kiểu top sản phẩm hiển thị.");
        setting.setPrimaryButtonText("Mua ngay");
        setting.setSecondaryButtonText("Xem thêm");
        setting.setFeaturedTitle("Sản phẩm nổi bật");
        setting.setShowStats(true);
        setting.setShowFilterSidebar(true);
        setting.setShowFeaturedSection(true);
        setting.setFeaturedMode(HomeSetting.FEATURE_MODE_NEWEST);
        setting.setFeaturedProductId(null);
        return setting;
    }

    private String normalizeMode(String mode) {
        if (HomeSetting.FEATURE_MODE_PRICE_ASC.equalsIgnoreCase(mode)) {
            return HomeSetting.FEATURE_MODE_PRICE_ASC;
        }
        if (HomeSetting.FEATURE_MODE_PRICE_DESC.equalsIgnoreCase(mode)) {
            return HomeSetting.FEATURE_MODE_PRICE_DESC;
        }
        return HomeSetting.FEATURE_MODE_NEWEST;
    }

    private boolean insertHomeSetting(HomeSetting setting) {
        String sql = "INSERT INTO HomeSetting (id, hero_badge, hero_title, hero_highlight, hero_description, "
                + "primary_button_text, secondary_button_text, featured_title, show_stats, show_filter_sidebar, "
                + "show_featured_section, featured_mode, featured_product_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, setting.getId() > 0 ? setting.getId() : 1);
            stm.setString(2, setting.getHeroBadge());
            stm.setString(3, setting.getHeroTitle());
            stm.setString(4, setting.getHeroHighlight());
            stm.setString(5, setting.getHeroDescription());
            stm.setString(6, setting.getPrimaryButtonText());
            stm.setString(7, setting.getSecondaryButtonText());
            stm.setString(8, setting.getFeaturedTitle());
            stm.setBoolean(9, setting.isShowStats());
            stm.setBoolean(10, setting.isShowFilterSidebar());
            stm.setBoolean(11, setting.isShowFeaturedSection());
            stm.setString(12, normalizeMode(setting.getFeaturedMode()));
            if (setting.getFeaturedProductId() != null && setting.getFeaturedProductId() > 0) {
                stm.setInt(13, setting.getFeaturedProductId());
            } else {
                stm.setNull(13, java.sql.Types.INTEGER);
            }
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
