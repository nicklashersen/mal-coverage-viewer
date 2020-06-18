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
        setStartX(src.getLayoutX() + src.getWidth());
        setStartY(src.getLayoutY() + src.getHeight());

        setEndX(end.getLayoutX() + end.getWidth() / 2);
        setEndY(end.getLayoutY() + end.getHeight() / 2);
    }
}
