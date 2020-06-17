package mal.coverage.viewer.model;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MalAsset {
    public final String name;
    public final long id;
    public final MalAssetDescription classDesc;
    public final long[] connections;

    public MalAsset(MalAssetDescription desc, String name, long id, long[] connections) {
        classDesc = desc;
        this.name = name;
        this.id = id;
        this.connections = connections;
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

        long[] connections = new long[jsonConnections.length()];

        for (int i = 0; i < jsonConnections.length(); i++) {
            connections[i] = jsonConnections.getLong(i);
        }

        return new MalAsset(descs.get(className), name, id, connections);
    }
    
}