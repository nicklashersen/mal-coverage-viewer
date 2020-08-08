package mal.coverage.viewer.model;

import java.util.Set;
import java.util.HashSet;

import org.json.JSONObject;
import org.json.JSONArray;

public class MalAttackStep {
    public final String name;
    public final double ttc;
    public final String type;
    public final int hash;

    public final Set<Integer> compromisedParents;
    public final Set<Integer> uncompromisedParents;

    public MalAttackStep(String name,
			 double ttc,
			 String type,
			 int hash,
			 Set<Integer> compromisedParents,
			 Set<Integer> uncompromisedParents) {

	this.name = name;
	this.type = type;
	this.ttc  = ttc;
	this.hash = hash;
	this.compromisedParents = compromisedParents;
	this.uncompromisedParents = uncompromisedParents;
    }

    public static MalAttackStep fromJSON(JSONObject obj) {
	String step = obj.getString("step");
	String type = obj.getString("type");
	double ttc = obj.getDouble("ttc");
	int hash = obj.getInt("hash");

	// Find visited parents
	JSONArray jsonVisited = obj.getJSONArray("compromisedParents");
	Set<Integer> compromisedParents = new HashSet<>(jsonVisited.length());
	for (int i = 0; i < jsonVisited.length(); i++) {
	    compromisedParents.add(jsonVisited.getInt(i));
	}

	// Find unvisited parents
	JSONArray jsonUncompromised = obj.getJSONArray("uncompromisedParents");
	Set<Integer> uncompromisedParents = new HashSet<>(jsonUncompromised.length());
	for (int i = 0; i < jsonUncompromised.length(); i++) {
	    uncompromisedParents.add(jsonUncompromised.getInt(i));
	}

	return new MalAttackStep(step, ttc, type, hash, compromisedParents, uncompromisedParents);
    }
}
