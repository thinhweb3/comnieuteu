package bvm.util;

import java.util.Base64;
import java.util.UUID;

public class XStr {

    // Kiểm tra rỗng
    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    // Bắt buộc không để trống
    public static boolean requireField(String input, String fieldName) {
        if (isBlank(input)) {
            XDialog.alert("Không được để trống: " + fieldName);
            return false;
        }
        return true;
    }

    // Kiểm tra độ dài chuỗi
    public static boolean requireLength(String input, int min, int max, String fieldName) {
        if (input.length() < min || input.length() > max) {
            XDialog.alert(fieldName + " phải có độ dài từ " + min + " đến " + max + " ký tự.");
            return false;
        }
        return true;
    }

    // Kiểm tra theo định dạng biểu thức chính quy
    public static boolean requireFormat(String input, String regex, String fieldName) {
        if (!input.matches(regex)) {
            XDialog.alert(fieldName + " không đúng định dạng.");
            return false;
        }
        return true;
    }

    // Kiểm tra số và phạm vi hợp lệ
    public static boolean requireNumberInRange(String input, double min, double max, String fieldName) {
        try {
            double value = Double.parseDouble(input);
            if (value < min || value > max) {
                XDialog.alert(fieldName + " phải trong khoảng từ " + min + " đến " + max + ".");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            XDialog.alert(fieldName + " phải là số hợp lệ.");
            return false;
        }
    }
    // Kiểm tra chuỗi tối thiểu
public static boolean minLength(String input, int min, String message) {
    if (input.length() < min) {
        XDialog.alert(message);
        return false;
    }
    return true;
}

// Kiểm tra chuỗi tối đa
public static boolean maxLength(String input, int max, String message) {
    if (input.length() > max) {
        XDialog.alert(message);
        return false;
    }
    return true;
}

// Kiểm tra định dạng bằng regex
public static boolean regex(String input, String regex, String message) {
    if (!input.matches(regex)) {
        XDialog.alert(message);
        return false;
    }
    return true;
}

// Kiểm tra định dạng ngày
public static boolean requireDate(String input, String pattern, String fieldName) {
    if (isBlank(input)) {
        XDialog.alert("Không được để trống: " + fieldName);
        return false;
    }
    if (bvm.util.XDate.parse(input, pattern) == null) {
        XDialog.alert(fieldName + " không đúng định dạng ngày (định dạng yêu cầu: " + pattern + ").");
        return false;
    }
    return true;
}

// Kiểm tra định dạng email
public static boolean requireEmail(String input, String fieldName) {
    String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    if (isBlank(input)) {
        XDialog.alert("Không được để trống: " + fieldName);
        return false;
    }
    if (!input.matches(emailRegex)) {
        XDialog.alert(fieldName + " không đúng định dạng email.");
        return false;
    }
    return true;
}

    // Các phương thức tiện ích khác
    public static String valueOf(Object object) {
        return object == null ? "" : String.valueOf(object);
    }

    public static String encodeB64(String text) {
        byte[] data = text.getBytes();
        return Base64.getEncoder().encodeToString(data);
    }

    public static String decodeB64(String text) {
        byte[] data = Base64.getDecoder().decode(text);
        return new String(data);
    }

    public static String getKey(String... args) {
        if (args.length == 0) {
            args = new String[]{UUID.randomUUID().toString(), String.valueOf(System.currentTimeMillis())};
        }
        int hashCode = String.join("-", args).hashCode();
        String key = "XXXXXXXX" + Integer.toHexString(Math.abs(hashCode)).toUpperCase();
        return key.substring(key.length() - 8);
    }

    public static void main(String[] args) {
        String encodedText = XStr.encodeB64("Nguyễn Nghiệm");
        System.out.println(encodedText);

        String decodedText = XStr.decodeB64("Tmd1eT9uIE5naGk/bQ==");
        System.out.println(decodedText);

        String key1 = XStr.getKey();
        System.out.println(key1);

        String key2 = XStr.getKey("Nguyễn", "Nghiệm", "PolyHCM");
        System.out.println(key2);
    }
}
