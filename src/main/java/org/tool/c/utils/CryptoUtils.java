package org.tool.c.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.exception.CryptoException;
import org.tool.c.services.crypto.Decrypt;
import org.tool.c.services.crypto.SymmetricKey;
import org.tool.c.utils.constants.Constants;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Crypto Utils class.
 */
public class CryptoUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoUtils.class);

    /**
     * Private Constructor to prevent new instance of CryptoUtils.
     */
    private CryptoUtils() {
    }

    /**
     * Decrypt data by using @algorithm.
     *
     * @param encryptData encrypt data need to decrypt
     * @param algorithm   algorithm
     * @return decrypt value
     */
    public static String decryptString(String algorithm, String encryptData) {
        String decryptValue;
        try {
            SymmetricKey sk = new SymmetricKey();
            Decrypt decrypt = new Decrypt(sk.getKey(), algorithm);
            decryptValue = decrypt.decryptString(encryptData);
        } catch (IOException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException |
                BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new CryptoException(e.getMessage(), e);
        }
        return decryptValue;
    }
}
