package mal.coverage.viewer.view;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class MouseGesture {
    private Graph graph; 

    private double contextY;
    private double contextX;

    public MouseGesture(Graph graph) {
        this.graph = graph;
    }

    /**
     * Make a node draggable.
     * 
     * @param node node to make draggable
     */
    public void makeDraggable(Node node) {
        node.setOnMousePressed(onMousePressedHandler);
        node.setOnMouseDragged(onMouseDragHandler);
        node.setOnMouseReleased(onMouseReleseHandler);
    }

    EventHandler<MouseEvent> onMousePressedHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent event) {
            Node node = (Node) event.getSource();
            double scale = graph.getScale();

            contextX = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
            contextY = node.getBoundsInParent().getMinY() * scale - event.getScreenY();
        }
    };

    EventHandler<MouseEvent> onMouseDragHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent e) {
            Node node = (Node) e.getSource();

            double x = e.getScreenX() + contextX;
            double y = e.getScreenY() + contextY;
            double scale = graph.getScale();

            node.relocate(x / scale, y / scale);

            for  (Edge edge : ((Cell) node).edges.values()) {
                edge.update();
            }

			e.consume();

			graph._onDrag();
        }
    };

    EventHandler<MouseEvent> onMouseReleseHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent e) {}
    };
}
