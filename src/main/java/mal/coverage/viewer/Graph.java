package mal.coverage.viewer;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import mal.coverage.viewer.layout.Layout;
import mal.coverage.viewer.layout.RandomLayout;

public class Graph {
    private ZoomableScrollPane scrollPane;

    private Group canvas;

    public Pane cellLayer;
    protected Layout cellLayout;

    public Graph() {
        this(new RandomLayout());
    }

    public Graph(Layout l) {
        canvas = new Group();
        cellLayer = new Pane();
        cellLayout = l;

        canvas.getChildren().add(cellLayer);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    public void clear() {
        cellLayer.getChildren().clear();
    }

    public void addCell(Cell cell) {
        cellLayer.getChildren().add(cell);

        cellLayout.execute(cell);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public double getScale() {
        return scrollPane.getScaleValue();
    }
}
