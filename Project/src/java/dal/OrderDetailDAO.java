package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cart;
import model.OrderDetail;

public class OrderDetailDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(OrderDetailDAO.class.getName());

    public void saveCart(int orderId, Map<Integer, Cart> carts) {
        String sql = "INSERT INTO [OrderDetail] ([order_id], [productName], [productImage], [productPrice], [quantity]) VALUES (?,?,?,?,?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
                Cart cart = entry.getValue();
                stm.setInt(1, orderId);
                stm.setString(2, cart.getProduct().getName());
                stm.setString(3, cart.getProduct().getImageUrl());
                stm.setInt(4, cart.getProduct().getPrice());
                stm.setInt(5, cart.getQuantity());
                stm.addBatch();
            }
            stm.executeBatch();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public List<OrderDetail> getAllOrderDetailById(int id) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE order_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    OrderDetail order = new OrderDetail();
                    order.setId(rs.getInt(1));
                    order.setOrderId(rs.getInt(2));
                    order.setProductName(rs.getString(3));
                    order.setProductImage(rs.getString(4));
                    order.setProductPrice(rs.getInt(5));
                    order.setQuantity(rs.getInt(6));
                    orderDetails.add(order);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return orderDetails;
    }

    public void delete(int id) {
        String sql = "DELETE FROM [OrderDetail] WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
