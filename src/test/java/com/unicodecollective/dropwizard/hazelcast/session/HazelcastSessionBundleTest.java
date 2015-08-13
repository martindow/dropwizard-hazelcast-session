package com.unicodecollective.dropwizard.hazelcast.session;

import com.unicodecollective.dropwizard.hazelcast.session.testapp.TestApplication;
import com.unicodecollective.dropwizard.hazelcast.session.testapp.TestConfiguration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.junit.Assert.assertEquals;


public class HazelcastSessionBundleTest {

    @ClassRule
    public static final DropwizardAppRule<TestConfiguration> TEST_APP =
            new DropwizardAppRule<>(TestApplication.class, resourceFilePath("test-app-config.yml"));


    @Test
    public void setAndGet() {
        Response response = getThing(null);
        NewCookie sessionCookie = response.getCookies().get("testapp-session");
        assertEquals("", response.readEntity(String.class));
        String sessionThing = "My session thing!";
        postThingyToSession(sessionThing, sessionCookie);

        assertEquals(sessionThing, getThing(sessionCookie).readEntity(String.class));
    }

    private void postThingyToSession(String sessionThing, NewCookie sessionCookie) {
        getPath().queryParam("thing", sessionThing)
                .request()
                .cookie(sessionCookie)
                .post(Entity.text(""));
    }

    private Response getThing(NewCookie sessionCookie) {
        JerseyInvocation.Builder request = getPath().request();
        if (sessionCookie != null) {
            request.cookie(sessionCookie);
        }
        return request.get();
    }

    private JerseyWebTarget getPath() {
        return new JerseyClientBuilder().build()
                .target("http://localhost:8745")
                .path("/thing");
    }

}