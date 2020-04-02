package org.tool.c.services.http;

import java.util.*;

/**
 * Http Header class.
 */
public class HttpHeaders implements Map<String, List<String>> {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String AUTHORIZATION = "Authorization";

    private final Map<String, List<String>> headers;

    private final boolean readOnly;

    public HttpHeaders() {
        this(new LinkedHashMap<>(), false);
    }

    private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
        if (readOnly) {
            Map<String, List<String>> map = new LinkedHashMap<>(headers);
            headers.forEach((key, valueList) -> map.put(key, Collections.unmodifiableList(valueList)));
            this.headers = Collections.unmodifiableMap(map);
        } else {
            this.headers = headers;
        }
        this.readOnly = readOnly;
    }

    @Override
    public int size() {
        return this.headers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return this.headers.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return this.headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return this.headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {
        this.headers.putAll(m);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return this.headers.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpHeaders that = (HttpHeaders) o;
        return readOnly == that.readOnly &&
                Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return this.headers.hashCode();
    }

    /**
     * New HttpHeaders instance with context type.
     *
     * @param contextType the context type of request.
     * @return HttpHeaders
     */
    public static HttpHeaders newInstance(String contextType) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, Arrays.asList(contextType));

        return new HttpHeaders(headers, false);
    }

    /**
     * New HttpHeaders instance with context type and authorization.
     *
     * @param contextType   the context type of request.
     * @param authorization the access token
     * @return HttpHeaders
     */
    public static HttpHeaders newInstanceAuth(String contextType, String authorization) {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, Collections.singletonList(contextType));
        headers.put(AUTHORIZATION, Collections.singletonList(authorization));

        return new HttpHeaders(headers, false);
    }
}
