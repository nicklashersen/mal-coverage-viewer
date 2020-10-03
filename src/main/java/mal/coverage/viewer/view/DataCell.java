package mal.coverage.viewer.view;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DataCell extends Cell {
	public static final double FONT_TITLE_SIZE = 12;
	public static final double FONT_SUBTITLE_SIZE = 8;
	public static final double TITLE_PADDING = 5;
	public static final Color  BG_HEAD = Color.LIGHTGREY;

	public Color defaultAttribColor = Color.BLACK;

	private Label subtitle = new Label();
	private Label title = new Label();
	private VBox _attribView = new VBox();

	public Map<String, DataCellAttrib> attribs = new HashMap<>();
	Map<String, TitledPane> attribGroup = new HashMap<>();

	public DataCell(String title, String subtitle) {
		this.title.setText(title);
		this.subtitle.setText(subtitle);

		setStyle("-fx-border-color: black; -fx-border-width: 1");

		// Title style
		this.title.setFont(new Font(FONT_TITLE_SIZE));
		this.title.setTextFill(Color.BLACK);
		this.title.setPadding(new Insets(TITLE_PADDING, TITLE_PADDING, 0, TITLE_PADDING));
		this.title.setStyle("-fx-font-weight: bold");

		// Subtitle style
		this.subtitle.setFont(new Font(FONT_SUBTITLE_SIZE));
		this.subtitle.setTextFill(Color.BLACK);
		this.subtitle.setPadding(new Insets(0, TITLE_PADDING, 0, TITLE_PADDING));
		this.subtitle.setStyle("-fx-border-color: black; -fx-border-width: 0 0 1 0");

		_attribView.setStyle("-fx-padding: 5");
		VBox vert = new VBox(this.title, this.subtitle, _attribView);

		setBackground(new Background(new BackgroundFill(BG_HEAD, null, null)));
		vert.setAlignment(Pos.TOP_CENTER);

		getChildren().add(vert);
	}

	/**
	 * Adds a new attribute group to the data cell. If a group with the 
	 * specified name already exist, the existing group is returned.	
	 *
	 * @param title group title
	 * @return group
	 */	
	public TitledPane addAttribGroup(String title) {
		return attribGroup.computeIfAbsent(title, t -> {
			VBox grp = new VBox();
			TitledPane pane = new TitledPane(title, grp);

			pane.setExpanded(false);

			_attribView.getChildren().add(pane);

			return pane;
		});
	}

	/**
	 * Add an attribute with the specified text to a specific group. 
	 * If no group with the specified name exist, a new group will
	 * be created. The attrib name must be unique (may not exist in another
	 * group). If an attrib with the specified name already exist, the
	 * existing label associated with the name will be returned.	
	 *
	 * @param group attrib group name
	 * @param attrib name
	 * @return label associated with the attribute name
	 */	
	public DataCellAttrib addAttributeToGroup(String group, String attrib) {
		DataCellAttrib res = attribs.computeIfAbsent(attrib, (key) -> {
			TitledPane view = addAttribGroup(group);
			DataCellAttrib label = new DataCellAttrib(attrib, this, view);
	
			((VBox) view.getContent()).getChildren().add(label);
			return label;
		});

		return res;
	}
	
	/**
	 * Set the color of an attribute if present.
	 *
	 * @param name  of the attribute.
	 * @param color new color.
	 */
	public void setAttribColor(String name, Color color) {
		attribs.computeIfPresent(name, (key, label) -> {
			label.setTextFill(color);

			return label;
		});
	}

	/**
	 * Set all attrib entries to the default attrib color.
	 */
	public void resetAllAttribColor() {
		for (Label lbl : attribs.values()) {
			lbl.setTextFill(defaultAttribColor);
		}
	}
}
