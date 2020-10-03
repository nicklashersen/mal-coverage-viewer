package mal.coverage.viewer.util;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalAttackStep;
import mal.coverage.viewer.model.MalDefense;
import mal.coverage.viewer.model.MalAbstractStep;
import mal.coverage.viewer.view.DataCell;

public class AssetDataCellConstructor {
	public Map<Label, MalAbstractStep> labelToStep = new HashMap<>();
	public Map<Integer, Label> stepIdToLabel = new HashMap<>();

	public DataCell cellFromAsset(MalAsset asset) {
		DataCell cell = new DataCell(asset.name, asset.assetName);
		processAttackSteps(asset, cell);
		processDefenses(asset, cell);

		return cell;
	}

	private void processDefenses(MalAsset asset, DataCell cell) {
		for (MalDefense def : asset.defense.values()) {
			Label lbl = cell.addAttributeToGroup("Defenses", String.format("# %s", def.name));

			labelToStep.put(lbl, def);

			stepIdToLabel.put(def.hash, lbl);
		}
	}

	private void processAttackSteps(MalAsset asset, DataCell cell) {
		for (MalAttackStep step : asset.steps.values()) {
			Label l = cell.addAttributeToGroup("Attack Steps", String.format("%s %s", step.type, step.name));

			labelToStep.put(l, step);
			stepIdToLabel.put(step.hash, l);
		}
	}
}
