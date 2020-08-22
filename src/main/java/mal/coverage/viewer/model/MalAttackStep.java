package mal.coverage.viewer.model;

import java.util.Set;

public class MalAttackStep {
    public final String name;
    public final String type;
    public final int hash;

    public final Set<Integer> parents;

    public MalAttackStep(String name,
			 String type,
			 int hash,
			 Set<Integer> parents) {

	this.name = name;
	this.type = type;
	this.hash = hash;
	this.parents = parents;
    }
}
