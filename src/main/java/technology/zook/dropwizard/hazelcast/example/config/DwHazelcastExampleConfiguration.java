package technology.zook.dropwizard.hazelcast.example.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import technology.zook.dropwizard.hazelcast.session.HazelcastSessionConfig;

import java.util.Map;


public class DwHazelcastExampleConfiguration extends Configuration {

    @JsonProperty("hazelcastSessionConfig")
    private HazelcastSessionConfig hazelcastSessionConfig;

    @JsonProperty("views")
    private Map<String, Map<String, String>> viewConfig;

    public HazelcastSessionConfig getHazelcastSessionConfig() {
        return hazelcastSessionConfig;
    }

    public Map<String, Map<String, String>> getViewConfig() {
        return viewConfig;
    }
}
