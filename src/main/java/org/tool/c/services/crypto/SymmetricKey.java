package org.tool.c.services.crypto;

import org.tool.c.utils.FileUtils;
import org.tool.c.utils.constants.Constants;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.ResourceBundle;

/**
 * Symmetric Key.
 */
public class SymmetricKey {

    /**
     * Secret key.
     */
    private SecretKeySpec secretKey;

    /**
     * Default constructor.
     */
    public SymmetricKey() throws IOException, ClassNotFoundException {
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.BUNDLE_APPLICATION);
        loadSecretKeyFromFile(bundle.getString("crypto.secret-file"));
    }

    /**
     * Constructor with path of secret key file.
     *
     * @param path path of secret key file
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public SymmetricKey(String path) throws IOException, ClassNotFoundException {
        loadSecretKeyFromFile(path);
    }

    /**
     * Constructor to create new instance of SymmetricKey.
     *
     * @param length    length of key
     * @param algorithm algorithm
     */
    public SymmetricKey(int length, String algorithm) {
        byte[] key = new byte[length];
        new SecureRandom().nextBytes(key);
        // New SecretKeySpec
        this.secretKey = new SecretKeySpec(key, algorithm);
    }

    /**
     * Get key.
     *
     * @return
     */
    public SecretKeySpec getKey() {
        return this.secretKey;
    }

    /**
     * Write Secret Key to file.
     *
     * @param path the path to save file
     * @throws IOException
     */
    public void writeToFile(String path) throws IOException {
        FileUtils.createFolderForCrypto(path);
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
        out.writeObject(this.secretKey);
        out.close();
    }

    /**
     * Load Secret Key from file.
     *
     * @param path the path of secret key
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadSecretKeyFromFile(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
        this.secretKey = (SecretKeySpec) in.readObject();
        in.close();
    }
}
