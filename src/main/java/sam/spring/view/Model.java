package sam.spring.view;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<String, Object> attributes;

    public Model() {
        attributes = new HashMap<>();
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
}
