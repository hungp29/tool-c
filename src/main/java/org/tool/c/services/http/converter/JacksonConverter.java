package org.tool.c.services.http.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tool.c.exception.ErrorResponseException;
import org.tool.c.exception.ResponseConverterException;
import org.tool.c.exception.ResponseTemplateUnsupportedException;
import org.tool.c.services.http.ResponseTemplate;
import org.tool.c.utils.CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Jackson converter.
 */
public class JacksonConverter {

    private static Logger LOG = LoggerFactory.getLogger(JacksonConverter.class);

    private static final String JAVA_TYPE_LIST = "LIST";
    private static final String JAVA_TYPE_MAP = "MAP";

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
    public Object read(Class<?> contextClass, InputStream body, AtomicReference<Throwable> cause, String path) {
        JavaType javaType = this.getJavaType(contextClass);

        if (this.validResponseTemplateClass(ResponseTemplate.class)) {
            ResponseTemplate responseTemplate = (ResponseTemplate) readJavaType(this.getJavaType(ResponseTemplate.class), ResponseTemplate.class, body);

            if (responseTemplate.isSuccess()) {
                try {
                    Object result = CommonUtils.isEmpty(path) ? responseTemplate.getResult() : propertyUtilsBean.getProperty(responseTemplate.getResult(), path);
                    return this.convertJavaType(javaType, result);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new ResponseConverterException("Cannot get value of object from property " + path);
                }
            } else {
                cause.set(new ErrorResponseException(responseTemplate.getError().getCode()));
            }
        } else {
            throw new ResponseTemplateUnsupportedException("Convert do not support Response template class");
        }
        return null;

    }

    /**
     * Read data as List.
     *
     * @param contextClass class of object
     * @param body         Input Stream from response
     * @param cause        store Error
     * @return list of object
     */
    @SuppressWarnings("unchecked")
    public List<Object> readAsList(Class<?> contextClass, InputStream body, AtomicReference<Throwable> cause) {
        JavaType javaType = this.getJavaType(contextClass, JAVA_TYPE_LIST);

        if (this.validResponseTemplateClass(ResponseTemplate.class)) {
            ResponseTemplate responseTemplate = (ResponseTemplate) readJavaType(this.getJavaType(ResponseTemplate.class), ResponseTemplate.class, body);

            if (responseTemplate.isSuccess()) {
                List<?> lstData = this.convertResultToList(responseTemplate.getResult());
                if (!CommonUtils.isEmpty(lstData)) {
                    return (List<Object>) this.convertJavaType(javaType, lstData);
                }
            } else {
                cause.set(new ErrorResponseException(responseTemplate.getError().getCode()));
            }
        } else {
            throw new ResponseTemplateUnsupportedException("Convert do not support Response template class");
        }
        return null;
    }

    /**
     * Read data as Map.
     *
     * @param body  Input Stream from response
     * @param cause store Exception
     * @return Map data
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> readAsMap(Class<?> contextClass, InputStream body, AtomicReference<Throwable> cause) {
        JavaType javaType = this.getJavaType(contextClass, JAVA_TYPE_MAP);

        if (this.validResponseTemplateClass(ResponseTemplate.class)) {
            ResponseTemplate responseTemplate = (ResponseTemplate) readJavaType(this.getJavaType(ResponseTemplate.class), ResponseTemplate.class, body);

            if (responseTemplate.isSuccess()) {
                return (Map<String, Object>) this.convertJavaType(javaType, responseTemplate.getResult());
            } else {
                cause.set(new ErrorResponseException(responseTemplate.getError().getCode()));
            }
        } else {
            throw new ResponseTemplateUnsupportedException("Convert do not support Response template class");
        }
        return null;
    }

    /**
     * Convert result to List.
     * If it's List then just cast it.
     * If it's Map and a first element of Map is List then cast it to List.
     *
     * @param result result need to convert to List
     * @return List
     */
    private List<?> convertResultToList(Object result) {
        List<?> resultConverted = null;
        if (!CommonUtils.isEmpty(result)) {
            if (List.class.isAssignableFrom(result.getClass())) {
                resultConverted = (List<?>) result;
            } else if (Map.class.isAssignableFrom(result.getClass())) {
                Iterator<? extends Map.Entry<?, ?>> entryIterator = ((Map<?, ?>) result).entrySet().iterator();
                Object valueFirstEntry = entryIterator.hasNext() ? entryIterator.next().getValue() : null;
                if (null != valueFirstEntry && List.class.isAssignableFrom(valueFirstEntry.getClass())) {
                    resultConverted = (List<?>) valueFirstEntry;
                }
            }
        }
        return resultConverted;
    }

//    private Map<String, Object> convertRes

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
     * Read object from json by Java Type.
     *
     * @param javaType     Java Type of object
     * @param contextClass Class of object
     * @param body         Input Stream of response
     * @return object is read from stream
     */
    private Object readJavaType(JavaType javaType, Class<?> contextClass, InputStream body) {
        try {
            if (null != contextClass) {
                return this.objectMapper.readerWithView(contextClass).forType(javaType).readValue(body);
            }
            return this.objectMapper.readValue(body, javaType);
        } catch (IOException e) {
            throw new ResponseConverterException("An error occurs while converting json to object", e);
        }
    }

    /**
     * Get JavaType of class.
     *
     * @param clazz class
     * @param type  type collection
     * @return JavaType of class
     */
    private JavaType getJavaType(Class<?> clazz, String type) {
        JavaType javaType = this.getJavaType(clazz);
        switch (type) {
            case JAVA_TYPE_LIST:
                return this.objectMapper.getTypeFactory().constructCollectionType(List.class, javaType);
            case JAVA_TYPE_MAP:
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
}
