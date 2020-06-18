package mal.coverage.viewer.view;

public class Edge {
    public final Cell src;
    public final Cell end;

    public Edge(Cell src, Cell end) {
	this.src = src;
	this.end = end;
    }
}
