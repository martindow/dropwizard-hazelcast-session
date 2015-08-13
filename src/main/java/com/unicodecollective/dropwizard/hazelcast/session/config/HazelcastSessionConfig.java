package com.unicodecollective.dropwizard.hazelcast.session.config;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class HazelcastSessionConfig {

    @JsonProperty("sessionTimeoutSecs")
    private int sessionTimeoutSecs = 15 * 60; // 15 minutes by default

    @JsonProperty("cookie")
    private CookieConfig cookieConfig = new CookieConfig();

    @JsonProperty("hazelcast")
    private HazelcastConfig hazelcastConfig;

    public int getSessionTimeoutSecs() {
        return sessionTimeoutSecs;
    }

    public CookieConfig getCookieConfig() {
        return cookieConfig;
    }

    public HazelcastConfig getHazelcastConfig() {
        return hazelcastConfig;
    }

}
