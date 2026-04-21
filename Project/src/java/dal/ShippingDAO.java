package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Shipping;

public class ShippingDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(ShippingDAO.class.getName());

    public int createReturnId(Shipping shipping) {
        String sql = "INSERT INTO [Shipping] ([name], [phone], [address], [Status], [store_id]) VALUES (?,?,?,'Pending',?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, shipping.getName());
            stm.setString(2, shipping.getPhone());
            stm.setString(3, shipping.getAddress());
            if (shipping.getStoreId() > 0) {
                stm.setInt(4, shipping.getStoreId());
            } else {
                stm.setNull(4, java.sql.Types.INTEGER);
            }
            stm.executeUpdate();
            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Shipping> getAllShipping() {
        List<Shipping> shippingList = new ArrayList<>();
        String sql = "SELECT s.*, ISNULL(a.fullname, a.[user]) AS shipper_name FROM Shipping s LEFT JOIN Account a ON a.uID = s.shipper_id";
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                shippingList.add(mapShipping(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return shippingList;
    }

    public Integer getStoreIdByShipperId(int shipperId) {
        String sql = "SELECT TOP 1 store_id FROM Shipping WHERE shipper_id = ? AND store_id IS NOT NULL ORDER BY id DESC";
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, shipperId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("store_id");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updateStatusByStore(int id, int storeId, String status) {
        if ("Shipped".equalsIgnoreCase(status)) {
            executeUpdate("UPDATE Shipping SET status = ?, shipped_date = GETDATE() WHERE id = ? AND store_id = ?", status, id, storeId);
        } else {
            executeUpdate("UPDATE Shipping SET status = ?, shipped_date = NULL WHERE id = ? AND store_id = ?", status, id, storeId);
        }
    }

    public void updateStatus(int id, String status) {
        if ("Shipped".equalsIgnoreCase(status)) {
            executeUpdate("UPDATE Shipping SET status = ?, shipped_date = GETDATE() WHERE id = ?", status, id);
        } else {
            executeUpdate("UPDATE Shipping SET status = ?, shipped_date = NULL WHERE id = ?", status, id);
        }
    }

    public void updateStatusByShipper(int id, int shipperId, String status) {
        if ("Shipped".equalsIgnoreCase(status)) {
            executeUpdate("UPDATE Shipping SET status = ?, shipped_date = GETDATE() WHERE id = ? AND shipper_id = ?", status, id, shipperId);
        } else {
            executeUpdate("UPDATE Shipping SET status = ?, shipped_date = NULL WHERE id = ? AND shipper_id = ?", status, id, shipperId);
        }
    }

    public Shipping getShippingByOrderId(int orderId) {
        return getSingleShipping("SELECT s.*, ISNULL(a.fullname, a.[user]) AS shipper_name FROM Shipping s INNER JOIN Orders o ON o.shipping_id = s.id LEFT JOIN Account a ON a.uID = s.shipper_id WHERE o.id = ?", orderId);
    }

    public Shipping getShippingByOrderIdAndStoreId(int orderId, int storeId) {
        return getSingleShipping("SELECT s.*, ISNULL(a.fullname, a.[user]) AS shipper_name FROM Shipping s INNER JOIN Orders o ON o.shipping_id = s.id LEFT JOIN Account a ON a.uID = s.shipper_id WHERE o.id = ? AND o.store_id = ? AND s.store_id = ?",
                orderId, storeId, storeId);
    }

    public Shipping getShippingByOrderIdAndShipperId(int orderId, int shipperId) {
        return getSingleShipping("SELECT s.*, ISNULL(a.fullname, a.[user]) AS shipper_name FROM Shipping s INNER JOIN Orders o ON o.shipping_id = s.id LEFT JOIN Account a ON a.uID = s.shipper_id WHERE o.id = ? AND s.shipper_id = ?",
                orderId, shipperId);
    }

    public boolean assignShipperByStore(int shippingId, int storeId, int shipperId) {
        return executeUpdate("UPDATE Shipping SET shipper_id = ? WHERE id = ? AND store_id = ? AND ISNULL(status, 'Pending') <> 'Shipped'", shipperId, shippingId, storeId) > 0;
    }

    private Shipping getSingleShipping(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapShipping(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Shipping mapShipping(ResultSet rs) throws SQLException {
        Shipping shipping = new Shipping();
        shipping.setId(rs.getInt("id"));
        shipping.setName(rs.getString("name"));
        shipping.setPhone(rs.getString("phone"));
        shipping.setAddress(rs.getString("address"));
        shipping.setStatus(rs.getString("status"));
        shipping.setStoreId(rs.getInt("store_id"));
        shipping.setShipperId(rs.getInt("shipper_id"));
        shipping.setShippedDate(rs.getString("shipped_date"));
        try {
            shipping.setShipperName(rs.getString("shipper_name"));
        } catch (SQLException ex) {
            shipping.setShipperName(null);
        }
        return shipping;
    }

    private int executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            bindParams(ps, params);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private void bindParams(PreparedStatement stm, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stm.setObject(i + 1, params[i]);
        }
    }
}