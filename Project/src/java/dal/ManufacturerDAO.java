package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Manufacturer;

public class ManufacturerDAO extends DBContext {
    private static final Logger LOGGER = Logger.getLogger(ManufacturerDAO.class.getName());

    public List<Manufacturer> getAll() {
        List<Manufacturer> list = new ArrayList<>();
        String sql = "SELECT * FROM Manufacturer ORDER BY name";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                list.add(new Manufacturer(rs.getInt("id"), rs.getString("name"), rs.getString("country")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
