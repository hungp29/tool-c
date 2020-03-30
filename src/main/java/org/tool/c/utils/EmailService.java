package org.tool.c.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.utils.constants.Constants;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Email Service.
 */
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private String host = "";
    private int port = 0;
    private String username = "";
    private String password = "";

    public EmailService() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException, ClassNotFoundException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        this.host = bundle.getString("email.announcement.host");
        this.port = Integer.parseInt(bundle.getString("email.announcement.port"));
        this.username = bundle.getString("email.announcement.username");
        this.password = CryptoUtils.decryptString(bundle.getString("crypto.algorithm"),
                bundle.getString("email.announcement.password"));
    }

    public EmailService(String host, int port, String username, String password) {

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Send email announcement.
     *
     * @param to      email to
     * @param subject subject of email
     * @param content content of email
     */
    public void sendAnnouncement(String to, String subject, String content) {
        // Setup properties for mail
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        // New session
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            String msg = content;

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
