package technology.zook.dropwizard.hazelcast.example.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.util.Map;


public class DwHazelcastExampleConfiguration extends Configuration {

    @JsonProperty("sessionTimeoutSecs")
    private int sessionTimeoutSecs = 15 * 60; // 15 mins by default.

    @JsonProperty("views")
    private Map<String, Map<String, String>> viewConfig;

    public int getSessionTimeoutSecs() {
        return sessionTimeoutSecs;
    }

    public Map<String, Map<String, String>> getViewConfig() {
        return viewConfig;
    }
}
