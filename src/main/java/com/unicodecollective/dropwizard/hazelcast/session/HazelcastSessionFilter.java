package com.unicodecollective.dropwizard.hazelcast.session;


import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HazelcastSessionFilter implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String SESSION_MAP_REQUEST_PROPERTY = "technology.zook.dropwizard.hazelcast.session-map";
    public static final String SESSION_ID_REQUEST_PROPERTY = "technology.zook.dropwizard.hazelcast.new-session-id";
    public static final String NEW_SESSION_ID_SET_REQUEST_PROPERTY = "technology.zook.dropwizard.hazelcast.new-session-id-set";
    public static final String HAZELCAST_SESSIONS_MAP_KEY = "technology.zook.dropwizard.hazelcast.sessions";

    private final HazelcastInstance hazelcastInstance;
    private final HazelcastSessionConfig config;

    public HazelcastSessionFilter(HazelcastInstance hazelcastInstance, HazelcastSessionConfig config) {
        this.hazelcastInstance = hazelcastInstance;
        this.config = config;
        MapConfig sessionsMapConfig = hazelcastInstance.getConfig().getMapConfig(HAZELCAST_SESSIONS_MAP_KEY);
        sessionsMapConfig.setMaxIdleSeconds(config.getSessionTimeoutSecs());
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Map<Object, Object> allSessionsMap = hazelcastInstance.getMap(HAZELCAST_SESSIONS_MAP_KEY);
        Session thisSessionMap = null;

        String sessionId = null;
        Cookie sessionCookie = requestContext.getCookies().get(config.getCookieName());
        if (sessionCookie != null) {
            sessionId = sessionCookie.getValue();
            if (isNotBlank(sessionId) && allSessionsMap.containsKey(sessionId)) {
                thisSessionMap = (Session) allSessionsMap.get(sessionId);
            }
        }
        if (thisSessionMap == null) {
            sessionId = randomUUID().toString();
            requestContext.setProperty(NEW_SESSION_ID_SET_REQUEST_PROPERTY, true);
            thisSessionMap = new Session();
            allSessionsMap.put(sessionId, thisSessionMap);
        }

        requestContext.setProperty(SESSION_ID_REQUEST_PROPERTY, sessionId);
        requestContext.setProperty(SESSION_MAP_REQUEST_PROPERTY, thisSessionMap);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String sessionId = (String) requestContext.getProperty(SESSION_ID_REQUEST_PROPERTY);
        if (requestContext.getProperty(NEW_SESSION_ID_SET_REQUEST_PROPERTY) != null) {
            // Note: cookie maxAge is -1 below: session timeout is handled with Hazelcast above: sessionsMapConfig.setMaxIdleSeconds(cookieMaxAgeSecs)
            NewCookie newSessionCookie = new NewCookie(config.getCookieName(), sessionId, config.getCookieDomain(),
                    config.getCookiePath(), config.getCookieComment(), -1, config.isCookieSecure(), config.isCookieHttpOnly());
            responseContext.getHeaders().add("Set-Cookie", newSessionCookie);
        }
        hazelcastInstance.getMap(HAZELCAST_SESSIONS_MAP_KEY).put(sessionId,
                requestContext.getProperty(SESSION_MAP_REQUEST_PROPERTY));
    }

}
