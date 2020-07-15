package mal.coverage.viewer.view;

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

    public void update() {
        setStartX(src.getBoundsInParent().getCenterX());
        setStartY(src.getBoundsInParent().getCenterY());

        setEndX(end.getBoundsInParent().getCenterX());
        setEndY(end.getBoundsInParent().getCenterY());
    }
}
