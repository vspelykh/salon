package ua.vspelykh.salon.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    private static final Logger LOG = LogManager.getLogger(EmailService.class);

    private final String username;
    private final String password;
    private final Properties prop;

    private static final String PROPERTY_PATH = "email.properties";
    private static final String EMAIL = "email.mail";
    private static final String PASS = "email.password";
    private static final String SMTP = "email.smtp";
    private static final String PORT = "email.port";

    public static void sendEmail(String clientEmail, String theme, String message) {
        Properties properties = new Properties();
        readProperties(properties);
        EmailService emailService = new EmailService(properties.getProperty(SMTP), getPort(properties.getProperty(PORT)),
                properties.getProperty(EMAIL), (properties.getProperty(PASS)));
        try {
            emailService.sendMail(properties.getProperty(EMAIL), clientEmail, theme, message);
            if (LOG.isEnabled(Level.INFO)) {
                LOG.info(String.format("Email sent to %s. Theme: %s", clientEmail, theme));
            }
        } catch (Exception e) {
            LOG.error(String.format("Fail to sent email for %s. Theme: %s. Issue: %s", clientEmail, theme, e.getMessage()));
        }
    }

    private EmailService(String host, int port, String username, String password) {
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        this.username = username;
        this.password = password;
    }

    private void sendMail(String from, String to, String subject, String msg) throws MessagingException {

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    private static void readProperties(Properties properties) {
        try {
            properties.load(EmailService.class.getClassLoader().getResourceAsStream(PROPERTY_PATH));
        } catch (IOException e) {
            LOG.error(String.format("Error to read property file for DB connection. Issue %s", e.getMessage()));
        }
    }

    private static int getPort(String port) {
        return Integer.parseInt(port);
    }
}
