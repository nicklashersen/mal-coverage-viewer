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

    /**
     * Create a new graph with the default (random) layout
     * stratgey.
     */
    public Graph() {
        this(new RandomLayout());
    }

    /**
     * Create a new graph with a specfic layout.
     * 
     * @param l layout strategy
     */
    public Graph(Layout l) {
        canvas = new Group();
        cellLayer = new Pane();
        cellLayout = l;

        canvas.getChildren().add(cellLayer);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    /**
     * Remove all cells displayed in the graph.
     */
    public void clear() {
        cellLayer.getChildren().clear();
    }

    /**
     * Add a cell to the graph view.
     * @param cell
     */
    public void addCell(Cell cell) {
        cellLayer.getChildren().add(cell);

        cellLayout.execute(cell);
    }

    /**
     * Get the underlying scroll pane.
     * 
     * @return scroll pane
     */
    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Get the curren scale (zoom)
     * 
     * @return zoom
     */
    public double getScale() {
        return scrollPane.getScaleValue();
    }
}
