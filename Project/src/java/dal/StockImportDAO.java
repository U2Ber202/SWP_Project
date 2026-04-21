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
        item.setProductId(rs.getInt("product_id"));
        item.setStoreId(rs.getInt("store_id"));
        item.setProductName(rs.getString("product_name"));
        item.setImportQuantity(rs.getInt("import_quantity"));
        item.setNote(rs.getString("note"));
        item.setCreatedAt(rs.getString("created_at"));
        item.setCreatedDate(rs.getString("created_date"));
        item.setCreatedTime(rs.getString("created_time"));
        item.setCreatedByName(rs.getString("created_by_name"));
        return item;
    }

    public boolean addStockImport(int productId, int storeId, int importQuantity, String note, int createdBy, java.util.Map<String, Integer> sizeQuantities) {
        String insertHistorySql = "INSERT INTO StockImport (product_id, store_id, import_quantity, note, created_by) VALUES (?, ?, ?, ?, ?)";
        String updateProductSql = "UPDATE Product SET quantity = quantity + ? WHERE id = ? AND store_id = ?";
        String upsertSizeSql = "IF EXISTS (SELECT 1 FROM ProductSize WHERE product_id = ? AND size = ?) "
                + "UPDATE ProductSize SET quantity = quantity + ? WHERE product_id = ? AND size = ? "
                + "ELSE INSERT INTO ProductSize (product_id, size, quantity) VALUES (?, ?, ?)";
        
        String ensureTableSql = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'ProductSize') "
                + "CREATE TABLE [dbo].[ProductSize] ("
                + "[product_id] INT NOT NULL, "
                + "[size] NVARCHAR(50) NOT NULL, "
                + "[quantity] INT NOT NULL DEFAULT 0, "
                + "CONSTRAINT [PK_ProductSize] PRIMARY KEY ([product_id], [size]), "
                + "CONSTRAINT [FK_ProductSize_Product] FOREIGN KEY ([product_id]) REFERENCES [dbo].[Product]([id]) ON DELETE CASCADE"
                + ")";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ensureTable = connection.prepareStatement(ensureTableSql);
                    PreparedStatement updateProduct = connection.prepareStatement(updateProductSql);
                    PreparedStatement insertHistory = connection.prepareStatement(insertHistorySql);
                    PreparedStatement upsertSize = connection.prepareStatement(upsertSizeSql)) {
                
                // 0. Ensure table exists
                ensureTable.executeUpdate();
                updateProduct.setInt(1, importQuantity);
                updateProduct.setInt(2, productId);
                updateProduct.setInt(3, storeId);
                int updatedRows = updateProduct.executeUpdate();
                if (updatedRows == 0) {
                    connection.rollback();
                    return false;
                }

                // 2. Update size-specific quantities
                if (sizeQuantities != null) {
                    for (java.util.Map.Entry<String, Integer> entry : sizeQuantities.entrySet()) {
                        String size = entry.getKey();
                        int qty = entry.getValue();
                        if (qty > 0) {
                            upsertSize.setInt(1, productId);
                            upsertSize.setString(2, size);
                            upsertSize.setInt(3, qty);
                            upsertSize.setInt(4, productId);
                            upsertSize.setString(5, size);
                            upsertSize.setInt(6, productId);
                            upsertSize.setString(7, size);
                            upsertSize.setInt(8, qty);
                            upsertSize.addBatch();
                        }
                    }
                    upsertSize.executeBatch();
                }

                // 3. Save import history
                insertHistory.setInt(1, productId);
                insertHistory.setInt(2, storeId);
                insertHistory.setInt(3, importQuantity);
                insertHistory.setString(4, note);
                insertHistory.setInt(5, createdBy);
                insertHistory.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<StockImport> getStockImportsByStoreId(int storeId) {
        String sql = "SELECT si.id, si.product_id, si.store_id, p.name AS product_name, si.import_quantity, "
                + "si.note, CONVERT(VARCHAR(19), si.created_at, 120) AS created_at, "
                + "CONVERT(VARCHAR(10), si.created_at, 23) AS created_date, "
                + "CONVERT(VARCHAR(5), si.created_at, 108) AS created_time, "
                + "ISNULL(a.fullname, a.[user]) AS created_by_name "
                + "FROM StockImport si "
                + "INNER JOIN Product p ON p.id = si.product_id "
                + "LEFT JOIN Account a ON a.uID = si.created_by "
                + "WHERE si.store_id = ? "
                + "ORDER BY si.created_at DESC, si.id DESC";
        return getStockImportsBySql(sql, storeId);
    }

    public List<StockImport> getDailyStockSummaryByStoreId(int storeId) {
        String sql = "SELECT MIN(si.id) AS id, 0 AS product_id, si.store_id, "
                + "N'Tong nhap trong ngay' AS product_name, "
                + "SUM(si.import_quantity) AS import_quantity, "
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
