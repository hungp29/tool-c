package org.tool.c.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.exception.EmailException;
import org.tool.c.utils.CryptoUtils;
import org.tool.c.utils.constants.Constants;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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

    public EmailService() {
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

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new EmailException(e.getMessage(), e);
        }
    }
}
