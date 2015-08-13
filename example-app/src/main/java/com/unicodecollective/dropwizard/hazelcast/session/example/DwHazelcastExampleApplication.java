package com.unicodecollective.dropwizard.hazelcast.session.example;


import com.unicodecollective.dropwizard.hazelcast.session.HazelcastSessionBundle;
import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastSessionConfig;
import com.unicodecollective.dropwizard.hazelcast.session.example.config.DwHazelcastExampleConfiguration;
import com.unicodecollective.dropwizard.hazelcast.session.example.health.HazelcastHealthcheck;
import com.unicodecollective.dropwizard.hazelcast.session.example.resources.DwHazelcastExampleViewsResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.Map;

public class DwHazelcastExampleApplication extends Application<DwHazelcastExampleConfiguration> {

    public static void main(String[] args) throws Exception {
        new DwHazelcastExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-hazelcast-example";
    }

    @Override
    public void initialize(Bootstrap<DwHazelcastExampleConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<DwHazelcastExampleConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(DwHazelcastExampleConfiguration config) {
                return config.getViewConfig();
            }
        });
        bootstrap.addBundle(new HazelcastSessionBundle<DwHazelcastExampleConfiguration>() {
            @Override
            public HazelcastSessionConfig getHazelcastSessionConfig(DwHazelcastExampleConfiguration configuration) {
                return configuration.getHazelcastSessionConfig();
            }
        });
    }

    @Override
    public void run(DwHazelcastExampleConfiguration config, Environment environment) throws Exception {
        environment.healthChecks().register("hazelcast-health", new HazelcastHealthcheck());
        environment.jersey().register(DwHazelcastExampleViewsResource.class);
    }

}
