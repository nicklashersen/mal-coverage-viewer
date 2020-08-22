package mal.coverage.viewer.model.util;

import java.util.HashMap;
import java.util.Map;

import mal.coverage.viewer.model.MalModel;
import mal.coverage.viewer.model.MalSimulation;

public class SimulationMerger {
	/**
	 * Returns a map containing all compromised nodes from both simulations.
	 * If a node is present in both simulations the entry with lowest ttc (value)
	 * is kept.	
	 *
	 * @param s1 simulation 1
	 * @param s2 simulation 2	
	 * @return s1 union s2 such that the ttc for each node is minimized.
	 */
	public static Map<Integer, Double> mergeSimulations(MalSimulation s1, MalSimulation s2) {
		return mergeSimulation(new HashMap<Integer, Double>(s1.compromisedAttackSteps), s2);
	}

	/**
	 * Merges the simulation s with the map res. If a node is present in both 
	 * the entry with the lowest ttc (value) is kept.
	 *
	 * @param res simulation (id, ttc) map
	 * @param s simulation	
	 * @return res
	 */
	public static Map<Integer, Double> mergeSimulation(Map<Integer, Double> res, MalSimulation s) {
		// For every entry 
		s.compromisedAttackSteps.forEach((id, ttc) -> {
			// Keep the one with the lowest ttc
			res.merge(id, ttc, (o, n) -> o.compareTo(n) <= 0 ? o : n);
		});

		return res;
	}

	/**
	 * Returns a map containing all compromised nodes for a mal model belonging
	 * to the specified test class. If a node occurs in multiple simulations the
	 * entry with the lowers ttc (value) will be kept.	
	 *
	 * @param mdl model with associated simulations.
	 * @param classname test class name to merge simulations from.
	 * @return all compromised entries from simulations associated with the model and 
	 *         test class name.	
	 */
	public static Map<Integer, Double> mergeModelByClassname(MalModel mdl, String classname) {
		Map<Integer, Double> res = new HashMap<>(mdl.attackSteps.size());

		for (MalSimulation sim : mdl.simulations.values()) {
			if (sim.className.equals(classname)) {
				mergeSimulation(res, sim);
			}
		}

		return res;
	 }

	/**
	 * Merges the results of all simulations for the specified model.
	 * If an entry occurs multiple times the entry with the lowest
	 * ttc (value) will be kept.
	 *
	 * @param mdl model to merge simulation from.
	 * @return a map containing compromised entries from all simulations.
	 */
	public static Map<Integer, Double> mergeModel(MalModel mdl) {
		Map<Integer, Double> res = new HashMap<>(mdl.attackSteps.size());

		for (MalSimulation sim : mdl.simulations.values()) {
			mergeSimulation(res, sim);
		}

		return res;
	}
}
