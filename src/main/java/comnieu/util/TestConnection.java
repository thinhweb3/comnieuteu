/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.util;

/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PS44850_PolyCafe;encrypt=true;trustServerCertificate=true"; // hoặc QUANLYDUAN
        String user = "sa";
        String password = "1703"; // sửa thành mật khẩu của bạn

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối thành công!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Kết nối thất bại!");
            e.printStackTrace();
        }
    }
}

