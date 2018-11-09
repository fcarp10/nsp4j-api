package manager.elements;

import java.util.HashMap;
import java.util.Map;

public class Function {

    private int type;
    private Map<String, Object> attributes;

    public Function() {
        attributes = new HashMap<>();
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public int getType() {
        return type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
