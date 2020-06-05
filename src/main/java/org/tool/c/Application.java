package org.tool.c;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.subapp.CheckinApp;

/**
 * Main application.
 */
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws InterruptedException {
        int maxTry = 500;
        int index = 0;
        boolean result = false;
        do {
            try {
                result = CheckinApp.run(args);
            } catch (Exception e) {
                index++;
                Thread.sleep(1000L);
                LOG.error(e.getMessage(), e);
                LOG.info("Try again: " + index);
            }
        } while(!result && index <= maxTry);
    }
}
