package comnieu.dao;

import comnieu.dao.Impl.IngredientImpl_1;
import comnieu.util.XJdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO_1 {

    // Lấy danh sách tất cả nguyên liệu
    public List<IngredientImpl_1> getAllIngredient() {
        List<IngredientImpl_1> list = new ArrayList<>();
        String sql = "SELECT Id, Name, Quantity, Unit, Status, SupplierId FROM Ingredient";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("Id");
                String name = rs.getString("Name");
                double quantity = rs.getDouble("Quantity");
                String unit = rs.getString("Unit");
                String status = String.valueOf(rs.getInt("Status"));
                String supplierId = rs.getString("SupplierId");
                list.add(new IngredientImpl_1(id, name, quantity, unit, status, supplierId));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách nguyên liệu", e);
        }
        return list;
    }

    // Thêm nguyên liệu mới
    public boolean insertIngredient(String name, String unit, String supplierId) {
        String sql = "INSERT INTO Ingredient (Name, Quantity, Unit, Status, SupplierId) VALUES (?, 0, ?, 0, ?)";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, unit);
            ps.setInt(3, Integer.parseInt(supplierId));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm nguyên liệu", e);
        }
    }

    // Xóa nguyên liệu theo Id
    public boolean deleteIngredientById(String id) {
        String sql = "DELETE FROM Ingredient WHERE Id = ?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa nguyên liệu", e);
        }
    }
}
