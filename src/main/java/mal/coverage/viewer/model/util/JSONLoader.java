package mal.coverage.viewer.model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.scene.control.Alert;
import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalAttackStep;
import mal.coverage.viewer.model.MalAbstractStep;
import mal.coverage.viewer.model.MalDefense;
import mal.coverage.viewer.model.MalModel;
import mal.coverage.viewer.model.MalSimulation;

public class JSONLoader implements ModelLoader {
	private Map<Integer, MalAttackStep> _steps;
	private Map<Integer, MalDefense> _defense;

	/**
	 * Constructs a MalDefense from a json object descrirption.
	 *
	 * @param json object describing a mal defense
	 * @return MalDefense constructed from the json description.
	 */
	public MalDefense parse_defense(JSONObject json, int assetHash) {
		String name = json.getString("name");
		int hash = json.getInt("hash");

		JSONArray jParents = json.getJSONArray("parents");
		Set<Integer> parents = new HashSet<>(jParents.length());

		for (int i = 0; i < jParents.length(); i++) {
			int id = jParents.getInt(i);
			parents.add(id);
		}

		MalDefense def = new MalDefense(name, assetHash, hash, parents);
		_defense.put(hash, def);

		return def;
	}

	/**
	 * Constructs a MalAttackStep from a json object description.
	 *
	 * @param json object describing a mal attack step.
	 * @return MalAttackStep constructed from the json description.
	 */
	public MalAttackStep parse_attack_step(JSONObject json, int assetHash) {
		String step = json.getString("step");
		String type = json.getString("type");
		int hash = json.getInt("hash");

		// Find parents
		JSONArray jParents = json.getJSONArray("parents");
		Set<Integer> parents = new HashSet<>(jParents.length());

		for (int i = 0; i < jParents.length(); i++) {
			parents.add(jParents.getInt(i));
		}

		// Store step
		MalAttackStep mStep = new MalAttackStep(step, type, assetHash, hash, parents);
		_steps.put(hash, mStep);

		return mStep;
	}

	/**
	 * Constructs a MalAsset from a json object description.
	 *
	 * @param json object describing a mal asset.
	 * @return MalAsset constructed from json the description.
	 */
	public MalAsset parse_asset(JSONObject json) {
		String name = json.getString("name");
		String className = json.getString("class");
		int hash = json.getInt("hash");

		// Find connected assets
		JSONArray jsonConnections = json.getJSONArray("connections");
		int[] connections = new int[jsonConnections.length()];

		for (int i = 0; i < jsonConnections.length(); i++) {
			connections[i] = jsonConnections.getInt(i);
		}

		// Find step connections
		JSONArray jsonStepCon = json.getJSONArray("stepConnections");
		Set<Integer> stepConnections = new HashSet<>(jsonStepCon.length());
		for (int i = 0; i < jsonStepCon.length(); i++) {
			stepConnections.add(jsonStepCon.getInt(i));
		}

		// Find attack steps
		JSONArray jsonAtkSteps = json.getJSONArray("steps");
		Map<Integer, MalAttackStep> atkSteps = new HashMap<>(jsonAtkSteps.length());

		for (int i = 0; i < jsonAtkSteps.length(); i++) {
			JSONObject jsonAtkStep = jsonAtkSteps.getJSONObject(i);
			MalAttackStep atkStep = parse_attack_step(jsonAtkStep, hash);

			atkSteps.put(atkStep.hash, atkStep);
		}

		// Find defenses
		JSONArray jsonDefenses = json.getJSONArray("defense");
		Map<String, MalDefense> defenses = new HashMap<>(jsonDefenses.length());

		for (int i = 0; i < jsonDefenses.length(); i++) {
			JSONObject jsonDefense = jsonDefenses.getJSONObject(i);
			MalDefense defense = parse_defense(jsonDefense, hash);

			defenses.put(defense.name, defense);
		}

		return new MalAsset(name, className, hash, connections, stepConnections, atkSteps, defenses);
	}

	/**
	 * Construcs a MalSimulation from a json representation.
	 *
	 * @param json representatino of a MalSimulation.
	 * @return a MalSimulationa.
	 */	
	private MalSimulation parse_simulation(JSONObject json) {
		String name = json.getString("test");
		String className = json.getString("class");
		JSONArray jinitComp = json.getJSONArray("initiallyCompromised");
		Set<Integer> initComp = new HashSet<>(jinitComp.length());
		for (int i = 0; i < jinitComp.length(); i++) {
			initComp.add(jinitComp.getInt(i));
		}

		// Parse compromised attack steps 
		JSONArray jSteps = json.getJSONArray("compromised");
		Map<Integer, Double> steps = new HashMap<>(jSteps.length());
	
		for (int i = 0; i < jSteps.length(); i++) {
			JSONObject jStep = jSteps.getJSONObject(i);
			int val = jStep.getInt("id");
			double ttc = jStep.getDouble("ttc");
			steps.put(val, ttc);
		}
	
		// Parse active defenses
		JSONArray jDef = json.getJSONArray("activeDefenses");
		Set<Integer> defenses = new HashSet<>(jDef.length());
	
		for (int i = 0; i < jDef.length(); i++) {
			Integer val = jDef.getInt(i);
			defenses.add(val);
		}
	
		return new MalSimulation(name, className, steps, defenses, initComp);
	}
	
	/**
	 * Constructs a MAL model from a JSON representation.
	 *
	 * @param obj root json object node.
	 * @return a MAL model.
	 */
	private MalModel parse_model(JSONObject json) {		// Parse mal threat model
		_steps = new HashMap<>();
		_defense = new HashMap<>();

		JSONArray jsonAssets = json.getJSONArray("model");
		Map<Integer, MalAsset> assets = new HashMap<>(jsonAssets.length());

		// Load assets
		for (int i = 0; i < jsonAssets.length(); i++) {
			JSONObject jAsset = jsonAssets.getJSONObject(i);
			MalAsset asset = parse_asset(jAsset);

			assets.put(asset.hash, asset);
		}

		// Parse simulations 
		JSONArray jSims = json.getJSONArray("simulations");
		Map<String, MalSimulation> sims = new HashMap<>(jSims.length());

		for (int i = 0; i < jSims.length(); i++) {
			JSONObject jSim = jSims.getJSONObject(i);
			MalSimulation sim = parse_simulation(jSim);

			sims.put(String.format("%s.%s", sim.className, sim.name), sim);
		}

		return new MalModel(assets, _steps, _defense, sims);
	}

	/**
	 * Constructs a MalModel list from a json array.
	 *
	 * @param json json mal model array.	
	 * @return list of mal models and simulations
	 */
	private List<MalModel> parse_json(JSONArray json) {
		List<MalModel> res = new ArrayList<>(json.length());

		for (int i = 0; i < json.length(); i++) {
			MalModel mdl = parse_model(json.getJSONObject(i));

			res.add(mdl);
		}

		return res;
	}

	/**
	 * Parses a json file containing mal threat model simulation data.
	 *
	 * @param file containing the simulation data.
	 * @return list of mal models and simulation results.	
	 */
	@Override
	public List<MalModel> parse(File file) {
		List<MalModel> res = new ArrayList<>();

		try {
			JSONArray json = new JSONArray(new JSONTokener(new BufferedReader(new FileReader(file))));

			res =  parse_json(json);

		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR, String.format("Failed to parse file '%s'.", file.getName()));
		}
		return res;
	}
}
