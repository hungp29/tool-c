package org.tool.c.exception;

/**
 * Crypto Exception.
 */
public class CryptoException extends RuntimeException {

    public static final String CRYPTO_ERROR_MESSAGE = "Encryption / Decryption process has encountered an error";

    public CryptoException() {
        super(CRYPTO_ERROR_MESSAGE);
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(Throwable thr) {
        super(thr);
    }

    public CryptoException(String message, Throwable thr) {
        super(message, thr);
    }
}
