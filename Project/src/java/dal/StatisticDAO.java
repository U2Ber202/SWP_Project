package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Satistic;

public class StatisticDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(StatisticDAO.class.getName());

    public Satistic getAll() {
        Satistic statistic = new Satistic();
        fillStatistic(statistic,
                "SELECT COUNT(DISTINCT o.id) AS totalOrders, ISNULL(SUM(o.totalPrice), 0) AS totalSales FROM Orders o JOIN Shipping s ON o.shipping_id = s.id "
                + "WHERE ((o.note LIKE '%VNPAY:%' AND CONVERT(date, o.create_date) = CONVERT(date, GETDATE())) "
                + "OR (s.Status = 'Shipped' AND CONVERT(date, s.shipped_date) = CONVERT(date, GETDATE())))");
        fillStatisticMonth(statistic,
                "SELECT COUNT(DISTINCT o.id) AS totalOrders, ISNULL(SUM(o.totalPrice), 0) AS totalSales FROM Orders o JOIN Shipping s ON o.shipping_id = s.id "
                + "WHERE ((o.note LIKE '%VNPAY:%' AND MONTH(o.create_date) = MONTH(GETDATE()) AND YEAR(o.create_date) = YEAR(GETDATE())) "
                + "OR (s.Status = 'Shipped' AND MONTH(s.shipped_date) = MONTH(GETDATE()) AND YEAR(s.shipped_date) = YEAR(GETDATE())))");
        return statistic;
    }

    public Satistic getForStore(int storeId) {
        Satistic statistic = new Satistic();
        fillStatistic(statistic,
                "SELECT COUNT(DISTINCT o.id) AS totalOrders, ISNULL(SUM(o.totalPrice), 0) AS totalSales FROM Orders o JOIN Shipping s ON o.shipping_id = s.id WHERE o.store_id = ? "
                + "AND ((o.note LIKE '%VNPAY:%' AND CONVERT(date, o.create_date) = CONVERT(date, GETDATE())) "
                + "OR (s.Status = 'Shipped' AND CONVERT(date, s.shipped_date) = CONVERT(date, GETDATE())))",
                storeId);
        fillStatisticMonth(statistic,
                "SELECT COUNT(DISTINCT o.id) AS totalOrders, ISNULL(SUM(o.totalPrice), 0) AS totalSales FROM Orders o JOIN Shipping s ON o.shipping_id = s.id WHERE o.store_id = ? "
                + "AND ((o.note LIKE '%VNPAY:%' AND MONTH(o.create_date) = MONTH(GETDATE()) AND YEAR(o.create_date) = YEAR(GETDATE())) "
                + "OR (s.Status = 'Shipped' AND MONTH(s.shipped_date) = MONTH(GETDATE()) AND YEAR(s.shipped_date) = YEAR(GETDATE())))",
                storeId);
        return statistic;
    }

    private void fillStatistic(Satistic statistic, String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    statistic.setTotalOrders(rs.getInt("totalOrders"));
                    statistic.setTotalSales(rs.getInt("totalSales"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void fillStatisticMonth(Satistic statistic, String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    statistic.setTotalOrdersMonth(rs.getInt("totalOrders"));
                    statistic.setTotalSalesMonth(rs.getInt("totalSales"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void bindParams(PreparedStatement stm, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stm.setObject(i + 1, params[i]);
        }
    }
}
