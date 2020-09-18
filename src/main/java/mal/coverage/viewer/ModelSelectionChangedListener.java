package mal.coverage.viewer;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import mal.coverage.viewer.model.MalAttackStep;
import mal.coverage.viewer.model.MalDefense;
import mal.coverage.viewer.model.MalModel;
import mal.coverage.viewer.model.MalSimulation;
import mal.coverage.viewer.model.coverage.CoverageData;
import mal.coverage.viewer.model.util.SimulationMerger;
import mal.coverage.viewer.view.Cell;
import mal.coverage.viewer.view.DataCell;

public class ModelSelectionChangedListener {
	public static final Color COLOR_COMPROMISED = Color.RED;
	public static final Color COLOR_COMPROMISED_EFFORT = Color.ORANGE;
	public static final Color COLOR_COMPROMISED_MUCH_EFFORT = Color.YELLOW;

	private Main _application;
	private TreeItem<String> _simTreeRoot;

	public ModelSelectionChangedListener(Main application) {
		_application = application;
	}

	public void registerTreeView(TreeView<String> view) {
		view.getSelectionModel().selectedItemProperty().addListener(selectionChangedListener);
		_simTreeRoot = view.getRoot();
	}

	/**
	 * Listener for handling treeview selected simulation changed events.
	 */
	private ChangeListener<TreeItem<String>> selectionChangedListener = new ChangeListener<>() {
		private String getModelName(TreeItem<String> item) {
			if (item == null)
				return "";

			while (!item.getParent().equals(_simTreeRoot)) {
				item = item.getParent();
			}

			return item.getValue();
		}

		/**
		 * Prepare the model display in the _application.graph view. Return true if successful, false
		 * otherwise.
		 */
		private boolean updateModel(String modelName, String oldMdlName) {
			if (oldMdlName.equals(modelName)) {
				// Same model
				for (Cell c : _application.graph.getCells()) {
					((DataCell) c).resetAllAttribColor();
				}

			} else {
				// New model
				if (_application.simulations.containsKey(modelName)) {
					MalModel mdl = _application.simulations.get(modelName);

					_application.graph.clear();
					_application.displayMALModel(mdl);

				} else {
					new Alert(Alert.AlertType.ERROR, String.format("Model with name '%s' does not exist.", modelName))
							.showAndWait();

					return false;
				}
			}

			return true;
		}

		private Map<Integer, Double> getCompromised(TreeItem<String> item) {
			Map<Integer, Double> simulationResults;
			String modelName = getModelName(item);
			MalModel mdl = _application.simulations.get(modelName);

			if (item.getParent().equals(_simTreeRoot)) {
				// Load model
				simulationResults = SimulationMerger.mergeModel(mdl);

			} else if (item.getParent().getParent().equals(_simTreeRoot)) {
				// Load SimGroup
				simulationResults = new HashMap<>();
				String selection = item.getValue();
				int sgHash = _application.mdlGrpNameMap.get(modelName).get(selection);

				//% TODO
				for (MalSimulation sim : mdl.simulationGroup.get(sgHash)) {
					simulationResults = SimulationMerger.mergeSimulation(simulationResults, sim);
				}
				
			} else if (item.getChildren().isEmpty()) {
				// Load simulation
				String key = String.format("%s.%s", item.getParent().getValue(), item.getValue());
				simulationResults = mdl.simulations.get(key).compromisedAttackSteps;

			} else {
				// Load test class
				simulationResults = SimulationMerger.mergeModelByClassname(mdl, item.getValue());
			}

			return simulationResults;
		}

		@Override
		public void changed(ObservableValue<? extends TreeItem<String>> obs, TreeItem<String> old,
				TreeItem<String> selected) {
			// Selection cleared
			if (selected == null)
				return;

			String oldMdlName = getModelName(old);
			String modelName = getModelName(selected);

			if (updateModel(modelName, oldMdlName)) {
				Map<Integer, Double> simRes = getCompromised(selected);
				MalModel mdl = _application.simulations.get(modelName);

				CoverageData data = new CoverageData(mdl, simRes, selected.getValue());
				_application.currentCoverage = data;

				simRes.forEach((id, ttc) -> {
					MalAttackStep step = mdl.attackSteps.get(id);
					int assetHash;
					String attribName;

					if (step == null) {
						MalDefense def = mdl.defenses.get(id);

						if (def == null) {
							System.err.println("WARNING: Found invalid compromised attack step reference. IGNORING");
							System.err.println(String.format("    ID: %d -> null", id));
							return;
						}
						attribName = String.format("# %s", def.name);
						assetHash = def.assetHash;

					} else {
						attribName = String.format("%s %s", step.type, step.name);
						assetHash = step.assetHash;

					}

					DataCell cell = (DataCell) _application.graph.getCell(assetHash);

					if (ttc < MalAttackStep.COMPROMISED_WITH_EFFORT_LOW) {
						cell.setAttribColor(attribName, COLOR_COMPROMISED);
					} else if (ttc < MalAttackStep.COMPROMISED_WITH_EFFORT_HIGH) {
						cell.setAttribColor(attribName, COLOR_COMPROMISED_EFFORT);
					} else {
						cell.setAttribColor(attribName, COLOR_COMPROMISED_MUCH_EFFORT);
					}
				});
			}

		}
	};
}
