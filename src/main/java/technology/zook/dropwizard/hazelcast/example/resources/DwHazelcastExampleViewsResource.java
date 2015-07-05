package technology.zook.dropwizard.hazelcast.example.resources;

import technology.zook.dropwizard.hazelcast.example.views.IndexPageView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/")
@Produces(TEXT_HTML)
public class DwHazelcastExampleViewsResource {

    @GET
    public IndexPageView indexPage() {
        return new IndexPageView();
    }


}
