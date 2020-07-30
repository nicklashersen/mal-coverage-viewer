package mal.coverage.viewer.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MalAsset {
    public final String name;
    public final long id;
    public final MalAssetDescription classDesc;
    public final long[] connections;
    public final Map<String, Double> coverage;

    public MalAsset(MalAssetDescription desc, String name, long id, long[] connections, Map<String, Double> coverage) {
        classDesc = desc;
        this.name = name;
        this.id = id;
        this.connections = connections;
        this.coverage = coverage;
    }

    /**
     * @param object json object descibing a MAL object instance
     * @param descs MAL class descriptions
     * @return
     */
    public static MalAsset fromJSON(JSONObject object, Map<String, MalAssetDescription> descs) {
        String name, className;
        long id;

        className = object.getString("class");
        if (!descs.containsKey(className)) {
            throw new RuntimeException("Unrecognized MAL asset class");
        }

        name = object.getString("name");
        id = object.getLong("hash");
        JSONArray jsonConnections = object.getJSONArray("connections");

        // Find connected assets
        long[] connections = new long[jsonConnections.length()];

        for (int i = 0; i < jsonConnections.length(); i++) {
            connections[i] = jsonConnections.getLong(i);
        }

        // Fetch compromised fields
        JSONArray jsonCompromised = object.getJSONArray("coverage");
        Map<String, Double> coverage = new HashMap<>(jsonCompromised.length());

        for (int i = 0; i < jsonCompromised.length(); i++) {
            JSONObject jsonCompObject = jsonCompromised.getJSONObject(i);
            coverage.put(jsonCompObject.getString("step"), jsonCompObject.getDouble("ttc"));
        }

        return new MalAsset(descs.get(className), name, id, connections, coverage);
    }
    
}
