package ui;

import java.sql.*;

public class XJdbc {

    // Cấu hình kết nối CSDL
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=RestaurantDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123456";

    // Mở kết nối
    public static Connection openConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver SQL Server.");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("? Không thể kết nối đến CSDL:");
            System.err.println("Lý do: " + e.getMessage());
            throw new RuntimeException("Lỗi khi mở kết nối đến CSDL", e);
        }
    }

    // Đóng kết nối
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }

    // Lấy Statement
    public static Statement getStmt(Connection conn) throws SQLException {
        return conn.createStatement();
    }

    // Thực thi SELECT (trả ResultSet)
    public static ResultSet executeQuery(String sql) {
        try {
            Connection conn = openConnection();
            Statement stmt = getStmt(conn);
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thực thi truy vấn SELECT", e);
        }
    }

    // Hàm demo SELECT
    public static void demoSelect() {
        String sql = "SELECT TOP 5 * FROM NhanVien"; // Bạn thay bảng phù hợp ở đây
        try (
            Connection conn = openConnection();
            Statement stmt = getStmt(conn);
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SELECT: " + e.getMessage());
        }
    }

    // Chạy thử chương trình
    public static void main(String[] args) {
        demoSelect();
    }
}
