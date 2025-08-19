package comnieu.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class MailSender {

    public static void send(String toEmail, String subject, String content) throws MessagingException {
        final String fromEmail = "phong161168@gmail.com"; // ✔ Email người gửi
        final String password = "smfp symm fdrw fodp";     // ✔ App password (không phải password Gmail)

        // Cấu hình SMTP cho Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // tránh lỗi SSL

        // Tạo session với xác thực
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Tạo nội dung email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Cơm Niêu"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(content); // Hoặc dùng setContent nếu muốn HTML

            // Gửi mail
            Transport.send(message);
            System.out.println("✅ Đã gửi email thành công đến: " + toEmail);
        } catch (Exception ex) {
            System.err.println("❌ Gửi email thất bại: " + ex.getMessage());
            throw new MessagingException("Lỗi khi gửi email: " + ex.getMessage(), ex);
        }
    }
}
