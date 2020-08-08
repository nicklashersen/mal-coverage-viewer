package mal.coverage.viewer.model;

import org.json.JSONObject;

public class MalDefense {
    public final String name;
    public final boolean active;

    public MalDefense(String name, boolean active) {
	this.name = name;
	this.active = active;
    }

    public static MalDefense fromJSON(JSONObject obj) {
	String name = obj.getString("name");
	boolean active = obj.getBoolean("active");

	return new MalDefense(name, active);
    }
}
