package mal.coverage.viewer.model.coverage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mal.coverage.viewer.model.MalAbstractStep;
import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalModel;

/**
 * Window for displaying coverage data.
 */
public class CoverageData implements Iterable<CoverageData.Entry> {
	private Map<String, Entry> coverageResults = new HashMap<>();
	public String selectionName;

	public CoverageData() {
	}

	public CoverageData(MalModel mdl, Map<Integer, Double> compromised, String selectionName) {
		computeCoverage(mdl, compromised);
		this.selectionName = selectionName;
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

			List<MalAbstractStep> steps =
				Stream.concat(asset.steps.values().stream(),
							  asset.defense.values().stream())
					.collect(Collectors.toList());

			// Compute compromised attack steps
			for (MalAbstractStep step : steps) {
				boolean compromised = compromisedSteps.containsKey(step.hash);

				fullyCompromised = fullyCompromised && compromised;
				partCompromised = partCompromised || compromised;

				// Compute compromised edges
				for (int parentId : step.parents) {
					if (compromisedSteps.containsKey(parentId)) {
						nCompromisedEdges++;
					}
				}

				// Compute total edges
				nEdges += step.parents.size();
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
