package mal.coverage.viewer.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalModel;
import mal.coverage.viewer.view.Cell;
import mal.coverage.viewer.view.Edge;
import mal.coverage.viewer.view.Graph;

public class AssociationInformationOverlay {
	private Graph graph;
	private MalModel lastModel;

	private Group associationGroup = new Group();
	private Group assetAssociationGroup = new Group();
	private Group stepAssociationGroup = new Group();


	public BooleanProperty showAssetAssociations = new SimpleBooleanProperty(true);
	public BooleanProperty showStepAssociations = new SimpleBooleanProperty(false);

	public AssociationInformationOverlay() {
		showAssetAssociations.addListener((prop, old, newV) -> {
			updateAssociations();
		});

		associationGroup.getChildren().addAll(assetAssociationGroup, stepAssociationGroup);
	}

	public AssociationInformationOverlay(Graph g) {
		this();
		setGraph(g);
	}

	/**
	 * Sets the graph view used to draw the assocaitions.
	 *
	 * @param g graph to draw on.
	 */
	public void setGraph(Graph g) {
		graph = g;
		graph.addLayer(associationGroup);
		associationGroup.toBack();

		graph.dragListeners.add(() -> {
			this.update();
		});
	}

	/**
	 * Updates all associations based on current options.
	 *
	 * @param mdl model to process associations from.
	 */
	public void updateAssociations(MalModel mdl) {
		lastModel = mdl;

		if (graph == null || lastModel == null) return;

		drawAssetAssociations(lastModel);
		drawStepAssociations(lastModel);

		updateAssociations();
	}

	/**
	 * Updates the visability of each assocaition layer.
	 */	
	private void updateAssociations() {
		if (graph == null || lastModel == null) return;
	
		assetAssociationGroup.setVisible(showAssetAssociations.getValue());
		stepAssociationGroup.setVisible(showStepAssociations.getValue());
		update();
	}
	
	/**
	 * Updates the position of all visible edges.
	 */	
	public void update() {
		if (showAssetAssociations.getValue()) {
			for (Node n : assetAssociationGroup.getChildren()) {
				((Edge) n).update(assetAssociationGroup);
			}
		}
	
		if (showStepAssociations.getValue()) {
			for (Node n : stepAssociationGroup.getChildren()) {
				((Edge) n).update(stepAssociationGroup);
			}
		}
	}
	
	/**
	* Draw asset association lines. A line represents a direct
	* relationship between two assets. 
	*
	* @param mdl model to process.
	*/
	private void drawAssetAssociations(MalModel mdl) {
		for (MalAsset asset : mdl.assets.values()) {
			Cell start = graph.getCell(asset.hash);

			for (int nodeID : asset.connections) {
				Cell end = graph.getCell(nodeID);

				if (asset.hash <= nodeID) {
					Edge line = new Edge(start, end);

					line.getStyleClass().add("association-edge");
					assetAssociationGroup.getChildren().add(line);
				}
			}
		}
	}

	private void drawStepAssociations(MalModel mdl) {
	}
}
