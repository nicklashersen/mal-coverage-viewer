package mal.coverage.viewer.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class MalModel {
	// MalAsset id -> asset
	public final Map<Integer, MalAsset> assets;
	public final Map<Integer, MalAttackStep> attackSteps;
	public final Map<Integer, MalDefense> defense;
	public final Map<String, MalSimulation> simulations;

    public MalModel(Map<Integer, MalAsset> assets,
		    Map<Integer, MalAttackStep> attackSteps,
		    Map<Integer, MalDefense> defense,
		    Map<String, MalSimulation> simulations) {

		this.assets = assets;
		this.attackSteps = attackSteps;
		this.defense = defense;
		this.simulations = simulations;
	}
}
