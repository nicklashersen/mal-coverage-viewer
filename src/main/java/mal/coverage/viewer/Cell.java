package mal.coverage.viewer;

import java.util.Set;
import java.util.HashSet;

import javafx.scene.layout.Pane;

public class Cell extends Pane {
    protected String cellID;

    protected Set<Cell> neighbors  = new HashSet<>();

    public Cell(String cellID) {
	this.cellID = cellID;
    }

    public void link(Cell cell) {
	neighbors.add(cell);
    }

    public void unlink(Cell cell) {
	neighbors.remove(cell);
    }

    public Set<Cell> getNeighbors() {
	return neighbors;
    }
}
