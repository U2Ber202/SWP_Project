package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.StaffActionHistory;

public class StaffActionHistoryDAO extends DBContext {

    public void insert(StaffActionHistory h) {
        String sql = "INSERT INTO StaffActionHistory (owner_id, staff_id, action_type, details) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, h.getOwnerId());
            stm.setInt(2, h.getStaffId());
            stm.setString(3, h.getActionType());
            stm.setString(4, h.getDetails());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StaffActionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<StaffActionHistory> getHistoryByOwner(int ownerId) {
        List<StaffActionHistory> list = new ArrayList<>();
        String sql = "SELECT h.*, a.fullname as staff_name, a.role as staff_role \n" +
                     "FROM StaffActionHistory h \n" +
                     "JOIN Account a ON h.staff_id = a.uID \n" +
                     "WHERE h.owner_id = ? \n" +
                     "ORDER BY h.action_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, ownerId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    StaffActionHistory h = new StaffActionHistory();
                    h.setId(rs.getInt("id"));
                    h.setOwnerId(rs.getInt("owner_id"));
                    h.setStaffId(rs.getInt("staff_id"));
                    h.setActionType(rs.getString("action_type"));
                    h.setDetails(rs.getString("details"));
                    h.setActionAt(rs.getTimestamp("action_at"));
                    h.setStaffName(rs.getString("staff_name"));
                    h.setStaffRole(rs.getString("staff_role"));
                    list.add(h);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffActionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean hasHistory(int ownerId, int staffId) {
        String sql = "SELECT 1 FROM StaffActionHistory WHERE owner_id = ? AND staff_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, ownerId);
            stm.setInt(2, staffId);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffActionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
