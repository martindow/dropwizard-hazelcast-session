package com.unicodecollective.dropwizard.hazelcast.session;


import com.hazelcast.core.HazelcastInstance;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.slf4j.LoggerFactory.getLogger;

public class HazelcastInstanceManager implements Managed {

    private static final Logger LOGGER = getLogger(HazelcastInstanceManager.class);

    private HazelcastInstance hazelcastInstance;

    public HazelcastInstanceManager(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Shutting down Hazelcast instance.");
        hazelcastInstance.shutdown();
    }

}
