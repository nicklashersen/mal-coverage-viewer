package mal.coverage.viewer.model;

import java.util.Set;

public class MalAttackStep {
    public final String name;
    public final String type;
    public final int hash;

    public final Set<Integer> parents;

	/*
	 * Same valus as used in assertCompromisedWithEffort (MAL:AttackStep.java)
	 */
	public static final double COMPROMISED_WITH_EFFORT_LOW = 1.0 / 1444;
	public static final double COMPROMISED_WITH_EFFORT_HIGH = 1000.0;

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
