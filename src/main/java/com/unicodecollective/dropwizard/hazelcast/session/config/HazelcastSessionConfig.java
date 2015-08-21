package com.unicodecollective.dropwizard.hazelcast.session.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.unicodecollective.dropwizard.hazelcast.session.HazelcastSessionsStoreFactory;
import com.unicodecollective.dropwizard.hazelcast.session.SessionsStore;
import org.glassfish.hk2.api.Factory;

public class HazelcastSessionConfig {

    @JsonProperty("sessionTimeoutSecs")
    private int sessionTimeoutSecs = 15 * 60; // 15 minutes by default

    @JsonProperty("cookie")
    private CookieConfig cookieConfig = new CookieConfig();

    @JsonProperty("hazelcast")
    private HazelcastConfig hazelcastConfig = new HazelcastConfig();

    @JsonProperty("sessionsStoreFactory")
    private Class<? extends Factory<SessionsStore>> sessionsStoreFactoryClass = HazelcastSessionsStoreFactory.class;

    public int getSessionTimeoutSecs() {
        return sessionTimeoutSecs;
    }

    public CookieConfig getCookieConfig() {
        return cookieConfig;
    }

    public Class<? extends Factory<SessionsStore>> getSessionsStoreFactoryClass() {
        return sessionsStoreFactoryClass;
    }

    public HazelcastConfig getHazelcastConfig() {
        return hazelcastConfig;
    }

}
