package comnieu.util;

import java.awt.Component;
import javax.swing.JOptionPane;

public class XDialog {

    // Thông báo cảnh báo
    public static void alert(String message) {
        alert(message, "Thông báo!");
    }

    public static void alert(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void alert(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    // Thông báo lỗi (icon đỏ)
    public static void alertError(String message) {
        JOptionPane.showMessageDialog(null, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // Thông báo thành công (icon xanh)
    public static void alertSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    // Hộp xác nhận Yes/No
    public static boolean confirm(String message) {
        return confirm(message, "Xác nhận!");
    }

    public static boolean confirm(String message, String title) {
        int result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    // Hộp xác nhận Yes/No/Cancel
    public static int confirmYesNoCancel(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Xác nhận", JOptionPane.YES_NO_CANCEL_OPTION);
    }

    // Nhập liệu cơ bản
    public static String prompt(String message) {
        return prompt(message, "Nhập vào!");
    }

    public static String prompt(String message, String title) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Nhập mật khẩu (text ẩn)
    public static String inputPassword(String message) {
        return JOptionPane.showInputDialog(null, message, "Nhập mật khẩu", JOptionPane.PLAIN_MESSAGE);
    }
}
