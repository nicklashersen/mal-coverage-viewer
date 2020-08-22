package mal.coverage.viewer.model;

import java.util.Map;
import java.util.Set;

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
}
