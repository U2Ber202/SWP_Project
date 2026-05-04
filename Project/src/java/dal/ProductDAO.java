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
import model.ProductVariant;

public class ProductDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    
    // Representative query for product listings (joins first variant found and aggregates sizes)
    private static final String PRODUCT_LIST_SELECT = "SELECT p.*, s.store_name, m.name as manufacturer_name, "
            + "v.price, v.image, v_qty.total_quantity, v_sz.sizes, v.color_name, v.first_size "
            + "FROM Product p "
            + "LEFT JOIN Store s ON p.store_id = s.store_id "
            + "LEFT JOIN Manufacturer m ON p.manufacturer_id = m.id "
            + "OUTER APPLY ( "
            + "    SELECT TOP 1 pv.price, pv.image, pv.size as first_size, c.color_name "
            + "    FROM ProductVariant pv "
            + "    LEFT JOIN Color c ON pv.color_id = c.id "
            + "    WHERE pv.product_id = p.id "
            + ") v "
            + "LEFT JOIN ( "
            + "    SELECT product_id, SUM(quantity) as total_quantity "
            + "    FROM ProductVariant GROUP BY product_id "
            + ") v_qty ON p.id = v_qty.product_id "
            + "LEFT JOIN ( "
            + "    SELECT product_id, STRING_AGG(CAST(size AS NVARCHAR(MAX)), ', ') as sizes "
            + "    FROM (SELECT DISTINCT product_id, size FROM ProductVariant) sz GROUP BY product_id "
            + ") v_sz ON p.id = v_sz.product_id";

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setCategoryId(rs.getInt("cateID"));
        product.setStoreId(rs.getInt("store_id"));
        product.setManufacturerId(rs.getInt("manufacturer_id"));
        product.setManufacturerName(rs.getString("manufacturer_name"));
        product.setStoreName(rs.getString("store_name"));
        
        // Populate representative data for UI listing (legacy fields)
        product.setPrice(rs.getInt("price"));
        product.setImageUrl(rs.getString("image"));
        product.setQuantity(rs.getInt("total_quantity"));
        product.setTiltle(rs.getString("sizes"));
        
        // Add a representative variant to the list for JSP compatibility (e.g. ${p.variants[0].size})
        String colorName = rs.getString("color_name");
        String firstSize = rs.getString("first_size");
        List<ProductVariant> vs = new ArrayList<>();
        if (colorName != null || firstSize != null) {
            ProductVariant v = new ProductVariant();
            v.setColorName(colorName);
            v.setSize(firstSize);
            vs.add(v);
        }
        product.setVariants(vs);
        
        return product;
    }

    private ProductVariant mapVariant(ResultSet rs) throws SQLException {
        ProductVariant v = new ProductVariant();
        v.setId(rs.getInt("id"));
        v.setProductId(rs.getInt("product_id"));
        v.setColorId(rs.getInt("color_id"));
        v.setSize(rs.getString("size"));
        v.setSku(rs.getString("sku"));
        v.setPrice(rs.getInt("price"));
        v.setQuantity(rs.getInt("quantity"));
        v.setImage(rs.getString("image"));
        
        // If color_name is available in result set (from a JOIN)
        try { v.setColorName(rs.getString("color_name")); } catch (SQLException e) {}
        try { v.setColorCode(rs.getString("color_code")); } catch (SQLException e) {}
        
        return v;
    }

    public List<Product> getAllProducts() {
        return getProductsBySql(PRODUCT_LIST_SELECT + " ORDER BY p.id DESC");
    }

    public Product getProductById(int productId) {
        Product p = getSingleProduct(PRODUCT_LIST_SELECT + " WHERE p.id = ?", productId);
        if (p != null) {
            p.setVariants(getVariantsByProductId(productId));
        }
        return p;
    }

    public List<ProductVariant> getVariantsByProductId(int productId) {
        List<ProductVariant> list = new ArrayList<>();
        String sql = "SELECT v.*, c.color_name, c.color_code FROM ProductVariant v "
                + "LEFT JOIN Color c ON v.color_id = c.id "
                + "WHERE v.product_id = ? ORDER BY v.size, c.color_name";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, productId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapVariant(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ProductVariant getVariantById(int variantId) {
        String sql = "SELECT v.*, c.color_name, c.color_code, p.name as product_name, p.store_id FROM ProductVariant v "
                + "JOIN Product p ON v.product_id = p.id "
                + "LEFT JOIN Color c ON v.color_id = c.id "
                + "WHERE v.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, variantId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    ProductVariant v = mapVariant(rs);
                    v.setProductName(rs.getString("product_name"));
                    v.setStoreId(rs.getInt("store_id"));
                    return v;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getTotalProducts() {
        return getCount("SELECT COUNT(id) FROM Product");
    }

    public int getTotalProductsByStoreId(int storeId) {
        return getCount("SELECT COUNT(id) FROM Product WHERE store_id = ?", storeId);
    }

    public List<Product> search(String keyword, int storeId) {
        String sql = PRODUCT_LIST_SELECT + " WHERE p.name LIKE ?" + (storeId > 0 ? " AND p.store_id = ?" : "") + " ORDER BY p.id DESC";
        return storeId > 0 ? getProductsBySql(sql, "%" + keyword + "%", storeId) : getProductsBySql(sql, "%" + keyword + "%");
    }

    public List<Product> getProductsByStoreId(int storeId) {
        return getProductsBySql(PRODUCT_LIST_SELECT + " WHERE p.store_id = ? ORDER BY p.id DESC", storeId);
    }

    public boolean reserveStock(int variantId, int amount) {
        return executeUpdate("UPDATE ProductVariant SET quantity = quantity - ? WHERE id = ? AND quantity >= ?", amount, variantId, amount) > 0;
    }

    public boolean releaseStock(int variantId, int amount) {
        return executeUpdate("UPDATE ProductVariant SET quantity = quantity + ? WHERE id = ?", amount, variantId) > 0;
    }

    // CRUD for Product
    public int insertProduct(Product p) {
        String sql = "INSERT INTO Product (name, description, cateID, store_id, manufacturer_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, p.getName());
            stm.setString(2, p.getDescription());
            stm.setInt(3, p.getCategoryId());
            stm.setInt(4, p.getStoreId());
            stm.setInt(5, p.getManufacturerId());
            stm.executeUpdate();
            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return -1;
    }


    public void insertVariant(ProductVariant v) {


        String sql = "INSERT INTO ProductVariant (product_id, color_id, size, sku, price, quantity, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, v.getProductId(), v.getColorId(), v.getSize(), v.getSku(), v.getPrice(), v.getQuantity(), v.getImage());
    }

    public List<Product> getAllNewProducts() {
        return getProductsBySql(PRODUCT_LIST_SELECT + " ORDER BY p.id DESC");
    }

    public void updateProduct(Product p) {
        String sql = "UPDATE Product SET name = ?, description = ?, cateID = ?, manufacturer_id = ? WHERE id = ?";
        executeUpdate(sql, p.getName(), p.getDescription(), p.getCategoryId(), p.getManufacturerId(), p.getId());
    }

    public java.util.Map<Integer, java.util.Map<Integer, java.util.Map<String, Integer>>> getSizeQuantitiesByStore(int storeId) {
        java.util.Map<Integer, java.util.Map<Integer, java.util.Map<String, Integer>>> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT v.product_id, v.color_id, v.size, v.quantity FROM ProductVariant v JOIN Product p ON v.product_id = p.id WHERE p.store_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, storeId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int pid = rs.getInt("product_id");
                    int cid = rs.getInt("color_id");
                    String size = rs.getString("size");
                    int qty = rs.getInt("quantity");
                    map.computeIfAbsent(pid, k -> new java.util.LinkedHashMap<>())
                       .computeIfAbsent(cid, k -> new java.util.LinkedHashMap<>())
                       .put(size, qty);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        return getProductsBySql(PRODUCT_LIST_SELECT + " WHERE p.cateID = ? ORDER BY p.id DESC", categoryId);
    }

    public List<Product> getProductsByCategoryIdAndStoreId(int categoryId, int storeId) {
        return getProductsBySql(PRODUCT_LIST_SELECT + " WHERE p.cateID = ? AND p.store_id = ? ORDER BY p.id DESC", categoryId, storeId);
    }

    public List<Product> getTopProductsByPriceAsc() {
        return getProductsBySql(PRODUCT_LIST_SELECT + " ORDER BY v.price ASC");
    }

    public List<Product> getTopProductsByPriceAsc(int storeId) {
        return getProductsBySql(PRODUCT_LIST_SELECT + " WHERE p.store_id = ? ORDER BY v.price ASC", storeId);
    }

    public List<Product> getTopProductsByPriceDesc() {
        return getProductsBySql(PRODUCT_LIST_SELECT + " ORDER BY v.price DESC");
    }

    public List<Product> getTopProductsByPriceDesc(int storeId) {
        return getProductsBySql(PRODUCT_LIST_SELECT + " WHERE p.store_id = ? ORDER BY v.price DESC", storeId);
    }

    public List<Product> getAllNewProductsByStoreId(int storeId) {
        return getProductsBySql(PRODUCT_LIST_SELECT + " WHERE p.store_id = ? ORDER BY p.id DESC", storeId);
    }


    public List<Product> getLatestProductsByStoreId(int storeId, int excludeProductId, int limit) {
        String sql = PRODUCT_LIST_SELECT + " WHERE p.store_id = ? AND p.id <> ? ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, storeId);
            stm.setInt(2, excludeProductId);
            stm.setMaxRows(limit);
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


    public Product getProductByIdAndStoreId(int productId, int storeId) {

        Product p = getSingleProduct(PRODUCT_LIST_SELECT + " WHERE p.id = ? AND p.store_id = ?", productId, storeId);
        if (p != null) {
            p.setVariants(getVariantsByProductId(productId));
        }
        return p;
    }

    public boolean deleteProduct(int id) {
        deleteRelatedRecords(id);
        return executeUpdate("DELETE FROM Product WHERE id = ?", id) > 0;
    }

    public boolean deleteProductByStore(int productId, int storeId) {
        Product p = getProductByIdAndStoreId(productId, storeId);
        if (p != null) {
            return deleteProduct(productId);
        }
        return false;
    }

    private void deleteRelatedRecords(int productId) {
        // Variants will be deleted by ON DELETE CASCADE in DB for table ProductVariant
        // But we need to clean up dependencies that don't have cascades
        executeUpdate("DELETE FROM Cart WHERE VariantID IN (SELECT id FROM ProductVariant WHERE product_id = ?)", productId);
        executeUpdate("DELETE FROM Feedback WHERE product_id = ?", productId);
        executeUpdate("UPDATE HomeSetting SET featured_product_id = NULL WHERE featured_product_id = ?", productId);
    }

    // Helper methods
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
                if (rs.next()) return rs.getInt(1);
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
