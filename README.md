Dropwizard Hazelcast Session
============================

A Dropwizard bundle for providing clustered session replication using Hazelcast distributed maps.

**Note** This jar has not yet been published to a public repository. TODO.


Getting Started
---------------

See the `example-app` module for a working example (more details below), but it comes down to this:

Add a `HazelcastSessionConfig` property to your application configuration:
```java
public class MyAppConfiguration extends Configuration {
    @JsonProperty("hazelcastSessionConfig")
    private HazelcastSessionConfig hazelcastSessionConfig;

    public HazelcastSessionConfig getHazelcastSessionConfig() {
        return hazelcastSessionConfig;
    }
}
```

Most of the configs are optional, but you might want to set these in your YML config file:
```yml
hazelcastSessionConfig:
  sessionTimeoutSecs: 900
  cookieName: myapp-session
```

Add a `HazelcastSessionBundle` to your bootstrap during initialisation:
```java
public class DwHazelcastExampleApplication extends Application<DwHazelcastExampleConfiguration> {
    // ...
    @Override
    public void initialize(Bootstrap<DwHazelcastExampleConfiguration> bootstrap) {
        bootstrap.addBundle(new HazelcastSessionBundle<DwHazelcastExampleConfiguration>() {
            @Override
            public HazelcastSessionConfig getHazelcastSessionConfig(DwHazelcastExampleConfiguration configuration) {
                return configuration.getHazelcastSessionConfig();
            }
        });
    }
    // ...
}

Add a `Session` parameter to resource method and annotate it with `@Context` to have the session injected:
```java
package my.package;
import technology.zook.dropwizard.hazelcast.session.Session;
// ...
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class MyResource {
    @GET
    public String getSomething(@Context Session session) {
        return (String) session.get("thing");
    }
}
```
```


See it in action
----------------

The `example-app` module contains a working example of `dropwizard-hazelcast-session`. It's a pretty terrible app, but what are ya gonna do...

Assuming you have a UNIX terminal with git and maven installed:
  1. `git clone https://github.com/martindow/dropwizard-hazelcast-session.git`
  2. `cd dropwizard-hazelcast-session`
  3. `mvn install`
  4. `cd example-app`
  5. `mvn package`
  6. `java -jar server config/dw-hazelcast-example-dev-8180.yml`

Now visit [http://localhost:8180/](http://localhost:8180/) and you should see "Hello, Somebody!". Wonderful. Now the "UI" is a bit basic, shall we say, because to you get your actual name into the session with another GET request and a query srting param: [http://localhost:8180/?name=Martin](http://localhost:8180/?name=Martin). Now go back to [http://localhost:8180/](http://localhost:8180/) and you'll be greeted with your actual name loaded from the actual session. Glorious.

To really appreciate the magic, however, you need to start a second instance of the server on another port. When you do that Hazelcast will use spot the second instance over multicast and will casually create you a cluster of "Hello, Somebody!" services.

So open a second terminal window in the same directory:
  1. `cd dropwizard-hazelcast-session/example-app`
  2. `java -jar server config/dw-hazelcast-example-dev-8280.yml`

Now visit that second web app in a browser: [http://localhost:8280/](http://localhost:8280/) (note the different port). You should be greated with the name stored in the first web app instance, which Hazelcast has synched over seamlessly. Mess about with changing the name on one server and watching the session be replicated to the other server until you get bored.

Next up, open `example-app/config/dw-hazelcast-example-dev-8180.yml` and `example-app/config/dw-hazelcast-example-dev-8280.yml` and change the value of `  sessionTimeoutSecs` to `10` (seconds). Restart both services. Now you'll see the session expire after 10 seconds.


Why, oh why?
------------

Obviously a RESTful service shouldn't rely on session state to do it's work, so why would you want need this? Well [Dropwizard Views](http://www.dropwizard.io/manual/views.html) offers a couple of great modules for building web applications using Dropwizard and those often do need some session state to be useful.

`dropwizard-hazelcast-session` lets you quickly and easily add session state which is instantly replicated across a cluster of services.
