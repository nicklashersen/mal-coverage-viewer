package mal.coverage.viewer.view;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Line {
    public final Cell src;
    public final Cell end;

    public Edge(Cell src, Cell end) {
        super();

        getStyleClass().add("edge-unvisited");
        getStyleClass().add("edge");

        this.src = src;
        this.end = end;

        update();
    }

	/**
	 * Update the edge start, end points assuming that
	 * the edge, cells are in the same view.	
	 */	
	public void update() {
	    setStartX(src.getBoundsInParent().getCenterX());
	    setStartY(src.getBoundsInParent().getCenterY());
	
	    setEndX(end.getBoundsInParent().getCenterX());
	    setEndY(end.getBoundsInParent().getCenterY());
	}
	
	
	/**
	 * Update the edge start, end points. Assumes that
	 * the edge and cells are in different views.	
	 *
	 * @param g view of the edge.	
	 */
	public void update(Group grp) {
		Bounds srcB = grp.sceneToLocal(src.localToScene(src.getBoundsInLocal()));
		Bounds endB = grp.sceneToLocal(end.localToScene(end.getBoundsInLocal()));

		setStartX(srcB.getCenterX());
		setStartY(srcB.getCenterY());

		setEndX(endB.getCenterX());
		setEndY(endB.getCenterY());
	}

}
