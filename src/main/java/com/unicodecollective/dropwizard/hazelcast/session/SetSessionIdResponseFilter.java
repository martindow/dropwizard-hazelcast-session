package com.unicodecollective.dropwizard.hazelcast.session;


import com.hazelcast.core.HazelcastInstance;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;

import static com.unicodecollective.dropwizard.hazelcast.session.SessionObjectResolver.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SetSessionIdResponseFilter implements ContainerResponseFilter {

    @Inject
    private HazelcastSessionConfig config;

    @Inject
    private HazelcastInstance hazelcastInstance;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String sessionId = (String) requestContext.getProperty(SESSION_ID_REQUEST_PROPERTY);
        if (isNotBlank(sessionId)) {
            if (requestContext.getProperty(NEW_SESSION_ID_SET_REQUEST_PROPERTY) != null) {
                NewCookie newSessionCookie = new NewCookie(config.getCookieName(), sessionId, config.getCookieDomain(),
                        config.getCookiePath(), config.getCookieComment(), config.getCookieMaxAge(),
                        config.isCookieSecure(), config.isCookieHttpOnly());
                responseContext.getHeaders().add("Set-Cookie", newSessionCookie);
            }
            hazelcastInstance.getMap(HAZELCAST_SESSIONS_MAP_KEY).put(sessionId, requestContext.getProperty(SESSION_MAP_REQUEST_PROPERTY));
        }
    }

}
