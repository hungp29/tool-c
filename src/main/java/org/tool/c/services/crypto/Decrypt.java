package org.tool.c.services.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Decrypt class.
 */
public class Decrypt {

    private SecretKeySpec secretKey;
    private Cipher cipher;

    /**
     * Constructor to new instance of Decrypt.
     *
     * @param secretKey SecretKeySpect object
     * @param algorithm algorithm
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public Decrypt(SecretKeySpec secretKey, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(algorithm);
    }

    /**
     * Decrypt string value.
     *
     * @param data string value need to decrypt
     * @return the value has been decrypt
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String decryptString(String data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(this.cipher.doFinal(Base64.getDecoder().decode(data)));
    }
}
