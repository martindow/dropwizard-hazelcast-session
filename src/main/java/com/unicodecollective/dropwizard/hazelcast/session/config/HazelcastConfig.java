package com.unicodecollective.dropwizard.hazelcast.session.config;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class HazelcastConfig {

    @JsonProperty("properties")
    private Map<String, String> properties = new HashMap<>();

    @JsonProperty("multicastGroup")
    private String multicastGroup;

    @JsonProperty("multicastPort")
    private Integer multicastPort;

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getMulticastGroup() {
        return multicastGroup;
    }

    public Integer getMulticastPort() {
        return multicastPort;
    }

}
