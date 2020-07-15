package mal.coverage.viewer.layout;

import javafx.scene.Node;
import java.util.Collection;

public abstract class Layout {
    public void execute(Collection<? extends Node> nodes) {
        for (Node n : nodes) execute(n);
    }

    public abstract void execute(Node n);
}
