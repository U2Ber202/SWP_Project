package util;

import java.security.SecureRandom;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateOTP() {
        int number = RANDOM.nextInt(1_000_000);
        return String.format("%06d", number);
    }

    public static boolean sendEmail(String toEmail, String otp) {
        return sendEmailWithContent(toEmail, "OTP Reset Password - Shoe Store", 
                "Ma OTP cua ban la: " + otp + "\nVui long khong chia se ma nay cho bat ky ai.");
    }

    public static boolean sendEmailWithContent(String toEmail, String subject, String content) {
        final String username = AppConfig.getRequired("MAIL_USERNAME", null);
        final String password = AppConfig.getRequired("MAIL_PASSWORD", null);

        if (username == null || password == null) {
            System.err.println("Email credentials not configured.");
            return false;
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", AppConfig.get("MAIL_SMTP_HOST", "smtp.gmail.com"));
        prop.put("mail.smtp.port", AppConfig.get("MAIL_SMTP_PORT", "587"));
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
//            Message message = new MimeMessage(session);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");         // ← fix subject encoding
            message.setText(content, "UTF-8");            // ← fix body encoding (plain text)
            Transport.send(message);
            return true;
        } catch (MessagingException | IllegalStateException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

