package com.unicodecollective.dropwizard.hazelcast.session.testapp;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastSessionConfig;
import io.dropwizard.Configuration;


public class TestConfiguration extends Configuration {

    @JsonProperty("hazelcastSession")
    private HazelcastSessionConfig hazelcastSessionConfig;

    public HazelcastSessionConfig getHazelcastSessionConfig() {
        return hazelcastSessionConfig;
    }

}
