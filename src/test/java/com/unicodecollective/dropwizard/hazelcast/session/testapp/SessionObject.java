package com.unicodecollective.dropwizard.hazelcast.session.testapp;

import java.io.Serializable;


public class SessionObject implements Serializable {

    private static final long serialVersionUID = 1l;

    private String thing;

    public String getThing() {
        return thing;
    }

    public void setThing(String thing) {
        this.thing = thing;
    }

}
