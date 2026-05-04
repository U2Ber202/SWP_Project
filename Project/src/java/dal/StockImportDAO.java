package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.StockImport;

public class StockImportDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(StockImportDAO.class.getName());

    private StockImport mapStockImport(ResultSet rs) throws SQLException {
        StockImport item = new StockImport();
        item.setId(rs.getInt("id"));
        item.setVariantId(rs.getInt("variant_id"));
        item.setStoreId(rs.getInt("store_id"));
        item.setProductName(rs.getString("product_name"));
        item.setColorName(rs.getString("color_name"));
        item.setSize(rs.getString("size"));
        item.setImportQuantity(rs.getInt("import_quantity"));
        item.setUnitCost(rs.getInt("unit_cost"));
        item.setBatchNumber(rs.getString("batch_number"));
        item.setNote(rs.getString("note"));
        item.setCreatedAt(rs.getString("created_at"));
        item.setCreatedDate(rs.getString("created_date"));
        item.setCreatedTime(rs.getString("created_time"));
        item.setCreatedByName(rs.getString("created_by_name"));
        return item;
    }

    public boolean addStockImport(int productId, int colorId, int storeId, int importQuantity, int unitCost, String batchNumber, String note, int createdBy, java.util.Map<String, Integer> sizeQuantities) {
        String insertHistorySql = "INSERT INTO StockImport (variant_id, store_id, import_quantity, unit_cost, batch_number, note, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateVariantSql = "UPDATE ProductVariant SET quantity = quantity + ? WHERE id = ?";
        String getVariantSql = "SELECT id FROM ProductVariant WHERE product_id = ? AND size = ? AND color_id = ?";
        String insertVariantSql = "INSERT INTO ProductVariant (product_id, size, color_id, quantity, price, image, sku) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String getTemplateSql = "SELECT TOP 1 price, image, sku FROM ProductVariant WHERE product_id = ?";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                // 1. Process each size in the import batch
                if (sizeQuantities != null) {
                    for (java.util.Map.Entry<String, Integer> entry : sizeQuantities.entrySet()) {
                        String size = entry.getKey();
                        int qty = entry.getValue();
                        if (qty > 0) {
                            int variantId = -1;
                            
                            // Find or Create variant
                            try (PreparedStatement getVarStm = connection.prepareStatement(getVariantSql)) {
                                getVarStm.setInt(1, productId);
                                getVarStm.setString(2, size);
                                getVarStm.setInt(3, colorId);
                                try (ResultSet rs = getVarStm.executeQuery()) {
                                    if (rs.next()) {
                                        variantId = rs.getInt("id");
                                    }
                                }
                            }
                            
                            if (variantId == -1) {
                                // Variant doesn't exist, create it
                                int price = 0;
                                String image = null;
                                String sku = null;
                                try (PreparedStatement templateStm = connection.prepareStatement(getTemplateSql)) {
                                    templateStm.setInt(1, productId);
                                    try (ResultSet rs = templateStm.executeQuery()) {
                                        if (rs.next()) {
                                            price = rs.getInt("price");
                                            image = rs.getString("image");
                                            sku = rs.getString("sku");
                                        }
                                    }
                                }
                                try (PreparedStatement insertVarStm = connection.prepareStatement(insertVariantSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                    insertVarStm.setInt(1, productId);
                                    insertVarStm.setString(2, size);
                                    insertVarStm.setInt(3, colorId);
                                    insertVarStm.setInt(4, 0); // Start with 0, will update below
                                    insertVarStm.setInt(5, price);
                                    insertVarStm.setString(6, image);
                                    insertVarStm.setString(7, sku);
                                    insertVarStm.executeUpdate();
                                    try (ResultSet rs = insertVarStm.getGeneratedKeys()) {
                                        if (rs.next()) variantId = rs.getInt(1);
                                    }
                                }
                            }
                            
                            if (variantId != -1) {
                                // 2. Update variant quantity
                                try (PreparedStatement updateStm = connection.prepareStatement(updateVariantSql)) {
                                    updateStm.setInt(1, qty);
                                    updateStm.setInt(2, variantId);
                                    updateStm.executeUpdate();
                                }
                                
                                // 3. Record import history for THIS variant
                                try (PreparedStatement insertHistory = connection.prepareStatement(insertHistorySql)) {
                                    insertHistory.setInt(1, variantId);
                                    insertHistory.setInt(2, storeId);
                                    insertHistory.setInt(3, qty);
                                    insertHistory.setInt(4, unitCost);
                                    insertHistory.setString(5, batchNumber);
                                    insertHistory.setString(6, note);
                                    insertHistory.setInt(7, createdBy);
                                    insertHistory.executeUpdate();
                                }
                            }
                        }
                    }
                }

                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "Error in addStockImport transaction", ex);
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database connection error in addStockImport", ex);
        }
        return false;
    }

    public List<StockImport> getStockImportsByStoreId(int storeId) {
        String sql = "SELECT si.*, p.name AS product_name, c.color_name, v.size, "
                + "CONVERT(VARCHAR(19), si.created_at, 120) AS created_at, "
                + "CONVERT(VARCHAR(10), si.created_at, 23) AS created_date, "
                + "CONVERT(VARCHAR(5), si.created_at, 108) AS created_time, "
                + "ISNULL(a.fullname, a.[user]) AS created_by_name "
                + "FROM StockImport si "
                + "LEFT JOIN ProductVariant v ON si.variant_id = v.id "
                + "LEFT JOIN Product p ON v.product_id = p.id "
                + "LEFT JOIN Color c ON v.color_id = c.id "
                + "LEFT JOIN Account a ON a.uID = si.created_by "
                + "WHERE si.store_id = ? "
                + "ORDER BY si.created_at DESC, si.id DESC";
        return getStockImportsBySql(sql, storeId);
    }

    public List<StockImport> getDailyStockSummaryByStoreId(int storeId) {
        String sql = "SELECT MIN(si.id) AS id, 0 AS variant_id, si.store_id, "
                + "N'Tổng nhập trong ngày' AS product_name, NULL as color_name, NULL as size, "
                + "SUM(si.import_quantity) AS import_quantity, "
                + "0 as unit_cost, CAST('' AS NVARCHAR) as batch_number, "
                + "CAST(NULL AS NVARCHAR(255)) AS note, "
                + "CONVERT(VARCHAR(19), MAX(si.created_at), 120) AS created_at, "
                + "CONVERT(VARCHAR(10), si.created_at, 23) AS created_date, "
                + "CONVERT(VARCHAR(5), MAX(si.created_at), 108) AS created_time, "
                + "CAST(NULL AS NVARCHAR(255)) AS created_by_name "
                + "FROM StockImport si "
                + "WHERE si.store_id = ? "
                + "GROUP BY si.store_id, CONVERT(VARCHAR(10), si.created_at, 23) "
                + "ORDER BY created_date DESC";
        return getStockImportsBySql(sql, storeId);
    }

    private List<StockImport> getStockImportsBySql(String sql, Object... params) {
        List<StockImport> items = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    items.add(mapStockImport(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return items;
    }

    private void bindParams(PreparedStatement stm, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stm.setObject(i + 1, params[i]);
        }
    }
}
