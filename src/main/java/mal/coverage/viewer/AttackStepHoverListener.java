package mal.coverage.viewer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import mal.coverage.viewer.view.Cell;
import mal.coverage.viewer.view.DataCell;
import mal.coverage.viewer.view.Graph;

public class AttackStepHoverListener {
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

	public String pb(boolean v) {
		return v ? "true" : "false";
	}

	private ChangeListener<Boolean> _listener = new ChangeListener<>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean newv) {
			System.out.println(obs.toString() + " " + pb(old) + " -> " + pb(newv));

		}
	};
}
