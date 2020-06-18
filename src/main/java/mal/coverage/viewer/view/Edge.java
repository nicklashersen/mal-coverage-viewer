package mal.coverage.viewer.view;

import javafx.scene.shape.Line;

public class Edge extends Line {
    public final Cell src;
    public final Cell end;

    public Edge(Cell src, Cell end) {
        super(src.getLayoutX(), src.getLayoutY(), end.getLayoutX(), end.getLayoutY());

        getStyleClass().add("edge-unvisited");
        getStyleClass().add("edge");

        this.src = src;
        this.end = end;

    }
}
