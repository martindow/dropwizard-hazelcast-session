package com.unicodecollective.dropwizard.hazelcast.session;

import com.hazelcast.core.HazelcastInstance;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SessionObjectResolver implements InjectionResolver<Session> {

    public static final String SESSION_MAP_REQUEST_PROPERTY = SessionObjectResolver.class.getCanonicalName() + ".session-map";
    public static final String SESSION_ID_REQUEST_PROPERTY = SessionObjectResolver.class.getCanonicalName() + ".new-session-id";
    public static final String NEW_SESSION_ID_SET_REQUEST_PROPERTY = SessionObjectResolver.class.getCanonicalName() + ".new-session-id-set";
    public static final String HAZELCAST_SESSIONS_MAP_KEY = SessionObjectResolver.class.getCanonicalName() + "SetSessionIdResponseFilter.class.getCanonicalName() + .sessions";

    @Inject
    private ServiceLocator serviceLocator;

    @Inject
    private HazelcastSessionConfig config;

    @Inject
    private HazelcastInstance hazelcastInstance;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        ContainerRequestContext requestContext = serviceLocator.getService(ContainerRequestContext.class);

        Map<Object, Object> allSessionsMap = hazelcastInstance.getMap(HAZELCAST_SESSIONS_MAP_KEY);
        HashMap<Object, Object> thisSessionMap = null;

        String sessionId = null;
        Cookie sessionCookie = requestContext.getCookies().get(config.getCookieName());
        if (sessionCookie != null) {
            sessionId = sessionCookie.getValue();
            if (isNotBlank(sessionId) && allSessionsMap.containsKey(sessionId)) {
                thisSessionMap = (HashMap) allSessionsMap.get(sessionId);
            }
        }
        if (thisSessionMap == null) {
            sessionId = randomUUID().toString();
            requestContext.setProperty(NEW_SESSION_ID_SET_REQUEST_PROPERTY, sessionId);
            thisSessionMap = new HashMap<>();
            allSessionsMap.put(sessionId, thisSessionMap);
        }
        requestContext.setProperty(SESSION_ID_REQUEST_PROPERTY, sessionId);
        requestContext.setProperty(SESSION_MAP_REQUEST_PROPERTY, thisSessionMap);

        Type requiredType = injectee.getRequiredType();
        if (!(requiredType instanceof Class)) {
            throw new RuntimeException("Only class are supported by dropwizard-hazelcast-session - found: " + requiredType);
        }
        String requiredTypeName = ((Class) requiredType).getName();
        if (thisSessionMap.containsKey(requiredTypeName)) {
            return thisSessionMap.get(requiredTypeName);
        } else {
            try {
                Object newInstance = Class.forName(requiredTypeName).newInstance();
                thisSessionMap.put(requiredTypeName, newInstance);
                return newInstance;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}