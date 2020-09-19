package mal.coverage.viewer.view;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.Pane;

public class Cell extends Pane {
	public Map<Cell, Edge> edges = new HashMap<>();
}
