package bvm.util;

import java.sql.*;

/**
 * Lớp tiện ích hỗ trợ thao tác với cơ sở dữ liệu j5Shop
 */
public class XJdbc {

    private static Connection connection;

    /**
     * Mở kết nối nếu chưa mở hoặc đã đóng
     *
     * @return Kết nối đã sẵn sàng
     */
    public static Connection openConnection() {
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dburl = "jdbc:sqlserver://localhost:1433;databaseName=j5Shop;encrypt=true;trustServerCertificate=true";
        String username = "sa";
        String password = "1703";

        try {
            if (!isReady()) {
                Class.forName(driver);
                connection = DriverManager.getConnection(dburl, username, password);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Lỗi kết nối cơ sở dữ liệu", e);
        }
        return connection;
    }

    /**
     * Đóng kết nối
     */
    public static void closeConnection() {
        try {
            if (isReady()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đóng kết nối", e);
        }
    }

    /**
     * Kiểm tra kết nối đã mở chưa
     */
    public static boolean isReady() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kiểm tra trạng thái kết nối", e);
        }
    }

    /**
     * Dùng cho INSERT, UPDATE, DELETE
     *
     * @param sql Câu lệnh SQL
     * @param values Giá trị truyền vào
     * @return Số dòng bị ảnh hưởng
     */
    public static int executeUpdate(String sql, Object... values) {
        try {
            PreparedStatement stmt = getStmt(sql, values);
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi khi thực thi câu lệnh UPDATE", ex);
        }
    }

    /**
     * Dùng cho SELECT trả về ResultSet
     */
    public static ResultSet executeQuery(String sql, Object... values) {
        try {
            PreparedStatement stmt = getStmt(sql, values);
            return stmt.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi khi thực thi truy vấn", ex);
        }
    }

    /**
     * Dùng cho truy vấn 1 giá trị duy nhất (SELECT COUNT, MAX, MIN,...)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(String sql, Object... values) {
        try {
            ResultSet rs = executeQuery(sql, values);
            if (rs.next()) {
                return (T) rs.getObject(1);
            }
            rs.getStatement().getConnection().close();
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Lỗi truy vấn giá trị đơn", ex);
        }
    }

    /**
     * Chuẩn bị câu lệnh PreparedStatement
     */
    private static PreparedStatement getStmt(String sql, Object... values) throws SQLException {
        Connection conn = openConnection();
        PreparedStatement stmt = sql.trim().startsWith("{")
                ? conn.prepareCall(sql)
                : conn.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            stmt.setObject(i + 1, values[i]);
        }
        return stmt;
    }
}
