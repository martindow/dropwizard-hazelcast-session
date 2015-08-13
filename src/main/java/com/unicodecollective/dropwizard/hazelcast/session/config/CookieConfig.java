package com.unicodecollective.dropwizard.hazelcast.session.config;


import com.fasterxml.jackson.annotation.JsonProperty;

import static javax.ws.rs.core.NewCookie.DEFAULT_MAX_AGE;

public class CookieConfig {

    @JsonProperty("name")
    private String cookieName = "dw-hc-session";

    @JsonProperty("path")
    private String cookiePath = null;

    @JsonProperty("domain")
    private String cookieDomain = null;

    @JsonProperty("comment")
    private String cookieComment = null;

    @JsonProperty("secure")
    private boolean cookieSecure = false;

    @JsonProperty("httpOnly")
    private boolean cookieHttpOnly = false;

    @JsonProperty("maxAge")
    private int cookieMaxAge = DEFAULT_MAX_AGE;

    public String getCookieName() {
        return cookieName;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public String getCookieComment() {
        return cookieComment;
    }

    public boolean isCookieSecure() {
        return cookieSecure;
    }

    public boolean isCookieHttpOnly() {
        return cookieHttpOnly;
    }

    public int getCookieMaxAge() {
        return cookieMaxAge;
    }

}
