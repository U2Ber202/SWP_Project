package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Slider;

public class SliderDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(SliderDAO.class.getName());

    public List<Slider> getAllSliders() {
        List<Slider> list = new ArrayList<>();
        String sql = "SELECT * FROM Slider ORDER BY id DESC";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                list.add(mapSlider(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Slider> getActiveSliders() {
        List<Slider> list = new ArrayList<>();
        String sql = "SELECT * FROM Slider WHERE status = 1 ORDER BY id DESC";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                list.add(mapSlider(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Slider getSliderById(int id) {
        String sql = "SELECT * FROM Slider WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapSlider(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean addSlider(Slider slider) {
        String sql = "INSERT INTO Slider (title, image_url, product_id, status, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, slider.getTitle());
            stm.setString(2, slider.getImageUrl());
            stm.setInt(3, slider.getProductId());
            stm.setBoolean(4, slider.isStatus());
            stm.setString(5, slider.getDescription());

            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean updateSlider(Slider slider) {
        String sql = "UPDATE Slider SET title = ?, image_url = ?, product_id = ?, status = ?, description = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, slider.getTitle());
            stm.setString(2, slider.getImageUrl());
            stm.setInt(3, slider.getProductId());
            stm.setBoolean(4, slider.isStatus());
            stm.setString(5, slider.getDescription());

            stm.setInt(6, slider.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteSlider(int id) {
        String sql = "DELETE FROM Slider WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private Slider mapSlider(ResultSet rs) throws SQLException {
        Slider slider = new Slider();
        slider.setId(rs.getInt("id"));
        slider.setTitle(rs.getString("title"));
        slider.setImageUrl(rs.getString("image_url"));
        slider.setProductId(rs.getInt("product_id"));
        slider.setStatus(rs.getBoolean("status"));
        slider.setDescription(rs.getString("description"));
        return slider;
    }

}
