package com.unicodecollective.dropwizard.hazelcast.session;

import com.unicodecollective.dropwizard.hazelcast.session.config.HazelcastSessionConfig;
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

    @Inject
    private ServiceLocator serviceLocator;

    @Inject
    private HazelcastSessionConfig config;

    @Inject
    private SessionsStore sessionsStore;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        ContainerRequestContext requestContext = serviceLocator.getService(ContainerRequestContext.class);

        Map<Object, Object> allSessionsMap = sessionsStore.get();
        HashMap<Object, Object> thisSessionMap = (HashMap<Object, Object>) requestContext.getProperty(SESSION_MAP_REQUEST_PROPERTY);

        String sessionId = (String) requestContext.getProperty(SESSION_ID_REQUEST_PROPERTY);
        if (sessionId == null) {
            Cookie sessionCookie = requestContext.getCookies().get(config.getCookieConfig().getCookieName());
            if (sessionCookie != null) {
                sessionId = sessionCookie.getValue();
                if (isNotBlank(sessionId) && allSessionsMap.containsKey(sessionId)) {
                    thisSessionMap = (HashMap) allSessionsMap.get(sessionId);
                }
            } else {
                sessionId = randomUUID().toString();
                requestContext.setProperty(NEW_SESSION_ID_SET_REQUEST_PROPERTY, sessionId);
            }
            requestContext.setProperty(SESSION_ID_REQUEST_PROPERTY, sessionId);
        }
        if (thisSessionMap == null) {
            thisSessionMap = new HashMap<>();
            allSessionsMap.put(sessionId, thisSessionMap);
        }
        if (requestContext.getProperty(SESSION_MAP_REQUEST_PROPERTY) == null && thisSessionMap != null) {
            requestContext.setProperty(SESSION_MAP_REQUEST_PROPERTY, thisSessionMap);
        }

        Type requiredType = injectee.getRequiredType();
        if (!(requiredType instanceof Class)) {
            throw new RuntimeException("Only class types are supported by dropwizard-hazelcast-session - found: " + requiredType);
        }
        String requiredTypeName = ((Class) requiredType).getName();
        if (thisSessionMap.containsKey(requiredTypeName)) {
            return thisSessionMap.get(requiredTypeName);
        } else {
            try {
                // TODO: Accept a factory to construct the class?
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