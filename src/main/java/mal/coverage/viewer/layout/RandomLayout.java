package mal.coverage.viewer.layout;

import java.util.Random;

import javafx.scene.Node;

public class RandomLayout extends Layout {
    public double limitX;
    public double limitY;

    public static final double DEFAULT_LIMIT_X = 1000;
    public static final double DEFAULT_LIMIT_Y = 800;


    public RandomLayout() {
        this(DEFAULT_LIMIT_X, DEFAULT_LIMIT_Y);
    }

    public RandomLayout(double x, double y) {

        limitX = x;
        limitY = y;
    }

    @Override
    public void execute(Node n) {
        Random r = new Random();

        n.relocate(r.nextDouble() * limitX, r.nextDouble() * limitY);
    }
}