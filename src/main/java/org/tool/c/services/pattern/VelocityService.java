package org.tool.c.services.pattern;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.tool.c.bundle.AppBundle;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Velocity Service.
 */
public class VelocityService extends AppBundle {

    private static final String SUFFIX = ".vm";
    private VelocityEngine velocityEngine;

    /**
     * Constructor to new instance.
     */
    public VelocityService() {
        velocityEngine = new VelocityEngine();
        setProperty();
    }

    /**
     * Merge data and template for mail.
     *
     * @param templateName template name of mail
     * @param data         data to merge with template
     * @return value of template after merging
     */
    public String[] mergeForMail(String templateName, Map<String, String> data) {
        String subject = this.mergeDataAndTemplate(velocityTemplateMail + templateName + "-subject", data);
        String content = this.mergeDataAndTemplate(velocityTemplateMail + templateName, data);

        return new String[]{subject, content};
    }

    /**
     * Set property for velocity.
     */
    private void setProperty() {
        Properties p = new Properties();
        p.setProperty("resource.loader", velocityResourceLoader);
        p.setProperty("class.resource.loader.class", velocityClassResourceLoader);
        velocityEngine.init(p);
    }

    /**
     * Merge data and template.
     *
     * @param templatePath template path
     * @param data         data to merge
     * @return value of template after merging
     */
    private String mergeDataAndTemplate(String templatePath, Map<String, String> data) {
        // Get template
        Template template = velocityEngine.getTemplate(templatePath + SUFFIX);
        // Create Velocity Context with data
        VelocityContext velocityContext = new VelocityContext(data);
        // Merge data to template
        StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);

        return writer.toString();
    }
}
