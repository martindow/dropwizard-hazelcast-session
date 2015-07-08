package com.unicodecollective.dropwizard.hazelcast.session;


import com.hazelcast.core.HazelcastInstance;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.List;

@Provider
public class HazelcastSessionFeature implements DynamicFeature {

    private final HazelcastInstance hazelcastInstance;
    private final HazelcastSessionConfig hazelcastSessionConfig;

    public HazelcastSessionFeature(HazelcastInstance hazelcastInstance, HazelcastSessionConfig hazelcastSessionConfig) {
        this.hazelcastInstance = hazelcastInstance;
        this.hazelcastSessionConfig = hazelcastSessionConfig;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        List<Class<?>> parameterTypes = Arrays.asList(resourceInfo.getResourceMethod().getParameterTypes());
        if (parameterTypes.contains(Session.class)) {
            context.register(new HazelcastSessionFilter(hazelcastInstance, hazelcastSessionConfig));
        }
    }

}
