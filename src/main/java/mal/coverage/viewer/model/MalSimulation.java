package mal.coverage.viewer.model;

import java.util.Map;

public class MalSimulation {
	public final String name;
	public final Map<Integer, Data> compromisedAttackSteps;
	public final Map<Integer, Integer> compromisedDefenses;

	public MalSimulation(String name, Map<Integer, Data> steps, Map<Integer, Integer> defenses) {
		this.name = name;
		compromisedAttackSteps = steps;
		compromisedDefenses = defenses;
	}

	public static class Data {
		public final int id;
		public final double ttc;

		public Data(int id, double ttc) {
			this.id = id;
			this.ttc = ttc;
		}
	}
}
