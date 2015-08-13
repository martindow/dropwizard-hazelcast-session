package com.unicodecollective.dropwizard.hazelcast.session.testapp;

import com.unicodecollective.dropwizard.hazelcast.session.Session;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


@Path(TestAppRoutes.THING_PATH)
public class TestAppRoutes {

    public static final String THING_PATH = "/thing";

    @Session
    private SessionObject sessionObject;

    @GET
    public String getThingy() {
        return sessionObject.getThing();
    }

    @POST
    public Response setThing(@QueryParam("thing") String thingy) throws URISyntaxException {
        sessionObject.setThing(thingy);
        return Response.seeOther(new URI(THING_PATH)).build();
    }

}
