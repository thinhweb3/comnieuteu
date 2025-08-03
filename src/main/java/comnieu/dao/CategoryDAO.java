package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<String> getAllMaLoaiMA() {
        List<String> list = new ArrayList<>();
        String url = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantDB;encrypt=true;trustServerCertificate=true";
        String sql = "SELECT Name FROM Category";
        try (Connection conn = DriverManager.getConnection(url, "sa", "123456");
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
