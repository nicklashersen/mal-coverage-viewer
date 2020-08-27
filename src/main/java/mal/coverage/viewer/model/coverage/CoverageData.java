package mal.coverage.viewer.model.coverage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalAttackStep;
import mal.coverage.viewer.model.MalDefense;
import mal.coverage.viewer.model.MalModel;

/**
 * CoverageData
 */
public class CoverageData implements Iterable {
	private Map<String, Entry> coverageResults = new HashMap<>();

	public CoverageData() {
	}

	public CoverageData(MalModel mdl, Map<Integer, Double> compromised) {
		computeCoverage(mdl, compromised);
	}

	/**
	 * Add a new coverage mapping name -> result.
	 *
	 * @param name        coverage model name
	 * @param nMax        maximum number of compromised states
	 * @param compromised states
	 */
	public void add(String name, int nMax, int nCompromised) {
		coverageResults.put(name, new Entry(name, nMax, nCompromised));
	}

	/**
	 * Returns coverage data associated with the specified name. If no such mapping
	 * has been registered an empty Optional is returned.
	 *
	 * @return Optional containing the result.
	 */
	public Optional<Entry> get(String name) {
		return Optional.ofNullable(coverageResults.get(name));
	}

	/**
	 * Returns an iterator over all registered coverage data.
	 *
	 * @return An iterator over all registered data
	 */
	@Override
	public Iterator<Entry> iterator() {
		return coverageResults.values().iterator();
	}

	/**
	 * Computes for the specified MalModel given a map of compromised attack steps.
	 *
	 * @param mdl         MAL threat model
	 * @param compromised map of compromised AttackSteps
	 */
	private void computeCoverage(MalModel mdl, Map<Integer, Double> compromisedSteps) {
		int nFullyCompromisedAssets = 0;
		int nPartiallyCompromisedAssets = 0;
		int nCompromisedEdges = 0;
		int nEdges = 0;

		for (MalAsset asset : mdl.assets.values()) {
			boolean fullyCompromised = true;
			boolean partCompromised = false;

			// Compute compromised attack steps
			for (MalAttackStep step : asset.steps.values()) {
				boolean compromised = compromisedSteps.containsKey(step.hash);

				fullyCompromised = fullyCompromised && compromised;
				partCompromised = partCompromised || compromised;

				// Compute compromised edges
				for (int parentId : step.parents) {
					if (compromised && compromisedSteps.containsKey(parentId)) {
						nCompromisedEdges++;
					}
				}

				// Compute total edges
				nEdges += step.parents.size();
			}

			// Do the same thing for all registered defenses
			for (MalDefense def : asset.defense.values()) {
				boolean compromised = compromisedSteps.containsKey(def.hash);

				fullyCompromised = fullyCompromised && compromised;
				partCompromised = partCompromised || compromised;

				for (int parentId : def.parents) {
					if (compromised && compromisedSteps.containsKey(parentId)) {
						nCompromisedEdges++;
					}
				}

				nEdges += def.parents.size();
			}

			if (partCompromised) {
				nPartiallyCompromisedAssets++;

				if (fullyCompromised) {
					nFullyCompromisedAssets++;
				}
			}

		}

		add("Full Asset", mdl.assets.size(), nFullyCompromisedAssets);
		add("Partial Asset", mdl.assets.size(), nPartiallyCompromisedAssets);
		add("AttackSteps", mdl.attackSteps.size() + mdl.defenses.size(), compromisedSteps.size());
		add("Edge", nEdges, nCompromisedEdges);
	}

	public class Entry {
		public String name;
		public int nMax;
		public int nCompromised;

		public Entry(String name, int nMax, int nCompromised) {
			this.name = name;
			this.nMax = nMax;
			this.nCompromised = nCompromised;
		}
	}
}
