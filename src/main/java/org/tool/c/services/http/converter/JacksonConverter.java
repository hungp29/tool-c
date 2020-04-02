package org.tool.c.services.http.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.exception.ConverterException;
import org.tool.c.exception.ConverterUnsupportedException;
import org.tool.c.exception.ResponseFailureException;
import org.tool.c.services.http.ResponseTemplate;
import org.tool.c.utils.CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Jackson converter.
 */
public class JacksonConverter {

    private static Logger LOG = LoggerFactory.getLogger(JacksonConverter.class);

    public static final String WRAP_CONTEXT_LIST = "LIST";
    public static final String WRAP_CONTEXT_MAP = "MAP";

    private ObjectMapper objectMapper;
    private PropertyUtilsBean propertyUtilsBean;

    public JacksonConverter() {
        objectMapper = new ObjectMapper();
        propertyUtilsBean = new PropertyUtilsBean();

        // register module
        this.registerModule();
    }

    /**
     * Register module for ObjectMapper.
     */
    private void registerModule() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Check support.
     *
     * @param clazz class need to check
     * @return true if this converter is support
     */
    public boolean support(Class<?> clazz) {
        JavaType javaType = this.getJavaType(clazz);
        AtomicReference<Throwable> causeRef = new AtomicReference<>();
        if (this.objectMapper.canDeserialize(javaType, causeRef)) {
            return true;
        }

        Throwable thr = causeRef.get();
        if (null != thr) {
            String msg = "Failed to evaluate deserialization for type " + javaType;
            LOG.warn(msg, thr);
        }
        return false;
    }

    /**
     * Read object from input stream.
     *
     * @param contextClass class of object
     * @param body         input stream of request
     * @param cause        to store error if response is failure
     * @param path         path to get object
     * @return object
     */
    public Object read(Class<?> contextClass, InputStream body, String path, AtomicReference<Throwable> cause) {
        JavaType javaType = this.getJavaType(contextClass);
        return this.readJavaType(javaType, body, path, cause);
    }

    /**
     * Read object from input stream.
     *
     * @param contextClass    class of object
     * @param body            input stream of request
     * @param cause           to store error if response is failure
     * @param path            path to get object
     * @param wrapContextType Wrap context type
     * @return object
     */
    public Object read(Class<?> contextClass, InputStream body, String path, String wrapContextType, AtomicReference<Throwable> cause) {
        JavaType javaType = this.getJavaType(contextClass, wrapContextType);
        return this.readJavaType(javaType, body, path, cause);
    }

    /**
     * Read by JavaType.
     *
     * @param javaType java type of object
     * @param body     input stream
     * @param path     path to get object
     * @param cause    store exception
     * @return object read from json
     */
    private Object readJavaType(JavaType javaType, InputStream body, String path, AtomicReference<Throwable> cause) {
        if (this.validResponseTemplateClass(ResponseTemplate.class)) {
            ResponseTemplate responseTemplate = readResponseTemplate(body);

            if (responseTemplate.isSuccess()) {
                Object result = this.getResultObject(responseTemplate, path);
                return this.convertJavaType(javaType, result);
            } else {
                cause.set(new ResponseFailureException(responseTemplate.getError().getCode()));
            }
        } else {
            throw new ConverterUnsupportedException("Convert do not support Response Template class");
        }
        return null;
    }

    /**
     * Convert to object by JavaType.
     *
     * @param javaType      JavaType of object
     * @param objectConvert object need to converted
     * @return object
     */
    private Object convertJavaType(JavaType javaType, Object objectConvert) {
        return this.objectMapper.convertValue(objectConvert, javaType);
    }

    /**
     * Read Response template object from json by Java Type.
     *
     * @param body Input Stream of response
     * @return object is read from stream
     */
    private ResponseTemplate readResponseTemplate(InputStream body) {
        try {
            return this.objectMapper.readValue(body, ResponseTemplate.class);
        } catch (IOException e) {
            throw new ConverterException("An error occurs while converting json to object", e);
        }
    }

    /**
     * Get JavaType of class.
     *
     * @param clazz           class
     * @param wrapContextType type collection
     * @return JavaType of class
     */
    private JavaType getJavaType(Class<?> clazz, String wrapContextType) {
        JavaType javaType = this.getJavaType(clazz);
        wrapContextType = CommonUtils.trim(wrapContextType);
        switch (wrapContextType) {
            case WRAP_CONTEXT_LIST:
                return this.objectMapper.getTypeFactory().constructCollectionType(List.class, javaType);
            case WRAP_CONTEXT_MAP:
                return this.objectMapper.getTypeFactory().constructMapType(Map.class, this.getJavaType(String.class), javaType);
            default:
                return javaType;
        }
    }

    /**
     * Get JavaType of class.
     *
     * @param clazz class
     * @return JavaType of class
     */
    private JavaType getJavaType(Class<?> clazz) {
        return this.objectMapper.getTypeFactory().constructType(clazz);
    }

    /**
     * Validation Response Template is valid or not.
     *
     * @param responseTemplate response template class
     * @return true if it's valid
     */
    private boolean validResponseTemplateClass(Class<?> responseTemplate) {
        return this.support(responseTemplate);
    }

    /**
     * Get result object from response template.
     *
     * @param responseTemplate response template object
     * @param path             path to get object
     * @return result objece
     */
    private Object getResultObject(ResponseTemplate responseTemplate, String path) {
        try {
            return CommonUtils.isEmpty(path) ? responseTemplate.getResult() : propertyUtilsBean.getProperty(responseTemplate.getResult(), path);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ConverterException("Cannot get result object from Response Template object with path: " + path, e);
        }
    }
}
