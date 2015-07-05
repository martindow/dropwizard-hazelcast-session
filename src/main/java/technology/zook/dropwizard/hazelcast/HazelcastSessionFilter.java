package technology.zook.dropwizard.hazelcast;


import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HazelcastSessionFilter implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String SESSION_MAP_REQUEST_PROPERTY = "technology.zook.dropwizard.hazelcast.session-map";
    public static final String SESSION_ID_REQUEST_PROPERTY = "technology.zook.dropwizard.hazelcast.new-session-id";
    public static final String NEW_SESSION_ID_SET_REQUEST_PROPERTY = "technology.zook.dropwizard.hazelcast.new-session-id-set";
    public static final String HAZELCAST_SESSIONS_MAP_KEY = "technology.zook.dropwizard.hazelcast.sessions";

    private final String cookieName;
    private final int cookieMaxAgeSecs;
    private final String cookiePath;
    private final String cookieDomain;
    private final String cookieComment;
    private final boolean cookieSecure;
    private final boolean cookieHttpOnly;
    private final HazelcastInstance hazelcastInstance;

    public HazelcastSessionFilter(HazelcastInstance hazelcastInstance,
                                  String cookieName, int cookieMaxAgeSecs) {
        this(hazelcastInstance, cookieName, cookieMaxAgeSecs, null, null, null, false, false);
    }

    public HazelcastSessionFilter(HazelcastInstance hazelcastInstance,
                                  String cookieName, int cookieMaxAgeSecs, String cookiePath, String cookieDomain, String cookieComment,
                                  boolean cookieSecure, boolean cookieHttpOnly) {
        this.hazelcastInstance = hazelcastInstance;
        this.cookieName = cookieName;
        this.cookieMaxAgeSecs = cookieMaxAgeSecs;
        this.cookiePath = cookiePath;
        this.cookieDomain = cookieDomain;
        this.cookieComment = cookieComment;
        this.cookieSecure = cookieSecure;
        this.cookieHttpOnly = cookieHttpOnly;
        MapConfig sessionsMapConfig = hazelcastInstance.getConfig().getMapConfig(HAZELCAST_SESSIONS_MAP_KEY);
        sessionsMapConfig.setMaxIdleSeconds(cookieMaxAgeSecs);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Map<Object, Object> allSessionsMap = hazelcastInstance.getMap(HAZELCAST_SESSIONS_MAP_KEY);
        Map<Object, Object> thisSessionMap = null;

        String sessionId = null;
        Cookie sessionCookie = requestContext.getCookies().get(cookieName);
        if (sessionCookie != null) {
            sessionId = sessionCookie.getValue();
            if (isNotBlank(sessionId) && allSessionsMap.containsKey(sessionId)) {
                thisSessionMap = (Map<Object, Object>) allSessionsMap.get(sessionId);
            }
        }
        if (thisSessionMap == null) {
            sessionId = randomUUID().toString();
            requestContext.setProperty(NEW_SESSION_ID_SET_REQUEST_PROPERTY, true);
            thisSessionMap = new HashMap<>();
            allSessionsMap.put(sessionId, thisSessionMap);
        }

        requestContext.setProperty(SESSION_ID_REQUEST_PROPERTY, sessionId);
        requestContext.setProperty(SESSION_MAP_REQUEST_PROPERTY, thisSessionMap);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String sessionId = (String) requestContext.getProperty(SESSION_ID_REQUEST_PROPERTY);
        if (requestContext.getProperty(NEW_SESSION_ID_SET_REQUEST_PROPERTY) != null) {
            NewCookie newSessionCookie = new NewCookie(cookieName, sessionId, cookieDomain, cookiePath, cookieComment,
                    -1, cookieSecure, cookieHttpOnly);
            responseContext.getHeaders().add("Set-Cookie", newSessionCookie);
        }
        hazelcastInstance.getMap(HAZELCAST_SESSIONS_MAP_KEY).put(sessionId,
                requestContext.getProperty(SESSION_MAP_REQUEST_PROPERTY));
    }

}
