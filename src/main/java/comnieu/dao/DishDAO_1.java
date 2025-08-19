package comnieu.dao;

import comnieu.dao.Impl.CategoryImpl_1;
import comnieu.util.XJdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishDAO_1 {

    /* ===== Helpers ===== */
    private CategoryImpl_1 mapRow(ResultSet rs) throws SQLException {
        String id       = rs.getString("Id");
        String name     = rs.getString("Name");
        int sizeInt     = rs.getInt("Size");            // Size trong DB là INT (1 nhỏ, 2 lớn)
        String size     = String.valueOf(sizeInt);      // convert về String nếu UI đang dùng String
        double price    = rs.getDouble("UnitPrice");
        String catId    = rs.getString("CategoryId");
        String status   = String.valueOf(rs.getInt("Status"));
        return new CategoryImpl_1(id, name, size, price, catId, status);
    }

    /* ===== Lấy món theo MÃ LOẠI ===== */
    public List<CategoryImpl_1> getMonAnByMaLoai(int categoryId) {
        List<CategoryImpl_1> list = new ArrayList<>();
        String sql = "SELECT Id, Name, Size, UnitPrice, CategoryId, Status FROM Dish WHERE CategoryId = ?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi getMonAnByMaLoai", e);
        }
        return list;
    }

    // Overload: vẫn nhận String để tương thích mã cũ
    public List<CategoryImpl_1> getMonAnByMaLoai(String categoryId) {
        try {
            return getMonAnByMaLoai(Integer.parseInt(categoryId));
        } catch (NumberFormatException ex) {
            return new ArrayList<>();
        }
    }

    /* ===== Lấy tất cả món ===== */
    public List<CategoryImpl_1> getAllMonAn() {
        List<CategoryImpl_1> list = new ArrayList<>();
        String sql = "SELECT Id, Name, Size, UnitPrice, CategoryId, Status FROM Dish";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi getAllMonAn", e);
        }
        return list;
    }

    /* ===== Lấy món theo TÊN LOẠI ===== */
    public List<CategoryImpl_1> getMonAnByTenLoai(String tenLoai) {
        List<CategoryImpl_1> list = new ArrayList<>();
        String sql = """
            SELECT d.Id, d.Name, d.Size, d.UnitPrice, d.CategoryId, d.Status
            FROM Dish d
            JOIN Category c ON d.CategoryId = c.Id
            WHERE c.Name = ?
            """;
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenLoai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi getMonAnByTenLoai", e);
        }
        return list;
    }

    /* ===== Kiểm tra tồn kho đủ để nấu 1 suất món (so với định mức) ===== */
    public boolean kiemTraTrangThaiNguyenLieu(int dishId) {
        // đủ nếu với MỌI nguyên liệu: i.Quantity >= di.Quantity
        String sql = """
            SELECT di.Quantity AS RequiredQty, i.Quantity AS StockQty
            FROM DishIngredient di
            JOIN Ingredient i ON i.Id = di.IngredientId
            WHERE di.DishId = ?
            """;
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dishId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double required = rs.getDouble("RequiredQty");
                    double available = rs.getDouble("StockQty");
                    if (available < required) return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kiemTraTrangThaiNguyenLieu", e);
        }
        return true;
    }

    /* ===== Cập nhật trạng thái món (0: Còn, 1: Hết) ===== */
    public void updateDishStatus(int dishId, int status) {
        String sql = "UPDATE Dish SET Status = ? WHERE Id = ?";
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setInt(2, dishId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi updateDishStatus", e);
        }
    }

    /* ===== Thêm nguyên liệu vào món =====
       Bảng DishIngredient(DishId, IngredientId, Quantity, Unit) */
    public void addIngredientToDish(String dishId, String ingredientName, String quantity, String unit) {
        String sql = """
            INSERT INTO DishIngredient (DishId, IngredientId, Quantity, Unit)
            SELECT ?, i.Id, ?, ?
            FROM Ingredient i
            WHERE i.Name = ?
            """;
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dishId);
            ps.setBigDecimal(2, new java.math.BigDecimal(quantity));
            ps.setString(3, unit);
            ps.setString(4, ingredientName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi addIngredientToDish", e);
        }
    }

    /* ===== Lấy/ tạo Category theo tên, trả về Id ===== */
    public int getOrCreateCategoryIdByName(String name) {
        String sqlSelect = "SELECT Id FROM Category WHERE Name = ?";
        String sqlInsert = "INSERT INTO Category (Name) VALUES (?)";
        try (Connection conn = XJdbc.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("Id");
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi getOrCreateCategoryIdByName", e);
        }
        return -1;
    }

    /* ===== Thêm món, trả về Id mới =====
       Lưu ý: bảng Dish có cột Unit NOT NULL → cần truyền vào */
    public int insertDishReturnId(String name, String size, double unitPrice, int categoryId) {
        int sizeInt;
        try {
            sizeInt = Integer.parseInt(size); // UI đang nhập "1"/"2"
        } catch (NumberFormatException e) {
            sizeInt = 1; // mặc định size nhỏ
        }

        String sql = """
            INSERT INTO Dish (Name, UnitPrice, Unit, CategoryId, Status, Size)
            OUTPUT INSERTED.Id
            VALUES (?, ?, ?, ?, 0, ?)
            """;
        try (Connection conn = XJdbc.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setBigDecimal(2, new java.math.BigDecimal(unitPrice));
            ps.setString(3, "Phần");          // TODO: nếu UI có trường Unit thì truyền từ UI
            ps.setInt(4, categoryId);
            ps.setInt(5, sizeInt);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi insertDishReturnId", e);
        }
        return -1;
    }
}
