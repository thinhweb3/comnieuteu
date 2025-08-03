package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lmp.Categorylmp;

public class DishDAO {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123456";

    public List<Categorylmp> getMonAnByMaLoai(String maLoaiMA) {
        List<Categorylmp> list = new ArrayList<>();
        String sql = "SELECT Id, Name, Size, UnitPrice, CategoryId, Status FROM Dish WHERE CategoryId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(maLoaiMA));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maMA   = rs.getString("Id");
                String tenMon = rs.getString("Name");
                String size   = String.valueOf(rs.getInt("Size"));
                double donGia = rs.getDouble("UnitPrice");
                String maLoai = rs.getString("CategoryId");
                String tinhTrang = String.valueOf(rs.getInt("Status"));
                Categorylmp ma = new Categorylmp(maMA, tenMon, size, donGia, maLoai, tinhTrang);
                list.add(ma);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Categorylmp> getAllMonAn() {
        List<Categorylmp> list = new ArrayList<>();
        String sql = "SELECT Id, Name, Size, UnitPrice, CategoryId, Status FROM Dish";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maMA   = rs.getString("Id");
                String tenMon = rs.getString("Name");
                String size   = String.valueOf(rs.getInt("Size"));
                double donGia = rs.getDouble("UnitPrice");
                String maLoai = rs.getString("CategoryId");
                String tinhTrang = String.valueOf(rs.getInt("Status"));
                Categorylmp ma = new Categorylmp(maMA, tenMon, size, donGia, maLoai, tinhTrang);
                list.add(ma);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Categorylmp> getMonAnByTenLoai(String tenLoai) {
        List<Categorylmp> list = new ArrayList<>();
        String sql = "SELECT d.Id, d.Name, d.Size, d.UnitPrice, d.CategoryId, d.Status "
                   + "FROM Dish d JOIN Category c ON d.CategoryId = c.Id "
                   + "WHERE c.Name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenLoai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maMA   = rs.getString("Id");
                String tenMon = rs.getString("Name");
                String size   = String.valueOf(rs.getInt("Size"));
                double donGia = rs.getDouble("UnitPrice");
                String maLoai = rs.getString("CategoryId");
                String tinhTrang = String.valueOf(rs.getInt("Status"));
                Categorylmp ma = new Categorylmp(maMA, tenMon, size, donGia, maLoai, tinhTrang);
                list.add(ma);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // Kiểm tra nguyên liệu tồn kho có đủ không
public boolean kiemTraTrangThaiNguyenLieu(int dishId) {
    String sql = """
        SELECT di.IngredientId, di.Quantity AS RequiredQty, i.Quantity AS StockQty
        FROM DishIngredient di
        JOIN Ingredient i ON di.IngredientId = i.Id
        WHERE di.DishId = ?
    """;
    try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, dishId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            float required = rs.getFloat("RequiredQty");
            float available = rs.getFloat("StockQty");
            if (available < required) {
                return false;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return true;
}

// Cập nhật trạng thái món ăn (0: Còn, 1: Hết)
public void updateDishStatus(int dishId, int status) {
    String sql = "UPDATE Dish SET Status = ? WHERE Id = ?";
    try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, status);
        ps.setInt(2, dishId);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}

  
