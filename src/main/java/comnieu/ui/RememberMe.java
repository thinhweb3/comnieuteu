package comnieu.ui;

import java.io.*;
import java.util.Properties;

public class RememberMe {
    private static final String FILE_NAME = "login.properties";

    public static void save(String username, String password) {
        try (FileOutputStream out = new FileOutputStream(FILE_NAME)) {
            Properties props = new Properties();
            props.setProperty("username", username);
            props.setProperty("password", password);
            props.store(out, "Remember Me Info");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new String[]{"", ""};

        try (FileInputStream in = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(in);
            String user = props.getProperty("username", "");
            String pass = props.getProperty("password", "");
            return new String[]{user, pass};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{"", ""};
    }

    public static void clear() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
