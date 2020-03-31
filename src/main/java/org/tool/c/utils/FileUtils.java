package org.tool.c.utils;

import org.tool.c.exception.ReadDataInputStreamException;

import java.io.*;

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

    /**
     * Read data from input stream.
     *
     * @param is InputStream object
     * @return
     * @throws IOException
     */
    public static String readInputStream(InputStream is) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while (null != (inputLine = br.readLine())) {
                content.append(inputLine);
            }
            br.close();

            return content.toString();
        } catch (IOException e) {
            throw new ReadDataInputStreamException(e);
        }
    }
}
