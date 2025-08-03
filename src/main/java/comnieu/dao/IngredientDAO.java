package dao;

import lmp.Ingredientlmp;
import java.sql.*;
    import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123456";

    public List<Ingredientlmp> getAllIngredient() {
        List<Ingredientlmp> list = new ArrayList<>();
        String sql = "SELECT Id, Name, Quantity, Unit, Status, SupplierId FROM Ingredient";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id   = rs.getString("Id");
                String name = rs.getString("Name");
                double quantity = rs.getDouble("Quantity");
                String unit = rs.getString("Unit");
                String status = String.valueOf(rs.getInt("Status"));
                String supplierId = rs.getString("SupplierId");
                list.add(new Ingredientlmp(id, name, quantity, unit, status, supplierId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean insertIngredient(String name, String unit, String supplierId) {
    String sql = "INSERT INTO Ingredient (Name, Quantity, Unit, Status, SupplierId) VALUES (?, 0, ?, 0, ?)";
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, name);
        ps.setString(2, unit);
        ps.setInt(3, Integer.parseInt(supplierId));
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
