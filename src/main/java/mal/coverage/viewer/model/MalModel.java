package mal.coverage.viewer.model;

import java.util.Map;

public class MalModel {
	// MalAsset id -> asset
	public final Map<Integer, MalAsset> assets;
	public final Map<Integer, MalAttackStep> attackSteps;
	public final Map<Integer, MalDefense> defenses;
	public final Map<String, MalSimulation> simulations;

    public MalModel(Map<Integer, MalAsset> assets,
		    Map<Integer, MalAttackStep> attackSteps,
		    Map<Integer, MalDefense> defenses,
		    Map<String, MalSimulation> simulations) {

		this.assets = assets;
		this.attackSteps = attackSteps;
		this.defenses = defenses;
		this.simulations = simulations;
	}
}
