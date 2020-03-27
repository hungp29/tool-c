package org.tool.c.services.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Encrypt class.
 */
public class Encrypt {

    private SecretKeySpec secretKey;
    private Cipher cipher;

    /**
     * Constructor to new instance of Encrypt.
     *
     * @param secretKey SecretKeySpect object
     * @param algorithm algorithm
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public Encrypt(SecretKeySpec secretKey, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(algorithm);
    }

    /**
     * Encrypt string value.
     *
     * @param data the string need to encrypt
     * @return the string has been encrypt
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String encryptString(String data) throws InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
        return Base64.getEncoder().encodeToString(this.cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
