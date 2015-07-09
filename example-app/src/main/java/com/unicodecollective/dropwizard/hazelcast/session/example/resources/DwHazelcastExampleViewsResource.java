package com.unicodecollective.dropwizard.hazelcast.session.example.resources;

import com.unicodecollective.dropwizard.hazelcast.session.Session;
import com.unicodecollective.dropwizard.hazelcast.session.example.MyAppSession;
import com.unicodecollective.dropwizard.hazelcast.session.example.views.IndexPageView;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/")
@Produces(TEXT_HTML)
public class DwHazelcastExampleViewsResource {

    @Session
    private MyAppSession myAppSession;

    @GET
    public IndexPageView indexPage() {
        return new IndexPageView(myAppSession.getName());
    }

    @POST
    @Path("name")
    public Response submitName(@FormParam("name") String name) {
        myAppSession.setName(name);
        return Response.seeOther(URI.create("/")).build();
    }

}
