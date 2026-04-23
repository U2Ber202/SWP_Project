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
        n.setIsVisible(rs.getBoolean("is_visible"));
        try {
            n.setStoreName(rs.getString("store_name"));
        } catch (SQLException e) {
            // Field might not exist in all queries
        }
        return n;
    }

    private final String SELECT_BASE = "SELECT n.*, s.store_name as store_name FROM News n LEFT JOIN Store s ON n.store_id = s.store_id";

    public List<News> getAllNews() {
        return getBySql(SELECT_BASE + " ORDER BY n.created_at DESC");
    }

    public List<News> getAllVisibleNews() {
        return getBySql(SELECT_BASE + " WHERE n.is_visible = 1 ORDER BY n.created_at DESC");
    }

    public List<News> getSystemNews() {
        return getBySql(SELECT_BASE + " WHERE n.store_id IS NULL ORDER BY n.created_at DESC");
    }

    public List<News> getVisibleSystemNews() {
        return getBySql(SELECT_BASE + " WHERE n.store_id IS NULL AND n.is_visible = 1 ORDER BY n.created_at DESC");
    }

    public List<News> getNewsForAdmin() {
        return getBySql(SELECT_BASE + " ORDER BY n.created_at DESC");
    }

    public List<News> getNewsByStore(int storeId) {
        return getBySql(SELECT_BASE + " WHERE n.store_id IS NULL OR n.store_id = ? ORDER BY n.created_at DESC", storeId);
    }

    public List<News> getVisibleNewsByStore(int storeId) {
        return getBySql(SELECT_BASE + " WHERE (n.store_id IS NULL OR n.store_id = ?) AND n.is_visible = 1 ORDER BY n.created_at DESC", storeId);
    }
    
    public List<News> getOnlyStoreNews(int storeId) {
        return getBySql(SELECT_BASE + " WHERE n.store_id = ? ORDER BY n.created_at DESC", storeId);
    }

    public List<News> getVisibleOnlyStoreNews(int storeId) {
        return getBySql(SELECT_BASE + " WHERE n.store_id = ? AND n.is_visible = 1 ORDER BY n.created_at DESC", storeId);
    }

    public News getNewsById(int id) {
        return getSingleBySql(SELECT_BASE + " WHERE n.id = ?", id);
    }

    public void insert(News n) {
        executeUpdate("INSERT INTO News (title, content, image, store_id, is_visible) VALUES (?, ?, ?, ?, ?)",
                n.getTitle(), n.getContent(), n.getImage(), n.getStoreId(), n.isIsVisible());
    }

    public void update(News n) {
        executeUpdate("UPDATE News SET title = ?, content = ?, image = ?, store_id = ?, is_visible = ? WHERE id = ?",
                n.getTitle(), n.getContent(), n.getImage(), n.getStoreId(), n.isIsVisible(), n.getId());
    }

    public void updateStatus(int id, boolean isVisible) {
        executeUpdate("UPDATE News SET is_visible = ? WHERE id = ?", isVisible, id);
    }

    public void delete(int id) {
        // Instead of deleting, we could mark as invisible, but the user said "Bài đăng chỉ có Visible/Invisible chứ không có xóa"
        // So we might want to keep the delete method but make it do nothing or change it to updateStatus.
        // I'll keep it but it will just hide it.
        updateStatus(id, false);
    }

    private List<News> getBySql(String sql, Object... params) {
        List<News> list = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
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
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
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

    private void executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
