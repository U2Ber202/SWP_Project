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
        String sql = "INSERT INTO [OrderDetail] ([order_id], [variant_id], [quantity], [productPrice]) VALUES (?,?,?,?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
                Cart cart = entry.getValue();
                stm.setInt(1, orderId);
                stm.setInt(2, cart.getVariant().getId());
                stm.setInt(3, cart.getQuantity());
                stm.setInt(4, cart.getVariant().getPrice());
                stm.addBatch();
            }
            stm.executeBatch();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public List<OrderDetail> getAllOrderDetailById(int id) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT od.*, p.name as product_name, v.image as product_image, c.color_name, v.size "
                + "FROM OrderDetail od "
                + "JOIN ProductVariant v ON od.variant_id = v.id "
                + "JOIN Product p ON v.product_id = p.id "
                + "LEFT JOIN Color c ON v.color_id = c.id "
                + "WHERE od.order_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    OrderDetail item = new OrderDetail();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setVariantId(rs.getInt("variant_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductImage(rs.getString("product_image"));
                    item.setProductPrice(rs.getInt("productPrice"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setColorName(rs.getString("color_name"));
                    item.setSize(rs.getString("size"));
                    orderDetails.add(item);
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
