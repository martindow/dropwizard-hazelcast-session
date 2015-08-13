package com.unicodecollective.dropwizard.hazelcast.session.testapp;


import com.unicodecollective.dropwizard.hazelcast.session.HazelcastSessionBundle;
import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastSessionConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class TestApplication extends Application<TestConfiguration> {

    @Override
    public void run(TestConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(TestAppRoutes.class);
    }

    @Override
    public void initialize(Bootstrap<TestConfiguration> bootstrap) {
        bootstrap.addBundle(new HazelcastSessionBundle<TestConfiguration>() {
            @Override
            public HazelcastSessionConfig getHazelcastSessionConfig(TestConfiguration configuration) {
                return configuration.getHazelcastSessionConfig();
            }
        });
    }

}
