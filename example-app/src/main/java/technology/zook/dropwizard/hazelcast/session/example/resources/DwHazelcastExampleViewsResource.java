package technology.zook.dropwizard.hazelcast.session.example.resources;

import technology.zook.dropwizard.hazelcast.session.Session;
import technology.zook.dropwizard.hazelcast.session.example.views.IndexPageView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Path("/")
@Produces(TEXT_HTML)
public class DwHazelcastExampleViewsResource {

    @GET
    public IndexPageView indexPage(@Context ContainerRequestContext requestContext,
                                   @QueryParam("name") String newName,
                                   @Context Session session) {
        if (isNotBlank(newName)) {
            session.put("name", newName);
        }
        return new IndexPageView(session.containsKey("name") ? (String) session.get("name") : "Somebody");
    }

}
