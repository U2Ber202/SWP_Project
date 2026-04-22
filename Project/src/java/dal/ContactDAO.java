package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Contact;

public class ContactDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(ContactDAO.class.getName());

    private Contact mapContact(ResultSet rs) throws SQLException {
        Contact c = new Contact();
        c.setId(rs.getInt("id"));
        c.setAccountId(rs.getInt("account_id"));
        c.setOrderId(rs.getInt("order_id"));
        c.setStoreId(rs.getInt("store_id"));
        c.setMessage(rs.getString("message"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setStatus(rs.getString("status"));
        
        // Optional joint data or columns that might be missing in older DB versions
        try {
            c.setAccountName(rs.getString("fullname"));
        } catch (Exception e) {}
        try {
            c.setStoreName(rs.getString("store_name"));
        } catch (Exception e) {}
        try {
            c.setResponseMessage(rs.getString("response_message"));
        } catch (Exception e) {}
        try {
            c.setRespondedAt(rs.getTimestamp("responded_at"));
        } catch (Exception e) {}
        
        return c;
    }

    public List<Contact> getAllContacts() {
        return getBySql("SELECT c.*, a.fullname, s.store_name FROM Contact c "
                + "LEFT JOIN Account a ON c.account_id = a.uID "
                + "LEFT JOIN [Orders] o ON c.order_id = o.id "
                + "LEFT JOIN Store s ON (c.store_id = s.store_id OR (c.store_id = 0 AND o.store_id = s.store_id)) "
                + "ORDER BY c.created_at DESC");
    }

    public List<Contact> getContactsByStore(int storeId) {
        // Query based on the store linked to the contact OR the order for maximum reliability
        return getBySql("SELECT c.*, a.fullname, s.store_name FROM Contact c "
                + "LEFT JOIN Account a ON c.account_id = a.uID "
                + "LEFT JOIN [Orders] o ON c.order_id = o.id "
                + "LEFT JOIN Store s ON s.store_id = ? "
                + "WHERE (c.store_id = ? OR (c.store_id = 0 AND o.store_id = ?)) "
                + "ORDER BY c.created_at DESC", storeId, storeId, storeId);
    }

    public List<Contact> getContactsByAccount(int accountId) {
        return getBySql("SELECT c.*, a.fullname, s.store_name FROM Contact c "
                + "LEFT JOIN Account a ON c.account_id = a.uID "
                + "LEFT JOIN Store s ON c.store_id = s.store_id "
                + "WHERE c.account_id = ? "
                + "ORDER BY c.created_at DESC", accountId);
    }

    public void updateResponse(int id, String responseMessage) {
        executeUpdate("UPDATE Contact SET response_message = ?, responded_at = GETDATE(), status = N'Đã phản hồi' WHERE id = ?",
                responseMessage, id);
    }

    public void insert(Contact c) {
        executeUpdate("INSERT INTO Contact (account_id, order_id, store_id, message) VALUES (?, ?, ?, ?)",
                c.getAccountId(), c.getOrderId(), c.getStoreId(), c.getMessage());
    }

    public void updateStatus(int id, String status) {
        executeUpdate("UPDATE Contact SET status = ? WHERE id = ?", status, id);
    }

    public void delete(int id) {
        executeUpdate("DELETE FROM Contact WHERE id = ?", id);
    }

    private List<Contact> getBySql(String sql, Object... params) {
        List<Contact> list = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapContact(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
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
