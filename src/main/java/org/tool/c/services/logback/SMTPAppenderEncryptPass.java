package org.tool.c.services.logback;

import ch.qos.logback.classic.net.SMTPAppender;
import org.tool.c.utils.CryptoUtils;
import org.tool.c.utils.constants.Constants;

import java.util.ResourceBundle;

/**
 * SMTPAppender with password encrypt.
 */
public class SMTPAppenderEncryptPass extends SMTPAppender {


    @Override
    public void setPassword(String password) {
        if ("[PROTECTED]".equals(password)) {
            ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
            password = CryptoUtils.decryptString(bundle.getString("crypto.algorithm"), bundle.getString("logback.password"));
        }
        super.setPassword(password);
    }
}
