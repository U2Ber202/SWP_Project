package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Category;

public class CategoryDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(CategoryDAO.class.getName());

    private Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCid(rs.getInt("cid"));
        category.setCname(rs.getString("cname"));
        category.setStoreId(rs.getInt("store_id"));
        category.setManufacturer(rs.getString("manufacturer"));
        return category;
    }

    public List<Category> getAllCategories() {
        return getCategoriesBySql("SELECT * FROM Category");
    }

    public List<Category> getCategoriesByStore(int storeId) {
        return getCategoriesBySql("SELECT * FROM Category WHERE store_id = ?", storeId);
    }

    public void insertCategory(String name, String manufacturer, int storeId) {
        String sql = "INSERT INTO [Category] ([cname], [manufacturer], [store_id]) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, name);
            stm.setString(2, manufacturer);
            if (storeId > 0) {
                stm.setInt(3, storeId);
            } else {
                stm.setNull(3, java.sql.Types.INTEGER);
            }
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public Category getCategoryById(int id) {
        return getSingleCategory("SELECT * FROM Category WHERE cid = ?", id);
    }

    public Category getCategoryByIdAndStore(int id, int storeId) {
        return getSingleCategory("SELECT * FROM Category WHERE cid = ? AND store_id = ?", id, storeId);
    }

    public void updateCategory(Category category) {
        executeUpdate("UPDATE [Category] SET [cname] = ?, [manufacturer] = ? WHERE cid = ?",
                category.getCname(), category.getManufacturer(), category.getCid());
    }

    public void updateCategoryByStore(Category category, int storeId) {
        executeUpdate("UPDATE [Category] SET [cname] = ?, [manufacturer] = ? WHERE cid = ? AND store_id = ?",
                category.getCname(), category.getManufacturer(), category.getCid(), storeId);
    }

    public void deleteCategoryById(int id) {
        executeUpdate("DELETE FROM Category WHERE cid = ?", id);
    }

    public void deleteCategoryByIdAndStore(int id, int storeId) {
        executeUpdate("DELETE FROM Category WHERE cid = ? AND store_id = ?", id, storeId);
    }

    private List<Category> getCategoriesBySql(String sql, Object... params) {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapCategory(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return categories;
    }

    private Category getSingleCategory(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapCategory(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            stm.executeUpdate();
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
