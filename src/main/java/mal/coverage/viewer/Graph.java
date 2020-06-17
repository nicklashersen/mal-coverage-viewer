package mal.coverage.viewer;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.Pane;

public class Graph {
    private ZoomableScrollPane scrollPane;

    private Group canvas;
    protected Pane cellLayer;

    public Graph() {
	canvas = new Group();
	cellLayer = new Pane();

	canvas.getChildren().add(cellLayer);

	scrollPane = new ZoomableScrollPane(canvas);

	scrollPane.setFitToWidth(true);
	scrollPane.setFitToHeight(true);
    }

    public ScrollPane getScrollPane() {
	return scrollPane;
    }

    public double getScale() {
	return scrollPane.getScaleValue();
    }
}
