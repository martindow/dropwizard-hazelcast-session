package com.unicodecollective.dropwizard.hazelcast.session;


import java.util.Map;

public interface SessionsStore {

    Map<Object, Object> get();

    void dispose();

}
