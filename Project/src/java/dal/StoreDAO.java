package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Store;

public class StoreDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(StoreDAO.class.getName());

    private Store mapStore(ResultSet rs) throws SQLException {
        Store store = new Store(rs.getInt("store_id"), rs.getString("store_name"), rs.getInt("owner_id"));
        try {
            store.setShipperId(rs.getInt("shipper_id"));
        } catch (SQLException ex) {
            store.setShipperId(0);
        }
        try {
            store.setWarehouseManagerId(rs.getInt("warehouse_manager_id"));
        } catch (SQLException ex) {
            store.setWarehouseManagerId(0);
        }
        try {
            store.setProductCount(rs.getInt("product_count"));
        } catch (SQLException ex) {
            store.setProductCount(0);
        }
        try {
            store.setAverageRating(rs.getDouble("avg_rating"));
        } catch (SQLException ex) {
            store.setAverageRating(0.0);
        }
        try {
            store.setActive(rs.getBoolean("active"));
        } catch (SQLException ex) {
            store.setActive(true);
        }
        return store;
    }

    private static final String STORE_SELECT = "SELECT s.*, "
            + "(SELECT COUNT(*) FROM Product p WHERE p.store_id = s.store_id) as product_count, "
            + "(SELECT AVG(CAST(rating AS FLOAT)) FROM Feedback f WHERE f.store_id = s.store_id) as avg_rating "
            + "FROM Store s";

    public List<Store> getAllStores() {
        return getStoresBySql(STORE_SELECT);
    }

    public Store getStoreById(int id) {
        return getSingleStore(STORE_SELECT + " WHERE s.store_id = ?", id);
    }

    public Store getStoreByOwnerId(int ownerId) {
        return getSingleStore(STORE_SELECT + " WHERE s.owner_id = ?", ownerId);
    }

    public Store getStoreByWarehouseManagerId(int warehouseManagerId) {
        return getSingleStore(STORE_SELECT + " WHERE s.warehouse_manager_id = ?", warehouseManagerId);
    }

    public Store getStoreByShipperId(int shipperId) {
        return getSingleStore(STORE_SELECT + " WHERE s.shipper_id = ?", shipperId);
    }

    public boolean hasStoreByOwnerId(int ownerId) {
        return getStoreByOwnerId(ownerId) != null;
    }

    public boolean ownerAlreadyHasStore(int ownerId) {
        return getStoreByOwnerId(ownerId) != null;
    }

    public boolean warehouseManagerAlreadyHasStore(int warehouseManagerId) {
        return getStoreByWarehouseManagerId(warehouseManagerId) != null;
    }

    public boolean shipperAlreadyAssigned(int shipperId) {
        return getStoreByShipperId(shipperId) != null;
    }

    public boolean storeAlreadyHasShipper(int storeId) {
        return exists("SELECT 1 FROM Store WHERE store_id = ? AND shipper_id IS NOT NULL", storeId);
    }

    public boolean ownerAlreadyHasAnotherStore(int ownerId, int storeId) {
        return exists("SELECT 1 FROM Store WHERE owner_id = ? AND store_id <> ?", ownerId, storeId);
    }

    public boolean warehouseManagerAlreadyHasAnotherStore(int warehouseManagerId, int storeId) {
        return exists("SELECT 1 FROM Store WHERE warehouse_manager_id = ? AND store_id <> ?", warehouseManagerId, storeId);
    }

    public boolean shipperAlreadyAssignedToAnotherStore(int shipperId, int storeId) {
        return exists("SELECT 1 FROM Store WHERE shipper_id = ? AND store_id <> ?", shipperId, storeId);
    }

    public boolean insertStore(String name, int ownerId) {
        return insertStore(name, ownerId, null);
    }

    public boolean insertStore(String name, int ownerId, Integer warehouseManagerId) {
        return executeUpdate("INSERT INTO Store (store_name, owner_id, warehouse_manager_id, active) VALUES (?, ?, ?, 1)", name, ownerId, warehouseManagerId) > 0;
    }

    public boolean updateStore(int id, String name, int ownerId) {
        return updateStore(id, name, ownerId, null);
    }

    public boolean updateStore(int id, String name, int ownerId, Integer warehouseManagerId) {
        return executeUpdate("UPDATE Store SET store_name = ?, owner_id = ?, warehouse_manager_id = ? WHERE store_id = ?", name, ownerId, warehouseManagerId, id) > 0;
    }

    public boolean toggleStoreStatus(int storeId) {
        return executeUpdate("UPDATE Store SET active = active ^ 1 WHERE store_id = ?", storeId) > 0;
    }

    public boolean assignShipperToStore(int storeId, int shipperId) {
        return executeUpdate("UPDATE Store SET shipper_id = ? WHERE store_id = ?", shipperId, storeId) > 0;
    }

    public boolean assignWarehouseManagerToStore(int storeId, int warehouseManagerId) {
        return executeUpdate("UPDATE Store SET warehouse_manager_id = ? WHERE store_id = ?", warehouseManagerId, storeId) > 0;
    }

    public void deleteStore(int id) {
        executeUpdate("DELETE FROM Store WHERE store_id = ?", id);
    }

    private List<Store> getStoresBySql(String sql, Object... params) {
        List<Store> stores = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    stores.add(mapStore(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return stores;
    }

    private Store getSingleStore(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapStore(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean exists(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private int executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            return stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private void bindParams(PreparedStatement stm, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                stm.setNull(i + 1, java.sql.Types.INTEGER);
            } else {
                stm.setObject(i + 1, params[i]);
            }
        }
    }
}