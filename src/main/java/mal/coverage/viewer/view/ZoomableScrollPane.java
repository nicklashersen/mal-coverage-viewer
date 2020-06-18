package mal.coverage.viewer.view;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.Group;

import javafx.scene.transform.Scale;

import javafx.scene.input.ScrollEvent;

/**
 * Every element added to the zoom group will be scaled during 
 * scroll events. If there is something that we don't want to scale
 * add it to the content group.
 */

public class ZoomableScrollPane extends ScrollPane {
    private Node content;
    private Group zoomGroup;
    private Scale scaleTransform;

    private double scaleValue = 1.0;
    private double scaleDelta = 0.1;

    public static final double MAX_ZOOM_IN = 10.0;
    public static final double MAX_ZOOM_OUT = 0.1;

    public ZoomableScrollPane(Node content) {
	this.content = content;
	Group contentGroup = new Group();

	zoomGroup = new Group();
	contentGroup.getChildren().add(zoomGroup);
	zoomGroup.getChildren().add(this.content);

	setContent(contentGroup);

	scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
	zoomGroup.getTransforms().add(scaleTransform);

	setOnScroll(new ZoomHandler());
    }

    public double getScaleValue() {
	return scaleValue;
    }

    public void zoomToActual() {
	zoomTo(1.0);
    }

    public void zoomTo(double scaleValue) {
	this.scaleValue = scaleValue;

	scaleTransform.setX(scaleValue);
	scaleTransform.setY(scaleValue);
    }

    public void zoomOut() {
	scaleValue -= scaleDelta;

	if (Double.compare(scaleValue, MAX_ZOOM_OUT) < 0) {
	    scaleValue = MAX_ZOOM_OUT;
	}

	zoomTo(scaleValue);
    }

    public void zoomIn() {
	scaleValue += scaleDelta;

	if (Double.compare(scaleValue, MAX_ZOOM_IN) > 0) {
	    scaleValue = MAX_ZOOM_IN;
	}

	zoomTo(scaleValue);
    }

    public void zoomToFit() {
	/* TODO */
	zoomToActual();
    }

    /**
     * Scroll event zoom handler.
     */
    private class ZoomHandler implements EventHandler<ScrollEvent> {

	@Override
	public void handle(ScrollEvent event) {
	    double deltaY = event.getDeltaY();

	    if (deltaY != 0) {
		System.out.println(event.getDeltaY());
		if (event.getDeltaY() < 0.0) {
		    zoomIn();
		} else {
		    zoomOut();
		}

		zoomTo(scaleValue);
	    }

	    event.consume();
	}
    }
}
