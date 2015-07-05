package technology.zook.dropwizard.hazelcast.example.resources;

import technology.zook.dropwizard.hazelcast.HazelcastSessionFilter;
import technology.zook.dropwizard.hazelcast.Session;
import technology.zook.dropwizard.hazelcast.example.views.IndexPageView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Path("/")
@Produces(TEXT_HTML)
public class DwHazelcastExampleViewsResource {

    @Session
    @GET
    public IndexPageView indexPage(@Context ContainerRequestContext requestContext, @QueryParam("name") String newName) {
        Map<Object, Object> session = (Map<Object, Object>) requestContext.getProperty(HazelcastSessionFilter.SESSION_MAP_REQUEST_PROPERTY);
        if (isNotBlank(newName)) {
            session.put("name", newName);
        }
        return new IndexPageView(session.containsKey("name") ? (String) session.get("name") : "Somebody");
    }

}
