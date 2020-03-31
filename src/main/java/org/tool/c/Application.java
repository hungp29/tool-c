package org.tool.c;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.subapp.CheckinApp;

/**
 * Main application.
 */
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            CheckinApp.run();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
