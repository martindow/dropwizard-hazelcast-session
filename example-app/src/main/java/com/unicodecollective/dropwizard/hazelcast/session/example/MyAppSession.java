package com.unicodecollective.dropwizard.hazelcast.session.example;


import java.io.Serializable;

public class MyAppSession implements Serializable {

    private static final long serialVersionUID = 11L;

    private String name = "Somebody";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
