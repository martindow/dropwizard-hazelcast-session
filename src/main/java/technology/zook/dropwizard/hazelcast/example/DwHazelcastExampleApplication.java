package technology.zook.dropwizard.hazelcast.example;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import technology.zook.dropwizard.hazelcast.example.config.DwHazelcastExampleConfiguration;
import technology.zook.dropwizard.hazelcast.example.health.HazelcastHealthcheck;
import technology.zook.dropwizard.hazelcast.example.resources.DwHazelcastExampleViewsResource;

import java.util.Map;

public class DwHazelcastExampleApplication extends Application<DwHazelcastExampleConfiguration> {

    public static void main(String[] args) throws Exception {
        new DwHazelcastExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-hazelcast-example";
    }

    @Override
    public void initialize(Bootstrap<DwHazelcastExampleConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<DwHazelcastExampleConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(DwHazelcastExampleConfiguration config) {
                return config.getViewConfig();
            }
        });
    }

    @Override
    public void run(DwHazelcastExampleConfiguration dwHazelcastExampleConfiguration, Environment environment) throws Exception {
        environment.healthChecks().register("hazelcast-health", new HazelcastHealthcheck());
        environment.jersey().register(new DwHazelcastExampleViewsResource());
    }

}
