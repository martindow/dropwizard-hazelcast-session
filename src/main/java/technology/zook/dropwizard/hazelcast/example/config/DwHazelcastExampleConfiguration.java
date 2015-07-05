package technology.zook.dropwizard.hazelcast.example.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.util.Map;


public class DwHazelcastExampleConfiguration extends Configuration {

    @JsonProperty("views")
    private Map<String, Map<String, String>> viewConfig;

    public Map<String, Map<String, String>> getViewConfig() {
        return viewConfig;
    }
}
