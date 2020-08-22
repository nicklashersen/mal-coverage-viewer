package mal.coverage.viewer.model;

import org.json.JSONObject;

public class MalDefense {
    public final String name;
    public final int hash;

    public MalDefense(String name, int hash) {
	this.name = name;
	this.hash = hash;
    }
}
