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
import mal.coverage.viewer.model.MalAbstractStep;
import mal.coverage.viewer.view.Cell;
import mal.coverage.viewer.view.DataCell;
import mal.coverage.viewer.view.Graph;

public class AttackStepHoverListener {
	public Graph graph;
	public Group drawGroup = new Group();
	public Map<Label, MalAbstractStep> labelStepMap;
	public Map<Integer, Label> stepIdToLabel;

	public boolean showParents;

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

		private void showParentSteps(MalAbstractStep step, Label lbl) {
			Bounds lblSceneBounds = getDrawBounds(lbl);

			for (int id : step.parents) {
				Label other = stepIdToLabel.get(id);
				Bounds otherSceneBounds = getDrawBounds(other);

				drawLine(lblSceneBounds, otherSceneBounds);
			}
		}


		private Bounds getDrawBounds(Label start) {
			return drawGroup.sceneToLocal(start.localToScene(start.getBoundsInLocal()));
		}

		private void drawLine(Bounds start, Bounds end) {
			Line parentLine = new Line(start.getCenterX(),
									   start.getCenterY(),
									   end.getCenterX(),
									   end.getCenterY());

			parentLine.getStyleClass().add("parent-edge");
			drawGroup.getChildren().add(parentLine); 
		}

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
					MalAbstractStep step = labelStepMap.get(lbl);

					if (showParents) {
						showParentSteps(step, lbl);
					}

				} catch (Exception e) {}
			}
		}
	};
}
