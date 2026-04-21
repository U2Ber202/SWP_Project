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
import model.Order;

public class OrderDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setAccountId(rs.getInt("account_id"));
        order.setTotalPrice(rs.getInt("totalPrice"));
        order.setNote(rs.getString("note"));
        order.setCreatedDate(rs.getString("create_date"));
        order.setShippingId(rs.getInt("shipping_id"));
        order.setStoreId(rs.getInt("store_id"));
        try {
            order.setStatus(rs.getInt("status"));
        } catch (Exception e) {}
        return order;
    }

    public List<Order> getOrdersByAccountId(int accountId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Orders] WHERE account_id = ? ORDER BY id DESC";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, accountId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return orders;
    }

    public int createReturnId(Order order) {
        String sql = "INSERT INTO [Orders] ([account_id], [totalPrice], [note], [shipping_id], [store_id]) VALUES (?,?,?,?,?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, order.getAccountId());
            stm.setInt(2, order.getTotalPrice());
            stm.setString(3, order.getNote());
            stm.setInt(4, order.getShippingId());
            if (order.getStoreId() > 0) {
                stm.setInt(5, order.getStoreId());
            } else {
                stm.setNull(5, java.sql.Types.INTEGER);
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

    public List<Order> getAllOrder() {
        return getOrdersBySql("SELECT * FROM [Orders] ORDER BY id DESC");
    }

    public List<Order> getOrdersByStoreId(int storeId) {
        return getOrdersBySql("SELECT * FROM [Orders] WHERE store_id = ? ORDER BY id DESC", storeId);
    }

    public List<Order> getOrdersByShipperId(int shipperId) {
        return getOrdersBySql("SELECT o.* FROM [Orders] o "
                + "INNER JOIN [Shipping] s ON o.shipping_id = s.id "
                + "WHERE s.shipper_id = ? "
                + "ORDER BY o.id DESC", shipperId);
    }

    public Order getOrderById(int orderId) {
        return getSingleOrder("SELECT * FROM [Orders] WHERE id = ?", orderId);
    }

    public Order getOrderByIdAndStoreId(int orderId, int storeId) {
        return getSingleOrder("SELECT * FROM [Orders] WHERE id = ? AND store_id = ?", orderId, storeId);
    }

    public Order getOrderByIdAndShipperId(int orderId, int shipperId) {
        return getSingleOrder("SELECT o.* FROM [Orders] o "
                + "INNER JOIN [Shipping] s ON o.shipping_id = s.id "
                + "WHERE o.id = ? AND s.shipper_id = ?", orderId, shipperId);
    }

    private List<Order> getOrdersBySql(String sql, Object... params) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return orders;
    }

    private Order getSingleOrder(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
