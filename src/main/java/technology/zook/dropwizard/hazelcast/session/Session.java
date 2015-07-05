package technology.zook.dropwizard.hazelcast.session;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Session implements Serializable {

    private static final long serialVersionUID = 111L;

    private final Map<Object, Object> map = new HashMap<>();

    public void put(Object key, Object value) {
        map.put(key, value);
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

}
