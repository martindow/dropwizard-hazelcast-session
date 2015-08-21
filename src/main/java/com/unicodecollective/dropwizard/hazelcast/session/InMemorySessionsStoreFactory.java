package com.unicodecollective.dropwizard.hazelcast.session;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastSessionConfig;

import javax.inject.Inject;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;

public class InMemorySessionsStoreFactory extends BaseSessionsStoreFactory {

    @Inject
    private HazelcastSessionConfig hazelcastSessionConfig;

    @Override
    public SessionsStore provide() {
        return new InMemorySessionsStore(hazelcastSessionConfig);
    }

    public static class InMemorySessionsStore implements SessionsStore {

        private Cache<Object, Object> cache;

        public InMemorySessionsStore(HazelcastSessionConfig hazelcastSessionConfig) {
            cache = CacheBuilder.newBuilder()
                    .expireAfterAccess(hazelcastSessionConfig.getSessionTimeoutSecs(), SECONDS)
                    .build();
        }

        @Override
        public Map<Object, Object> get() {
            return cache.asMap();
        }

        @Override
        public void dispose() {
            cache.cleanUp();
            cache = null;
        }

    }
}
