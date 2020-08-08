package mal.coverage.viewer.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MalAsset {
    public final String name;
    public final String assetName;
    public final int hash;
    public final int[] connections;
    public final Map<Integer, MalAttackStep> steps;
    public final Map<String, MalDefense> defense;

    public MalAsset(String name,
		    String assetName,
		    int hash,
		    int[] connections,
		    Map<Integer, MalAttackStep> steps,
		    Map<String, MalDefense> defense) {

	    this.name = name;
	    this.assetName = assetName;
	    this.hash = hash;
	    this.connections = connections;
	    this.steps = steps;
	    this.defense = defense;
	}

    /**
     * @param object json object descibing a MAL object instance
     * @param descs MAL class descriptions
     * @return
     */
    public static MalAsset fromJSON(JSONObject object) {
	String name = object.getString("name");
	String className = object.getString("class");
	int hash = object.getInt("hash");

	// Find connected assets
	JSONArray jsonConnections = object.getJSONArray("connections");
	int[] connections = new int[jsonConnections.length()];

	for (int i = 0; i < jsonConnections.length(); i++) {
	    connections[i] = jsonConnections.getInt(i);
	}

	// Find attack steps
	JSONArray jsonAttackSteps = object.getJSONArray("steps");
	Map<Integer, MalAttackStep> attackSteps = new HashMap<>(jsonAttackSteps.length());

	for (int i = 0; i < jsonAttackSteps.length(); i++) {
	    JSONObject jsonAttackStep = jsonAttackSteps.getJSONObject(i);
	    MalAttackStep attackStep = MalAttackStep.fromJSON(jsonAttackStep);

	    attackSteps.put(attackStep.hash, attackStep);
	}

	// Find defenses
	JSONArray jsonDefenses = object.getJSONArray("defense");
	Map<String, MalDefense> defenses = new HashMap<>(jsonDefenses.length());

	for (int i = 0; i < jsonDefenses.length(); i++) {
	    JSONObject jsonDefense = jsonDefenses.getJSONObject(i);
	    MalDefense defense = MalDefense.fromJSON(jsonDefense);

	    defenses.put(defense.name, defense);
	}

	return new MalAsset(name, className, hash, connections, attackSteps, defenses);
    }
    
}
