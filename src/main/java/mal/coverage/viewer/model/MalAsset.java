package mal.coverage.viewer.model;

import java.util.Map;

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
}
