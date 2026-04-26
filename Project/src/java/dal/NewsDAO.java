package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.News;

public class NewsDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(NewsDAO.class.getName());

    private News mapNews(ResultSet rs) throws SQLException {
        News n = new News();
        n.setId(rs.getInt("id"));
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));
        n.setImage(rs.getString("image"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        int storeId = rs.getInt("store_id");
        n.setStoreId(rs.wasNull() ? null : storeId);
        n.setVisible(rs.getBoolean("visible")); // ✅ Map thêm visible
        try {
            n.setStoreName(rs.getString("store_name"));
        } catch (SQLException e) {
            // Field might not exist in all queries
        }
        return n;
    }

    private final String SELECT_BASE
            = "SELECT n.*, s.store_name as store_name FROM News n LEFT JOIN Store s ON n.store_id = s.store_id";

    public List<News> getAllNews() {
        return getBySql(SELECT_BASE + " ORDER BY n.created_at DESC");
    }

    public List<News> getSystemNews() {
        return getBySql(SELECT_BASE + " WHERE n.store_id IS NULL AND n.visible = 1 ORDER BY n.created_at DESC");
    }

    public List<News> getNewsForAdmin() {
        // Admin thấy tất cả kể cả invisible
        return getBySql(SELECT_BASE + " ORDER BY n.created_at DESC");
    }

    // ✅ Thêm method mới để lấy bài viết hiển thị cho user thường
    public List<News> getVisibleNewsForHome() {
        return getBySql(SELECT_BASE + " WHERE n.visible = 1 ORDER BY n.created_at DESC");
    }

    public List<News> getNewsByStore(int storeId) {
        // ✅ Chỉ lấy bài visible = 1
        return getBySql(SELECT_BASE + " WHERE (n.store_id IS NULL OR n.store_id = ?) AND n.visible = 1 ORDER BY n.created_at DESC", storeId);
    }

    public List<News> getOnlyStoreNews(int storeId) {
        // Owner thấy tất cả bài của mình (kể cả invisible) để có thể quản lý
        return getBySql(SELECT_BASE + " WHERE n.store_id = ? ORDER BY n.created_at DESC", storeId);
    }

    public News getNewsById(int id) {
        return getSingleBySql(SELECT_BASE + " WHERE n.id = ?", id);
    }

//    public void insert(News n) {
//        executeUpdate(
//            "INSERT INTO News (title, content, image, store_id, visible) VALUES (?, ?, ?, ?, 1)",
//            n.getTitle(), n.getContent(), n.getImage(), n.getStoreId()
//        );
//    }
    public void insert(News n) {
        // ✅ Đảm bảo visible = 1 khi mới tạo
        executeUpdate(
                "INSERT INTO News (title, content, image, store_id, visible) VALUES (?, ?, ?, ?, 1)",
                n.getTitle(), n.getContent(), n.getImage(), n.getStoreId()
        );
    }

    public void update(News n) {
        executeUpdate(
                "UPDATE News SET title = ?, content = ?, image = ?, store_id = ? WHERE id = ?",
                n.getTitle(), n.getContent(), n.getImage(), n.getStoreId(), n.getId()
        );
    }

    // Toggle visible thay vì delete
    public void toggleVisible(int id) {
        executeUpdate("UPDATE News SET visible = 1 - visible WHERE id = ?", id);
    }

    // Kiểm tra title trùng khi THÊM MỚI
    public boolean isTitleExist(String title) {
        return existsBySql(
                "SELECT 1 FROM News WHERE LOWER(TRIM(title)) = LOWER(TRIM(?))", title
        );
    }

    // Kiểm tra title trùng khi CHỈNH SỬA (bỏ qua bài hiện tại)
    public boolean isTitleExistForAnother(String title, int excludeId) {
        return existsBySql(
                "SELECT 1 FROM News WHERE LOWER(TRIM(title)) = LOWER(TRIM(?)) AND id <> ?",
                title, excludeId
        );
    }

    // ==================== PRIVATE HELPERS ====================
    private List<News> getBySql(String sql, Object... params) {
        List<News> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNews(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private News getSingleBySql(String sql, Object... params) {
        try (Connection conn = getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapNews(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean existsBySql(String sql, Object... params) {
        try (Connection conn = getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection(); PreparedStatement stm = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public List<News> getAllVisibleNews() {
        return getBySql(SELECT_BASE + " WHERE n.visible = 1 ORDER BY n.created_at DESC");
    }

}
