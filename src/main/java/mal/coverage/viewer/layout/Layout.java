package mal.coverage.viewer.layout;

import javafx.scene.Node;
import java.util.List;

public abstract class Layout {
    public void execute(List<Node> nodes) {
        for (Node n : nodes) execute(n);
    }

    public abstract void execute(Node n);
}
