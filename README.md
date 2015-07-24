Dropwizard Hazelcast Session
============================

A Dropwizard bundle for providing clustered session replication using Hazelcast distributed maps.

Maven
-----

Maven coordinates for the [latest release](https://repo1.maven.org/maven2/com/unicodecollective/dropwizard/dropwizard-hazelcast-session/1.0.1/) are:

```xml
<dependency>
    <groupId>com.unicodecollective.dropwizard</groupId>
    <artifactId>dropwizard-hazelcast-session</artifactId>
    <version>1.0.2</version>
</dependency>
```


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
```

Annotate the resource field with `@Session` to have the value injected from the Hazelcast-managed session:
```java
package my.package;
import technology.zook.dropwizard.hazelcast.session.Session;
// ...
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class MyResource {

    @Session
    private String thingy;
    
    @GET
    public String getSomething() {
        return thingy;
    }
}
```

**Note**

You session objects should be injected as fields into your Resource class using the `@Session` annotation. There's currently a problem injecting them into request handler parameters.


**Note**

There appears to be a problem with declaring your Jersey resource in Dropwizard as an instance like this:
```java
// Don't do this:
environment.jersey().register(new MyResourceClass());
```

While we figure out the problem you should declare your resource like this:
```java
environment.jersey().register(MyResourceClass.class);
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

Now visit [http://localhost:8180/](http://localhost:8180/) and you should see "Hello, Somebody!". Wonderful. Now submit a name using the form and you should be greeted appropriately. Reload the page a few times and you'll see the name sticks, as it's being read from the session.

To really appreciate the magic, however, you need to start a second instance of the server on another port (imagine these two nodes sitting behind a load balancer). When you do that Hazelcast will hook up with the second instance over multicast and will casually create you a cluster of "Hello, Somebody!" services.

So open a second terminal window in the same directory:
  1. `cd dropwizard-hazelcast-session/example-app`
  2. `java -jar server config/dw-hazelcast-example-dev-8280.yml`

Now visit that second web app in a browser: [http://localhost:8280/](http://localhost:8280/) (note the different port). You should be greeted with the name stored in the first web app instance, which Hazelcast has synched over seamlessly. Change the name on one server and watch it replicate to the other server until you get bored.

Next up, open `example-app/config/dw-hazelcast-example-dev-8180.yml` and `example-app/config/dw-hazelcast-example-dev-8280.yml` and change the value of `  sessionTimeoutSecs` to `10` (seconds). Restart both services. Now you'll see the session expire after 10 seconds.

You can keep adding and removing nodes and Hazelcast will manage the cluster for you. Data will be transparently distributed across the nodes to ensure it will remain available if any node suddenly disappears.



Why, oh why?
------------

Obviously a RESTful service shouldn't rely on session state to do it's work, so why would you want need this? Well [Dropwizard Views](http://www.dropwizard.io/manual/views.html) offers a couple of great modules for building web applications and those often do need some session state to be useful.

`dropwizard-hazelcast-session` lets you quickly and easily add session state which is instantly replicated across a cluster of services.



Releasing
---------

This project is released to the Sonatype OSS repository use Maven, the details of which are documented here:

[http://central.sonatype.org/pages/apache-maven.html](http://central.sonatype.org/pages/apache-maven.html)

- To release a snapshot version: `mvn deploy`
- To release production version:
  - Set the release version: `mvn versions:set -DnewVersion=1.0.2`
  - Commit version change: `git commit -m "Setting the maven project version to xxx."`
  - Deploy the release version: `mvn clean deploy -P release`
  - Set the next snapshot version: `mvn versions:set -DnewVersion=1.0.2-SNAPSHOT`
  - Commit the version change: `git commit -m "Setting the maven project version to xxx."`