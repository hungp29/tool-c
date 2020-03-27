package org.tool.c.utils;

import java.io.File;

/**
 * File utils.
 */
public class FileUtils {

    /**
     * Private constructor to prevent new instance of FileUtils.
     */
    private FileUtils() {
    }

    /**
     * If folder of crypto files is not exist, create new one.
     *
     * @param path path of folder crypto
     * @return true if create
     */
    public static boolean createFolderForCrypto(String path) {
        boolean result = false;
        if (!CommonUtils.isEmpty(path)) {
            File file = new File(path);
            if (path.contains(".crypto")) {
                file = file.getParentFile();
            }

            if (!file.exists()) {
                result = file.mkdir();
            } else {
                result = true;
            }
        }
        return result;
    }
}
