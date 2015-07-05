package technology.zook.dropwizard.hazelcast.example.health;


import com.codahale.metrics.health.HealthCheck;

public class HazelcastHealthcheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        // TODO: Check Hazelcast health.
        return Result.healthy();
    }

}
