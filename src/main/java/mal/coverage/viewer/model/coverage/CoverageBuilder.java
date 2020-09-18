package mal.coverage.viewer.model.coverage;

import java.util.List;

import mal.coverage.viewer.model.MalModel;
import mal.coverage.viewer.model.MalSimulation;

public interface CoverageBuilder {
	/**
	 * Computes coverage for a MalModel.
	 *
	 * @param mdl MAL threat model	
	 * @return Coverage data for simulation run on the model.
	 */
	public CoverageData computeModelCoverage(MalModel mdl);

	/**
	 * Computes coverage for a single simulation group. 
	 *
	 * @param mdl MAL threat model
	 * @param group simulation group
	 * @return Coverage data for the simulation group.
	 */
	public CoverageData computeSimGroupCoverage(MalModel mdl, List<MalSimulation> group);

	/**
	 * Computes coverage for a single test class for a specific model.
	 *
	 * @param mdl MAL threat model
	 * @param sims simulations 
	 * @reutrn Coverage data for the test class	
	 */
	public CoverageData computeClassCoverage(MalModel mdl, List<MalSimulation> sims);

	/**
	 * Computes coverage for a single simulation.
	 *
	 * @param mdl MAL threat model
	 * @param sim MAL simulation
	 * @return Coverage data for the simulation	
	 */
	public CoverageData computeSimulationCoverage(MalModel mdl, MalSimulation sim);
}
