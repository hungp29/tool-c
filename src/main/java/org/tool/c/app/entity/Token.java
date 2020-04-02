package org.tool.c.app.entity;

import java.util.Objects;

/**
 * Authentication Token.
 */
public class Token {

    private String token;

    private long tokenExpiration;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setTokenExpiration(long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public long getTokenExpiration() {
        return tokenExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token that = (Token) o;
        return tokenExpiration == that.tokenExpiration && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tokenExpiration);
    }
}
