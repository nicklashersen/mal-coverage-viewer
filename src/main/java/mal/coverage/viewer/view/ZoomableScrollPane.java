package mal.coverage.viewer.view;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.Node;
import javafx.scene.Group;

/**
 * Every element added to the zoom group will be scaled during scroll events. If
 * there is something that we don't want to scale add it to the content group.
 */

public class ZoomableScrollPane extends ScrollPane {
	private Node content;
	private Group contentGroup;
	private Group zoomGroup;

	private double scaleValue = 1.0;
	private double zoomIntensity = 0.002;

	public final double MIN_ZOOM_VALUE = 0.1;
	public final double MAX_ZOOM_VALUE = 10;

	public ZoomableScrollPane(Node content) {
		super();

		this.content = content;
		zoomGroup = new Group(this.content);
		contentGroup = new Group(zoomGroup);

		addEventFilter(ScrollEvent.ANY, e -> {
			scroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
			e.consume();
		});

		setContent(contentGroup);

		setPannable(true);
		setHbarPolicy(ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollBarPolicy.NEVER);
		setFitToHeight(true);
		setFitToWidth(true);
	}

	public double getScaleValue() {
		return scaleValue;
	}

	public void zoomToActual() {
		zoomTo(1.0);
	}

	public void zoomTo(double scaleValue) {
		scaleValue = scaleValue <= MIN_ZOOM_VALUE ? MIN_ZOOM_VALUE : scaleValue;
		scaleValue = scaleValue >= MAX_ZOOM_VALUE ? MAX_ZOOM_VALUE : scaleValue;

		this.scaleValue = scaleValue;

		content.setScaleX(scaleValue);
		content.setScaleY(scaleValue);
	}

	public void zoomToFit() {
		/* TODO */
		zoomToActual();
	}

	public void scroll(double amount, Point2D mousePos) {
			double zoomFactor = Math.exp(amount * zoomIntensity);

			Bounds innerBounds = zoomGroup.getLayoutBounds();
			Bounds viewportBounds = getViewportBounds();

			double valX = getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
			double valY = getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

			scaleValue = scaleValue * zoomFactor;
			zoomTo(scaleValue);
			layout();

			Point2D posInZoomTarget = content.parentToLocal(zoomGroup.parentToLocal(mousePos));

			Point2D adjustment = content.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

			Bounds updatedInnerBounds = zoomGroup.getBoundsInLocal();
			setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
			setHvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
	}
}
