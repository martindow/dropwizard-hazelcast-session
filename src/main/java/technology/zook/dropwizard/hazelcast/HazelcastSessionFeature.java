package technology.zook.dropwizard.hazelcast;


import com.hazelcast.core.HazelcastInstance;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class HazelcastSessionFeature implements DynamicFeature {

    private final HazelcastInstance hazelcastInstance;
    private final String cookieName;
    private final int cookieMaxAgeSecs;
    private final String cookiePath;
    private final String cookieDomain;
    private final String cookieComment;
    private final boolean cookieSecure;
    private final boolean cookieHttpOnly;

    public HazelcastSessionFeature(HazelcastInstance hazelcastInstance,
                                   String cookieName, int cookieMaxAgeSecs) {
        this(hazelcastInstance, cookieName, cookieMaxAgeSecs, null, null, null, false, false);
    }

    public HazelcastSessionFeature(HazelcastInstance hazelcastInstance,
                                   String cookieName, int cookieMaxAgeSecs, String cookieDomain, String cookieComment, String cookiePath,
                                   boolean cookieSecure, boolean cookieHttpOnly) {
        this.hazelcastInstance = hazelcastInstance;
        this.cookieName = cookieName;
        this.cookieMaxAgeSecs = cookieMaxAgeSecs;
        this.cookiePath = cookiePath;
        this.cookieDomain = cookieDomain;
        this.cookieComment = cookieComment;
        this.cookieSecure = cookieSecure;
        this.cookieHttpOnly = cookieHttpOnly;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (resourceInfo.getResourceMethod().getAnnotation(Session.class) != null) {
            context.register(new HazelcastSessionFilter(hazelcastInstance, cookieName,
                    cookieMaxAgeSecs, cookiePath, cookieDomain, cookieComment, cookieSecure, cookieHttpOnly));
        }
    }

}
