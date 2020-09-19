package mal.coverage.viewer.model.coverage;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mal.coverage.viewer.model.MalAbstractStep;
import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalModel;
import mal.coverage.viewer.model.MalSimulation;
import mal.coverage.viewer.model.util.SimulationMerger;

public class StandardCoverage implements CoverageBuilder {
	/**
	 * Computes for the specified MalModel given a map of compromised attack steps.
	 *
	 * @param mdl         MAL threat model
	 * @param compromised map of compromised AttackSteps
	 */
	public CoverageData computeCoverage(MalModel mdl, Map<Integer, Double> compromisedSteps) {
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
			}

			if (fullyCompromised) {
				nFullyCompromisedAssets++;
			}

		}

		CoverageData cData = new CoverageData();
		cData.add("Full Asset", mdl.assets.size(), nFullyCompromisedAssets);
		cData.add("Partial Asset", mdl.assets.size(), nPartiallyCompromisedAssets);
		cData.add("AttackSteps", mdl.attackSteps.size(), compromisedSteps.size());
		cData.add("Edge", nEdges, nCompromisedEdges);

		return cData;
	}

	@Override
	public CoverageData computeClassCoverage(MalModel mdl, List<MalSimulation> sims) {
		Map<Integer, Double> compromised = new HashMap<>();
		Set<Integer> states = new HashSet<>();
		Set<Integer> simGroups = new HashSet<>();

		for (MalSimulation sim : sims) {
			states.add(sim.activeDefenses.hashCode());
			simGroups.add(sim.initiallyCompromised.hashCode());
			SimulationMerger.mergeSimulation(compromised, sim);
		}

		CoverageData cData = computeCoverage(mdl, compromised);
		BigInteger defStates = BigInteger.valueOf(2)
			.pow(mdl.defenses.size())
			.multiply(BigInteger.valueOf(simGroups.size()));

		cData.add("Defense", defStates, BigInteger.valueOf(states.size()));
		cData.compromised = compromised;

		return cData;
	}

	@Override
	public CoverageData computeModelCoverage(MalModel mdl) {
		Map<Integer, Double> compromised = SimulationMerger.mergeModel(mdl);

		// Defense state coverage
		int coveredStates = mdl.simulationGroup.values().stream()
			.map(s -> s.size())
			.reduce(0, Integer::sum);

		BigInteger states = BigInteger.valueOf(2)
			.pow(mdl.defenses.size())
			.multiply(BigInteger.valueOf(mdl.simulationGroup.size()));

		CoverageData cData = computeCoverage(mdl, compromised);
		cData.add("Defense",
				states,
				BigInteger.valueOf(coveredStates));

		cData.compromised = compromised;

		return cData;
	}

	@Override
	public CoverageData computeSimGroupCoverage(MalModel mdl, List<MalSimulation> group) {
		Map<Integer, Double> compromised = new HashMap<>();
		int groupHash = 0;
		int mismatches = 0;

		if (!group.isEmpty()) {
			groupHash = group.get(0).initiallyCompromised.hashCode();
		}

		for (MalSimulation sim : group) {
			int hash = sim.initiallyCompromised.hashCode();

			if (groupHash != hash) {
				System.err.println("Warning: StandardCoverage::computeSimsGroupCoverage");
				System.err.println("  mismatched simulation group hash; ignoring\n");
				mismatches++;
				continue;
			}
			
			SimulationMerger.mergeSimulation(compromised, sim);
		}

		CoverageData cData = computeCoverage(mdl, compromised);
		cData.add("Defense",
				  BigInteger.valueOf(2).pow(mdl.defenses.size()),
				  BigInteger.valueOf(group.size() - mismatches));
		cData.compromised = compromised;

		return cData;
	}

	@Override
	public CoverageData computeSimulationCoverage(MalModel mdl, MalSimulation sim) {
		Map<Integer, Double> compromised = new HashMap<>(sim.compromisedAttackSteps);
		CoverageData cData = computeCoverage(mdl, sim.compromisedAttackSteps);
		cData.compromised = compromised;

		return cData;
	}
}
