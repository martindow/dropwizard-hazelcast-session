package com.unicodecollective.dropwizard.hazelcast.session;


import com.hazelcast.core.HazelcastInstance;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

public class HazelcastSessionBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private final HazelcastInstance hazelcastInstance;
    private final HazelcastSessionConfig config;

    public HazelcastSessionBundle() {
        this(newHazelcastInstance());
    }

    public HazelcastSessionBundle(HazelcastInstance hazelcastInstance) {
        this(hazelcastInstance, new HazelcastSessionConfig());
    }

    public HazelcastSessionBundle(HazelcastSessionConfig config) {
        this(newHazelcastInstance(), config);
    }

    public HazelcastSessionBundle(HazelcastInstance hazelcastInstance, HazelcastSessionConfig config) {
        this.hazelcastInstance = hazelcastInstance;
        this.config = config;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(T configuration, Environment environment) {
        final HazelcastSessionConfig hazelcastSessionConfig = getHazelcastSessionConfig(configuration);
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
        return config;
    }

}
