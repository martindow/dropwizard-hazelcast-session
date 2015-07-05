package technology.zook.dropwizard.hazelcast.example.views;


import io.dropwizard.views.View;

public class IndexPageView extends View {

    public IndexPageView() {
        super("/views/index.ftl");
    }

    public String getName() {
        return "Martin";
    }

}
