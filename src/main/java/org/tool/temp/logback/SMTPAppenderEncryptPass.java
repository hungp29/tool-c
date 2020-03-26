package org.tool.temp.logback;

import ch.qos.logback.classic.net.SMTPAppender;
import org.tool.temp.crypto.Decrypt;
import org.tool.temp.crypto.SymmetricKey;
import org.tool.temp.utils.constants.Constants;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

/**
 * SMTPAppender with password encrypt.
 */
public class SMTPAppenderEncryptPass extends SMTPAppender {


    @Override
    public void setPassword(String password) {
        if ("[PROTECTED]".equals(password)) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
                String algorithm = bundle.getString("crypto.algorithm");
                password = bundle.getString("logback.password");
                SymmetricKey sk = new SymmetricKey();
                Decrypt decrypt = new Decrypt(sk.getKey(), algorithm);
                password = decrypt.decryptString(password);
            } catch (IOException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
                System.out.println("ERROR SMTPAppenderEncryptPass: " + e.getMessage());
            }
        }
        super.setPassword(password);
    }
}
