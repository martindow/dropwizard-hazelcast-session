package com.unicodecollective.dropwizard.hazelcast.session;


import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastSessionConfig;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import java.util.Map;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

public class HazelcastSessionBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private static final HazelcastSessionConfig DEFAULT_CONFIG = new HazelcastSessionConfig();

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(T configuration, Environment environment) {
        final HazelcastSessionConfig hazelcastSessionConfig = getHazelcastSessionConfig(configuration);
        Config hcHazelcastConfig = new Config();
        for (Map.Entry<String, String> hazelcastConfigProperty : hazelcastSessionConfig.getHazelcastConfig().entrySet()) {
            hcHazelcastConfig.setProperty(hazelcastConfigProperty.getKey(), hazelcastConfigProperty.getValue());
        }
        final HazelcastInstance hazelcastInstance = newHazelcastInstance(hcHazelcastConfig);
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(hazelcastInstance).to(HazelcastInstance.class);
                bind(hazelcastSessionConfig).to(HazelcastSessionConfig.class);
                bind(SessionObjectResolver.class)
                        .to(new TypeLiteral<InjectionResolver<Session>>() {
                        })
                        .in(Singleton.class);
            }
        });
        environment.lifecycle().manage(new HazelcastInstanceManager(hazelcastInstance));
        environment.jersey().register(SetSessionIdResponseFilter.class);
    }

    public HazelcastSessionConfig getHazelcastSessionConfig(T configuration) {
        return DEFAULT_CONFIG;
    }

}
