package mal.coverage.viewer.model;

import java.util.Map;
import java.util.Set;

import mal.coverage.viewer.model.coverage.CoverageData;

public class MalSimulation {
	public final String name;
	public final String className;
	public final Map<Integer, Double> compromisedAttackSteps; // (id, ttc)
	public final Set<Integer> activeDefenses; // id

	public MalSimulation(String name, String className, Map<Integer, Double> steps, Set<Integer> defenses) {
		this.name = name;
		this.className = className;
		compromisedAttackSteps = steps;
		activeDefenses = defenses;
	}

	public boolean compromised(MalAttackStep step) {
		return compromised(step.hash);
	}

	public boolean compromised(int hash) {
		return compromisedAttackSteps.containsKey(hash);
	}
}
