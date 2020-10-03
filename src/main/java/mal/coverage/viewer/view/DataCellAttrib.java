package mal.coverage.viewer.view;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class DataCellAttrib extends Label {
	private final DataCell parent;
	private final TitledPane parentPane;

	DataCellAttrib(String title, DataCell parent, TitledPane ppane) {
		super(title);
		this.parent = parent;
		parentPane = ppane;
	}

	/**
	 * Returns the bounds of an attribute in the data cell. If the cell is 
	 * in a non-expanded group the bounds of the DataCell is returned.
	 *
	 * @param title attrib title
	 * @return attrib bounds or DataCell bounds, opt. of null in non-existant.
	 */	
	public Bounds getBoundsInScene() {
		if (!parentPane.isExpanded()) {
			return parent.localToScene(parent.getBoundsInLocal());
		} else {
			return localToScene(getBoundsInLocal());
		}
	}
}
