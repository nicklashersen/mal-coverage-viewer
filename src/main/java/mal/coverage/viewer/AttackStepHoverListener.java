package mal.coverage.viewer;

import java.util.Map;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import mal.coverage.viewer.model.MalAttackStep;
import mal.coverage.viewer.view.Cell;
import mal.coverage.viewer.view.DataCell;
import mal.coverage.viewer.view.Graph;

public class AttackStepHoverListener {
	public Graph graph;
	public Group drawGroup = new Group();
	public Map<Label, MalAttackStep> labelStepMap;
	public Map<Integer, Label> stepIdToLabel;

	public AttackStepHoverListener(Graph g) {
		this.graph = g;

		graph.addLayer(drawGroup);
	}

	/**
	 * Registers the hover handler for all attributes of
	 * a data cell.	
	 */
	public void registerDataCell(DataCell cell) {
		for (Label lbl : cell.attribs.values()) {
			lbl.hoverProperty().addListener(_listener);
		}
	}

	/**
	 * Registeres the hover handler for all the data cells in
	 * the specified graph.	
	 *
	 * @param g graph
	 */
	public void registerDataCellsInGraph(Graph g) {
		for (Cell c : g.getCells()) {
			if (c instanceof DataCell) {
				registerDataCell((DataCell) c);
			}
		}
	}

	private ChangeListener<Boolean> _listener = new ChangeListener<>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean newv) {
			if (newv == false) {
				// Focus lost
				drawGroup.getChildren().clear();

			} else {
				// Hovering
				try {
					ReadOnlyBooleanProperty prop = (ReadOnlyBooleanProperty) obs;
					Label lbl = (Label) prop.getBean();
					MalAttackStep step = labelStepMap.get(lbl);

					Bounds lblSceneBounds = drawGroup
						.sceneToLocal(lbl.localToScene(lbl.getBoundsInLocal()));

					for (int id : step.parents) {
						Label other = stepIdToLabel.get(id);
						Bounds otherSceneBounds = drawGroup
							.sceneToLocal(other.localToScene(other.getBoundsInLocal()));

						Line parentLine = new Line(lblSceneBounds.getCenterX(),
												   lblSceneBounds.getCenterY(),
												   otherSceneBounds.getCenterX(),
												   otherSceneBounds.getCenterY());

						parentLine.getStyleClass().add("parent-edge");
						drawGroup.getChildren().add(parentLine); 

					}
				} catch (Exception e) {
				}
			}

		}
	};
}
