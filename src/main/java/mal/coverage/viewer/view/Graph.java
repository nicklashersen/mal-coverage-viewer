package mal.coverage.viewer.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import mal.coverage.viewer.layout.Layout;
import mal.coverage.viewer.layout.RandomLayout;

public class Graph {
    private ZoomableScrollPane scrollPane;
    private MouseGesture mouseGesture;

    private Group canvas = new Group();
    private Map<Integer, Cell> cells = new HashMap<>();
    private List<Edge> edges = new LinkedList<>();

    private Group cellLayer = new Group();

    protected Layout cellLayout;

	public List<Runnable> dragListeners = new ArrayList<>();

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
        cellLayout = l;
        mouseGesture = new MouseGesture(this);

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
        cells.clear();
        edges.clear();
    }

	/**
	 * Add another (draw) layer to the graph.
	 */
	public void addLayer(Node layer) {
		canvas.getChildren().add(layer);
	}

	/**
	 * Add a cell to the graph view.
	 * @param cell
	 */
    public void addCell(int id, Cell cell) {
        cells.put(id, cell);

        cellLayout.execute(cell);
        cellLayer.getChildren().add(cell);

        mouseGesture.makeDraggable(cell);
    }

    /**
     * Add an edge between two cells with the corresponding ids.
     * If no cell with a specified id has been registered nothing 
     * will be done.
     * 
     * @param id1 cell id 1
     * @param id2 cell id 2
     */
    public void addEdge(int id1, int id2) {
        if (id1 == id2) {
            throw new IllegalArgumentException("Cannot add edge to itself");
        }

        try {
            Cell cell1 = cells.get(id1);
            Cell cell2 = cells.get(id2);

            addEdge(cell1, cell2);
        } catch (Exception e) {}
    }

    /**
     * Add a new edge between two cells.
     * 
     * @param c1 cell 1
     * @param c2 cell 2
     */
    public void addEdge(Cell c1, Cell c2) {
		if (c1.edges.containsKey(c2))
			return;

        Edge e = new Edge(c1, c2);
        c1.edges.put(c2, e);
        c2.edges.put(c1, e);

        edges.add(e);

        cellLayer.getChildren().add(e);
        e.toBack();
    }
    /**
     * Fetch a cell from the graph.
     * 
     * @param id cell id.
     * @return cell associated with the corresponding id.
     */
    public Cell getCell(int id) {
        return cells.get(id);
    }

	/**
	 * Returns a collection of all registered cells.
	 *
	 * @return a collection of all cells.
	 */	
	public Collection<Cell> getCells() {
		return cells.values();
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

    /**
     * Reset zoom.
     */
    public void resetZoom() {
        scrollPane.zoomToActual();
    }

    /**
     * Run the cell layout algorithm.
     */
    public void layoutCells() {
        cellLayout.execute(cells.values());
        edges.forEach(e -> e.update());
    }

	void _onDrag() {
		dragListeners.forEach(r -> r.run());
	}
}
