package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;

public class ProductDAO extends DBContext {
    
    public boolean isProductNameExist(String name, int storeId, int excludeId) {
        String sql = "SELECT COUNT(*) FROM Product WHERE name = ? AND store_id = ? AND id <> ?";
        return getCount(sql, name, storeId, excludeId) > 0;
    }

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final String PRODUCT_SELECT = "SELECT p.*, s.store_name "
            + "FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id";

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setImageUrl(rs.getString("image"));
        product.setPrice(rs.getInt("price"));
        product.setTiltle(rs.getString("title"));
        product.setDescription(rs.getString("description"));
        product.setCategoryId(rs.getInt("cateID"));
        product.setSell_ID(rs.getInt("sell_ID"));
        product.setStoreId(rs.getInt("store_id"));
        product.setQuantity(rs.getInt("quantity"));
        product.setStoreName(rs.getString("store_name"));
        product.setManufacturer(rs.getString("manufacturer"));
        return product;
    }

    public List<Product> getAllProducts() {
        return getProductsBySql(PRODUCT_SELECT + " ORDER BY p.id DESC");
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        return getProductsBySql(PRODUCT_SELECT + " WHERE p.cateID = ? ORDER BY p.id DESC", categoryId);
    }

    public List<Product> getProductsByCategoryIdAndStoreId(int categoryId, int storeId) {
        return getProductsBySql(PRODUCT_SELECT + " WHERE p.cateID = ? AND p.store_id = ? ORDER BY p.id DESC", categoryId, storeId);
    }

    public List<Product> getProductsWithPagging(int page, int pageSize) {
        return getProductsBySql(PRODUCT_SELECT + " ORDER BY p.id DESC OFFSET (?-1)*? ROWS FETCH NEXT ? ROWS ONLY", page, pageSize, pageSize);
    }

    public List<Product> getProductsWithPagingByStoreId(int storeId, int page, int pageSize) {
        return getProductsBySql(PRODUCT_SELECT + " WHERE p.store_id = ? ORDER BY p.id DESC OFFSET (?-1)*? ROWS FETCH NEXT ? ROWS ONLY", storeId, page, pageSize, pageSize);
    }

    public int getTotalProducts() {
        return getCount("SELECT COUNT(id) FROM Product");
    }

    public int getTotalProductsByStoreId(int storeId) {
        return getCount("SELECT COUNT(id) FROM Product WHERE store_id = ?", storeId);
    }

    public List<Product> search(String keyword) {
        return search(keyword, 0);
    }

    public List<Product> search(String keyword, int storeId) {
        String sql = PRODUCT_SELECT + " WHERE p.name LIKE ?" + (storeId > 0 ? " AND p.store_id = ?" : "") + " ORDER BY p.id DESC";
        return storeId > 0 ? getProductsBySql(sql, "%" + keyword + "%", storeId) : getProductsBySql(sql, "%" + keyword + "%");
    }

    public Product getProductById(int productId) {
        return getSingleProduct(PRODUCT_SELECT + " WHERE p.id = ?", productId);
    }

    public Product getProductByIdAndStoreId(int productId, int storeId) {
        return getSingleProduct(PRODUCT_SELECT + " WHERE p.id = ? AND p.store_id = ?", productId, storeId);
    }

    public List<Product> getProductsBySellId(int id) {
        return getProductsBySql(PRODUCT_SELECT + " WHERE p.sell_ID = ? ORDER BY p.id DESC", id);
    }

    public void insertProduct(String name, String img, int price, String title, String description, String manufacturer, int cid, int quantity, int sellId, int storeId) {
        String sql = "INSERT INTO [Product] ([name], [image], [price], [title], [description], [manufacturer], [cateID], [quantity], [sell_ID], [store_id]) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, img);
            stm.setInt(3, price);
            stm.setString(4, title);
            stm.setString(5, description);
            stm.setString(6, manufacturer);
            stm.setInt(7, cid);
            stm.setInt(8, quantity);
            stm.setInt(9, sellId);
            if (storeId > 0) {
                stm.setInt(10, storeId);
            } else {
                stm.setNull(10, java.sql.Types.INTEGER);
            }
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public boolean deleteProduct(int id) {
        deleteRelatedRecords(id);
        return executeUpdate("DELETE FROM [Product] WHERE id = ?", id) > 0;
    }

    public boolean deleteProductByStore(int id, int storeId) {
        deleteRelatedRecords(id);
        return executeUpdate("DELETE FROM [Product] WHERE id = ? AND store_id = ?", id, storeId) > 0;
    }

    private void deleteRelatedRecords(int productId) {
        executeUpdate("DELETE FROM [Cart] WHERE [ProductID] = ?", productId);
        executeUpdate("DELETE FROM [Feedback] WHERE [product_id] = ?", productId);
        executeUpdate("DELETE FROM [StockImport] WHERE [product_id] = ?", productId);
        executeUpdate("DELETE FROM [ProductSize] WHERE [product_id] = ?", productId);
        executeUpdate("UPDATE [HomeSetting] SET [featured_product_id] = NULL WHERE [featured_product_id] = ?", productId);
    }

    public void updateProduct(String name, String img, String price, String title, String description, String manufacturer, String cid, String quantity, String id) {
        executeUpdate("UPDATE [Product] SET [name] = ?, [image] = ?, [price] = ?, [title] = ?, [description] = ?, [manufacturer] = ?, [cateID] = ?, [quantity] = ? WHERE id = ?",
                name, img, price, title, description, manufacturer, cid, quantity, id);
    }

    public void updateProductByStore(String name, String img, int price, String title, String description, String manufacturer, int cid, int quantity, int id, int storeId) {
        executeUpdate("UPDATE [Product] SET [name] = ?, [image] = ?, [price] = ?, [title] = ?, [description] = ?, [manufacturer] = ?, [cateID] = ?, [quantity] = ? WHERE id = ? AND store_id = ?",
                name, img, price, title, description, manufacturer, cid, quantity, id, storeId);
    }

    public List<Product> getAllProductsLast() {
        return getProductsBySql("SELECT TOP 4 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id ORDER BY p.ID ASC");
    }

    public List<Product> getLatestProductsByStoreId(int storeId, int excludeProductId, int limit) {
        StringBuilder sql = new StringBuilder("SELECT TOP ").append(limit)
                .append(" p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id ")
                .append("WHERE p.store_id = ? ");
        List<Object> params = new ArrayList<>();
        params.add(storeId);
        if (excludeProductId > 0) {
            sql.append("AND p.id <> ? ");
            params.add(excludeProductId);
        }
        sql.append("ORDER BY p.ID DESC");
        return getProductsBySql(sql.toString(), params.toArray());
    }

    public List<Product> getAllNewProducts() {
        return getProductsBySql("SELECT TOP 4 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id ORDER BY p.ID DESC");
    }

    public List<Product> getAllNewProductsByStoreId(int storeId) {
        return getProductsBySql("SELECT TOP 4 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id WHERE p.store_id = ? ORDER BY p.ID DESC", storeId);
    }

    public List<Product> getTopProductsByPriceDesc() {
        return getProductsBySql("SELECT TOP 4 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id ORDER BY p.price DESC, p.id DESC");
    }

    public List<Product> getTopProductsByPriceAsc() {
        return getProductsBySql("SELECT TOP 4 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id ORDER BY p.price ASC, p.id DESC");
    }

    public List<Product> getTopProductsByPriceDesc(int storeId) {
        return getProductsBySql("SELECT TOP 4 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id WHERE p.store_id = ? ORDER BY p.price DESC, p.id DESC", storeId);
    }

    public List<Product> getTopProductsByPriceAsc(int storeId) {
        return getProductsBySql("SELECT TOP 4 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id WHERE p.store_id = ? ORDER BY p.price ASC, p.id DESC", storeId);
    }

    public List<Product> getNewProductsByCateID(int categoryId) {
        return getProductsBySql("SELECT TOP 2 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id WHERE p.cateID = ? ORDER BY p.ID DESC", categoryId);
    }

    public List<Product> getNewProductsByCateIDAndStoreId(int categoryId, int storeId) {
        return getProductsBySql("SELECT TOP 2 p.*, s.store_name FROM Product p LEFT JOIN Store s ON p.store_id = s.store_id WHERE p.cateID = ? AND p.store_id = ? ORDER BY p.ID DESC", categoryId, storeId);
    }

    public List<Product> getProductsByStoreId(int storeId) {
        return getProductsBySql(PRODUCT_SELECT + " WHERE p.store_id = ? ORDER BY p.id DESC", storeId);
    }

    public boolean reserveStock(int productId, int amount) {
        return executeUpdate("UPDATE Product SET quantity = quantity - ? WHERE id = ? AND quantity >= ?", amount, productId, amount) > 0;
    }

    public boolean releaseStock(int productId, int amount) {
        return executeUpdate("UPDATE Product SET quantity = quantity + ? WHERE id = ?", amount, productId) > 0;
    }

    public java.util.Map<String, Integer> getProductSizeQuantities(int productId) {
        java.util.Map<String, Integer> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT size, quantity FROM ProductSize WHERE product_id = ? ORDER BY size";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, productId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("size"), rs.getInt("quantity"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public java.util.Map<Integer, java.util.Map<String, Integer>> getSizeQuantitiesByStore(int storeId) {
        java.util.Map<Integer, java.util.Map<String, Integer>> storeMap = new java.util.HashMap<>();
        String sql = "SELECT ps.product_id, ps.size, ps.quantity FROM ProductSize ps "
                + "INNER JOIN Product p ON ps.product_id = p.id WHERE p.store_id = ?";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, storeId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int pid = rs.getInt("product_id");
                    String size = rs.getString("size");
                    int qty = rs.getInt("quantity");
                    storeMap.computeIfAbsent(pid, k -> new java.util.LinkedHashMap<>()).put(size, qty);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return storeMap;
    }

    private List<Product> getProductsBySql(String sql, Object... params) {
        List<Product> list = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private Product getSingleProduct(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private int getCount(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return 0;
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
            stm.setObject(i + 1, params[i]);
        }
    }
}
