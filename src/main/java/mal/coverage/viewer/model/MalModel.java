package mal.coverage.viewer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MalModel {
	// MalAsset id -> asset
	public final Map<Integer, MalAsset> assets;
	public final Map<Integer, MalAttackStep> attackSteps;
	public final Map<Integer, MalDefense> defenses;
	public final Map<String, MalSimulation> simulations;
	public final Map<Integer, List<MalSimulation>> simulationGroup = new HashMap<>();

    public MalModel(Map<Integer, MalAsset> assets,
		    Map<Integer, MalAttackStep> attackSteps,
		    Map<Integer, MalDefense> defenses,
			Map<String, MalSimulation> simulations) {

		this.assets = assets;
		this.attackSteps = attackSteps;
		this.defenses = defenses;
		this.simulations = simulations;

		for (MalSimulation sim : simulations.values()) {
			int groupKey = sim.initiallyCompromised.hashCode();

			List<MalSimulation> simGroup
				= simulationGroup.computeIfAbsent(groupKey, k -> new ArrayList<>());

			simGroup.add(sim);
		}
	}
}
