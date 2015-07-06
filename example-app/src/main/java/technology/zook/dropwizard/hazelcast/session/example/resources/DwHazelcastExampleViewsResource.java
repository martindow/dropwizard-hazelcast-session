package technology.zook.dropwizard.hazelcast.session.example.resources;

import technology.zook.dropwizard.hazelcast.session.Session;
import technology.zook.dropwizard.hazelcast.session.example.views.IndexPageView;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/")
@Produces(TEXT_HTML)
public class DwHazelcastExampleViewsResource {

    public static final String NAME_PARAM = "name";

    @GET
    public IndexPageView indexPage(@Context Session session) {
        return new IndexPageView(session.containsKey(NAME_PARAM) ? (String) session.get(NAME_PARAM) : "Somebody");
    }

    @POST
    @Path("name")
    public Response submitName(@Context Session session, @FormParam("name") String name) {
        session.put(NAME_PARAM, name);
        return Response.seeOther(URI.create("/")).build();
    }

}
