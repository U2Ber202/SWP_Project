package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Feedback;

public class FeedbackDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(FeedbackDAO.class.getName());

    public List<Feedback> getFeedbackByStore(int storeId) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.*, a.[user] as userName, p.[name] as productName, s.store_name as storeName " +
                     "FROM Feedback f " +
                     "JOIN Account a ON f.account_id = a.uID " +
                     "JOIN Product p ON f.product_id = p.id " +
                     "JOIN Store s ON f.store_id = s.store_id " +
                     "WHERE f.store_id = ? " +
                     "ORDER BY f.create_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, storeId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Feedback f = new Feedback();
                    f.setId(rs.getInt("id"));
                    f.setAccountId(rs.getInt("account_id"));
                    f.setProductId(rs.getInt("product_id"));
                    f.setStoreId(rs.getInt("store_id"));
                    f.setRating(rs.getInt("rating"));
                    f.setContent(rs.getString("content"));
                    f.setCreateDate(rs.getTimestamp("create_date"));
                    f.setIsEdited(rs.getBoolean("is_edited"));
                    f.setIsHidden(rs.getBoolean("is_hidden"));
                    f.setUserName(rs.getString("userName"));
                    f.setProductName(rs.getString("productName"));
                    f.setStoreName(rs.getString("storeName"));
                    list.add(f);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Feedback> getAllFeedbackForProduct(int productId) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.*, a.[user] as userName, p.[name] as productName, s.store_name as storeName " +
                     "FROM Feedback f " +
                     "JOIN Account a ON f.account_id = a.uID " +
                     "JOIN Product p ON f.product_id = p.id " +
                     "JOIN Store s ON f.store_id = s.store_id " +
                     "WHERE f.product_id = ? AND (f.is_hidden = 0 OR f.is_hidden IS NULL) " +
                     "ORDER BY f.create_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, productId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Feedback f = new Feedback();
                    f.setId(rs.getInt("id"));
                    f.setAccountId(rs.getInt("account_id"));
                    f.setProductId(rs.getInt("product_id"));
                    f.setStoreId(rs.getInt("store_id"));
                    f.setRating(rs.getInt("rating"));
                    f.setContent(rs.getString("content"));
                    f.setCreateDate(rs.getTimestamp("create_date"));
                    f.setIsEdited(rs.getBoolean("is_edited"));
                    f.setIsHidden(rs.getBoolean("is_hidden"));
                    f.setUserName(rs.getString("userName"));
                    f.setProductName(rs.getString("productName"));
                    f.setStoreName(rs.getString("storeName"));
                    list.add(f);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.*, a.[user] as userName, p.[name] as productName, s.store_name as storeName FROM Feedback f " +
                     "JOIN Account a ON f.account_id = a.uID " +
                     "JOIN Product p ON f.product_id = p.id " +
                     "JOIN Store s ON f.store_id = s.store_id " +
                     "ORDER BY f.create_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Feedback f = new Feedback();
                f.setId(rs.getInt("id"));
                f.setAccountId(rs.getInt("account_id"));
                f.setProductId(rs.getInt("product_id"));
                f.setStoreId(rs.getInt("store_id"));
                f.setRating(rs.getInt("rating"));
                f.setContent(rs.getString("content"));
                f.setCreateDate(rs.getTimestamp("create_date"));
                f.setIsEdited(rs.getBoolean("is_edited"));
                f.setIsHidden(rs.getBoolean("is_hidden"));
                f.setUserName(rs.getString("userName"));
                f.setProductName(rs.getString("productName"));
                f.setStoreName(rs.getString("storeName"));
                list.add(f);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Feedback> getFeedbackByRating(int storeId, int rating) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.*, a.[user] as userName, p.[name] as productName, s.store_name as storeName " +
                     "FROM Feedback f " +
                     "JOIN Account a ON f.account_id = a.uID " +
                     "JOIN Product p ON f.product_id = p.id " +
                     "JOIN Store s ON f.store_id = s.store_id " +
                     "WHERE f.store_id = ? AND f.rating = ? " +
                     "ORDER BY f.create_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, storeId);
            stm.setInt(2, rating);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Feedback f = new Feedback();
                    f.setId(rs.getInt("id"));
                    f.setAccountId(rs.getInt("account_id"));
                    f.setProductId(rs.getInt("product_id"));
                    f.setStoreId(rs.getInt("store_id"));
                    f.setRating(rs.getInt("rating"));
                    f.setContent(rs.getString("content"));
                    f.setCreateDate(rs.getTimestamp("create_date"));
                    f.setIsEdited(rs.getBoolean("is_edited"));
                    f.setIsHidden(rs.getBoolean("is_hidden"));
                    f.setUserName(rs.getString("userName"));
                    f.setProductName(rs.getString("productName"));
                    f.setStoreName(rs.getString("storeName"));
                    list.add(f);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Feedback> getAllFeedbackByRating(int rating) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT f.*, a.[user] as userName, p.[name] as productName, s.store_name as storeName " +
                     "FROM Feedback f " +
                     "JOIN Account a ON f.account_id = a.uID " +
                     "JOIN Product p ON f.product_id = p.id " +
                     "JOIN Store s ON f.store_id = s.store_id " +
                     "WHERE f.rating = ? AND f.is_hidden = 0 " +
                     "ORDER BY f.create_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, rating);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Feedback f = new Feedback();
                    f.setId(rs.getInt("id"));
                    f.setAccountId(rs.getInt("account_id"));
                    f.setProductId(rs.getInt("product_id"));
                    f.setStoreId(rs.getInt("store_id"));
                    f.setRating(rs.getInt("rating"));
                    f.setContent(rs.getString("content"));
                    f.setCreateDate(rs.getTimestamp("create_date"));
                    f.setIsEdited(rs.getBoolean("is_edited"));
                    f.setIsHidden(rs.getBoolean("is_hidden"));
                    f.setUserName(rs.getString("userName"));
                    f.setProductName(rs.getString("productName"));
                    f.setStoreName(rs.getString("storeName"));
                    list.add(f);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countFeedbackByUserOnProduct(int accountId, int productId) {
        String sql = "SELECT COUNT(*) FROM Feedback WHERE account_id = ? AND product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, accountId);
            stm.setInt(2, productId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public java.util.Map<Integer, Integer> getFeedbackStatistics(int storeId) {
        java.util.Map<Integer, Integer> stats = new java.util.HashMap<>();
        for (int i = 1; i <= 5; i++) stats.put(i, 0);
        
        String sql = "SELECT rating, COUNT(*) as count FROM Feedback WHERE store_id = ? GROUP BY rating";
        if (storeId == -1) { // Admin view
            sql = "SELECT rating, COUNT(*) as count FROM Feedback GROUP BY rating";
        }
        
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            if (storeId != -1) stm.setInt(1, storeId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    stats.put(rs.getInt("rating"), rs.getInt("count"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return stats;
    }

    public boolean hasBoughtProduct(int accountId, int productId) {
        String sql = "SELECT COUNT(*) FROM Orders o " +
                     "JOIN OrderDetail od ON o.id = od.order_id " +
                     "JOIN Product p ON od.productName = p.name " +
                     "WHERE o.account_id = ? AND p.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, accountId);
            stm.setInt(2, productId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean insertFeedback(int accountId, int productId, int storeId, int rating, String content) {
        String sql = "INSERT INTO Feedback (account_id, product_id, store_id, rating, content, create_date, is_edited, is_hidden) " +
                     "VALUES (?, ?, ?, ?, ?, GETDATE(), 0, 0)";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, accountId);
            stm.setInt(2, productId);
            stm.setInt(3, storeId);
            stm.setInt(4, rating);
            stm.setString(5, content);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Feedback getFeedbackById(int id) {
        String sql = "SELECT f.*, a.[user] as userName FROM Feedback f " +
                     "JOIN Account a ON f.account_id = a.uID " +
                     "WHERE f.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    Feedback f = new Feedback();
                    f.setId(rs.getInt("id"));
                    f.setAccountId(rs.getInt("account_id"));
                    f.setProductId(rs.getInt("product_id"));
                    f.setStoreId(rs.getInt("store_id"));
                    f.setRating(rs.getInt("rating"));
                    f.setContent(rs.getString("content"));
                    f.setCreateDate(rs.getTimestamp("create_date"));
                    f.setIsEdited(rs.getBoolean("is_edited"));
                    f.setIsHidden(rs.getBoolean("is_hidden"));
                    f.setUserName(rs.getString("userName"));
                    return f;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updateFeedback(int id, int rating, String content) {
        String sql = "UPDATE Feedback SET rating = ?, content = ?, is_edited = 1 WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, rating);
            stm.setString(2, content);
            stm.setInt(3, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void hideFeedback(int id, boolean hide) {
        String sql = "UPDATE Feedback SET is_hidden = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setBoolean(1, hide);
            stm.setInt(2, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteFeedback(int id) {
        String sql = "DELETE FROM Feedback WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
