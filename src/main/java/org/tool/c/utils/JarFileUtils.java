package org.tool.c.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.utils.constants.Constants;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Jar File Utils.
 *
 * @author hungp
 */
public class JarFileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JarFileUtils.class);

    /**
     * Prevents new instance {@link JarFileUtils}.
     */
    private JarFileUtils() {
    }

    /**
     * Get version of jar.
     *
     * @return version
     * @throws IOException IO exception
     */
    public static String getVersion() throws IOException {
        String jarFilePath = JarFileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
//        String jarFilePath = "D:\\workspaces\\tool-c\\build\\libs\\tool-c-1.0.7.jar";
        String versionNumber = Constants.EMPTY;
        if (jarFilePath.endsWith(".jar")) {
            JarFile jarFile = new JarFile(jarFilePath);
            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            if (null != attributes) {
                for (Object o : attributes.keySet()) {
                    Attributes.Name key = (Attributes.Name) o;
                    if (key.toString().equals("App-Version")) {
                        versionNumber = (String) attributes.get(key);
                        break;
                    }
                }
            }
            jarFile.close();
        }
        return versionNumber;
    }
}
