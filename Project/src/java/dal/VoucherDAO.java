package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Voucher;

public class VoucherDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(VoucherDAO.class.getName());

    private Voucher mapVoucher(ResultSet rs) throws SQLException {
        Voucher v = new Voucher();
        v.setId(rs.getInt("id"));
        v.setCode(rs.getString("code"));
        v.setDiscountPercent(rs.getInt("discount_percent"));
        v.setMaxDiscount(rs.getObject("max_discount") != null ? rs.getInt("max_discount") : null);
        v.setMinOrderValue(rs.getObject("min_order_value") != null ? rs.getInt("min_order_value") : null);
        v.setExpiryDate(rs.getString("expiry_date"));
        v.setStoreId(rs.getInt("store_id"));
        try {
            v.setStoreName(rs.getString("store_name"));
        } catch (SQLException ex) {
            v.setStoreName(null);
        }
        return v;
    }

    public List<Voucher> getAllVouchers() {
        String sql = "SELECT v.*, s.store_name FROM Voucher v LEFT JOIN Store s ON v.store_id = s.store_id ORDER BY v.expiry_date DESC, v.id DESC";
        return getVouchersBySql(sql);
    }

    public List<Voucher> getVouchersByStoreId(int storeId) {
        String sql = "SELECT v.*, s.store_name FROM Voucher v LEFT JOIN Store s ON v.store_id = s.store_id WHERE v.store_id = ? ORDER BY v.expiry_date DESC, v.id DESC";
        return getVouchersBySql(sql, storeId);
    }

    public Voucher getVoucherById(int id) {
        String sql = "SELECT v.*, s.store_name FROM Voucher v LEFT JOIN Store s ON v.store_id = s.store_id WHERE v.id = ?";
        List<Voucher> list = getVouchersBySql(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean addVoucher(Voucher v) {
        String sql = "INSERT INTO Voucher (code, discount_percent, max_discount, min_order_value, expiry_date, store_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, v.getCode());
            stm.setInt(2, v.getDiscountPercent());
            if (v.getMaxDiscount() != null) {
                stm.setInt(3, v.getMaxDiscount());
            } else {
                stm.setNull(3, java.sql.Types.INTEGER);
            }
            if (v.getMinOrderValue() != null) {
                stm.setInt(4, v.getMinOrderValue());
            } else {
                stm.setNull(4, java.sql.Types.INTEGER);
            }
            stm.setString(5, v.getExpiryDate());
            stm.setInt(6, v.getStoreId());
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean updateVoucher(Voucher v) {
        String sql = "UPDATE Voucher SET code = ?, discount_percent = ?, max_discount = ?, min_order_value = ?, expiry_date = ?, store_id = ? WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, v.getCode());
            stm.setInt(2, v.getDiscountPercent());
            if (v.getMaxDiscount() != null) {
                stm.setInt(3, v.getMaxDiscount());
            } else {
                stm.setNull(3, java.sql.Types.INTEGER);
            }
            if (v.getMinOrderValue() != null) {
                stm.setInt(4, v.getMinOrderValue());
            } else {
                stm.setNull(4, java.sql.Types.INTEGER);
            }
            stm.setString(5, v.getExpiryDate());
            stm.setInt(6, v.getStoreId());
            stm.setInt(7, v.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteVoucher(int id) {
        String sql = "DELETE FROM Voucher WHERE id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteVoucher(int id, int storeId) {
        String sql = "DELETE FROM Voucher WHERE id = ? AND store_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            stm.setInt(2, storeId);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Voucher getVoucherByCodeAndStoreId(String code, int storeId) {
        String sql = "SELECT * FROM Voucher WHERE code = ? AND (store_id = ? OR store_id = 0) AND expiry_date >= CAST(GETDATE() AS DATE)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, code);
            stm.setInt(2, storeId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapVoucher(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Voucher> getVouchersBySql(String sql, Object... params) {
        List<Voucher> list = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stm.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapVoucher(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
