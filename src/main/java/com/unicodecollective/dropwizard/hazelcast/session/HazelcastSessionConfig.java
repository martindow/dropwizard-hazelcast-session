package com.unicodecollective.dropwizard.hazelcast.session;


import com.fasterxml.jackson.annotation.JsonProperty;

import static javax.ws.rs.core.NewCookie.DEFAULT_MAX_AGE;

public class HazelcastSessionConfig {

    @JsonProperty("sessionTimeoutSecs")
    private int sessionTimeoutSecs = 15 * 60; // 15 minutes by default

    @JsonProperty("cookieName")
    private String cookieName = "dw-hc-session";

    @JsonProperty("cookiePath")
    private String cookiePath = null;

    @JsonProperty("cookieDomain")
    private String cookieDomain = null;

    @JsonProperty("cookieComment")
    private String cookieComment = null;

    @JsonProperty("cookieSecure")
    private boolean cookieSecure = false;

    @JsonProperty("cookieHttpOnly")
    private boolean cookieHttpOnly = false;

    @JsonProperty("cookieMaxAge")
    private int cookieMaxAge = DEFAULT_MAX_AGE;

    public int getSessionTimeoutSecs() {
        return sessionTimeoutSecs;
    }

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
