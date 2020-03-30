package org.tool.c.utils;

import org.tool.c.services.crypto.Decrypt;
import org.tool.c.services.crypto.SymmetricKey;

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
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     */
    public static String decryptString(String algorithm, String encryptData) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        SymmetricKey sk = new SymmetricKey();
        Decrypt decrypt = new Decrypt(sk.getKey(), algorithm);
        return decrypt.decryptString(encryptData);
    }
}
