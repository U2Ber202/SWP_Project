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
                     "WHERE f.product_id = ? " +
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

    public void insertFeedback(int accountId, int productId, int storeId, int rating, String content) {
        String sql = "INSERT INTO Feedback (account_id, product_id, store_id, rating, content, create_date) " +
                     "VALUES (?, ?, ?, ?, ?, GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, accountId);
            stm.setInt(2, productId);
            stm.setInt(3, storeId);
            stm.setInt(4, rating);
            stm.setString(5, content);
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
