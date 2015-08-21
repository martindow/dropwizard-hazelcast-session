package com.unicodecollective.dropwizard.hazelcast.session;

import com.hazelcast.config.Config;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.core.HazelcastInstance;
import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastConfig;
import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastSessionConfig;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Map;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

public class HazelcastSessionsStoreFactory extends BaseSessionsStoreFactory {

    public static final String HAZELCAST_SESSIONS_MAP_KEY = SessionObjectResolver.class.getCanonicalName() + "SetSessionIdResponseFilter.class.getCanonicalName() + .sessions";

    private static final Logger LOGGER = getLogger(HazelcastSessionsStoreFactory.class);

    @Inject
    private HazelcastSessionConfig hazelcastSessionConfig;

    @Override
    public SessionsStore provide() {
        LOGGER.info("Starting Hazelcast instance");
        HazelcastConfig hazelcastConfig = hazelcastSessionConfig.getHazelcastConfig();
        Config hcHazelcastConfig = new Config();
        for (Map.Entry<String, String> hazelcastConfigProperty : hazelcastConfig.getProperties().entrySet()) {
            hcHazelcastConfig.setProperty(hazelcastConfigProperty.getKey(), hazelcastConfigProperty.getValue());
        }
        MulticastConfig multicastConfig = hcHazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig();
        String multicastGroup = hazelcastConfig.getMulticastGroup();
        if (isNotBlank(multicastGroup)) {
            multicastConfig.setMulticastGroup(multicastGroup);
        }
        Integer multicastPort = hazelcastConfig.getMulticastPort();
        if (multicastPort != null) {
            multicastConfig.setMulticastPort(multicastPort);
        }
        HazelcastInstance hazelcastInstance = newHazelcastInstance(hcHazelcastConfig);
        return new HazelcastSessionsStore(hazelcastInstance);
    }

    public static final class HazelcastSessionsStore implements SessionsStore {

        private final HazelcastInstance hazelcastInstance;

        public HazelcastSessionsStore(HazelcastInstance hazelcastInstance) {
            this.hazelcastInstance = hazelcastInstance;
        }

        @Override
        public Map<Object, Object> get() {
            return hazelcastInstance.getMap(HAZELCAST_SESSIONS_MAP_KEY);
        }

        @Override
        public void dispose() {
            LOGGER.info("Shutting down Hazelcast instance");
            hazelcastInstance.shutdown();
        }

    }
}
