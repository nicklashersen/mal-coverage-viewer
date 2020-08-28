package mal.coverage.viewer.model;

import java.util.Set;

public class MalAttackStep extends MalAbstractStep {
	public final String type;

	/*
	 * Same valus as used in assertCompromisedWithEffort (MAL:AttackStep.java)
	 */
	public static final double COMPROMISED_WITH_EFFORT_LOW = 1.0 / 1444;
	public static final double COMPROMISED_WITH_EFFORT_HIGH = 1000.0;

	public MalAttackStep(String name, String type, int assetHash, int hash, Set<Integer> parents) {

		super(name, assetHash, hash, parents);
		this.type = type;
	}
}
