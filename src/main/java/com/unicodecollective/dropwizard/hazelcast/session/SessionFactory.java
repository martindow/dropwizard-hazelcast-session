package com.unicodecollective.dropwizard.hazelcast.session;


import org.glassfish.hk2.api.Factory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

public class SessionFactory implements Factory<Session> {

    private final ContainerRequestContext requestContext;

    public SessionFactory(@Context ContainerRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public Session provide() {
        return (Session) requestContext.getProperty(HazelcastSessionFilter.SESSION_MAP_REQUEST_PROPERTY);
    }

    @Override
    public void dispose(Session session) {
    }
}
