package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Color;

public class ColorDAO extends DBContext {
    private static final Logger LOGGER = Logger.getLogger(ColorDAO.class.getName());

    public List<Color> getAll() {
        List<Color> list = new ArrayList<>();
        String sql = "SELECT * FROM Color ORDER BY color_name";
        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                list.add(new Color(rs.getInt("id"), rs.getString("color_name"), rs.getString("color_code")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
