package mal.coverage.viewer.view;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalDefense;
import mal.coverage.viewer.model.MalAttackStep;

public class DataCell extends Cell {
    protected MalAsset asset;

    public static final double FONT_NAME_SIZE = 12;
    public static final double FONT_CLASS_SIZE = 8;
    public static final double NAME_PADDING = 5;
    public static final Color BG_HEAD = Color.LIGHTGREY;
    public static final Color COLOR_COMPROMISED = Color.RED;
    public static final Color COLOR_COMPROMISED_EFFORT = Color.YELLOW;
    public static final Color COLOR_UNCOMPROMISED = Color.GREEN;

    /*
     * Same valus as used in assertCompromisedWithEffort (MAL:AttackStep.java) 
     */
    public static final double COMPROMISED_WITH_EFFORT_LOW = 1.0 / 1444;
    public static final double COMPROMISED_WITH_EFFORT_HIGH = 1000.0;

    private Label className = new Label();
    private Label name = new Label();

    public Map<String, Label> attribs;

    public DataCell(MalAsset asset) {
        setStyle("-fx-border-color: black; -fx-border-width: 1");
        className.setText(asset.assetName);
        name.setText(asset.name);

        /* Font sizes */
        name.setFont(new Font(FONT_NAME_SIZE));
        name.setTextFill(Color.BLACK);
        name.setPadding(new Insets(NAME_PADDING, NAME_PADDING, 0, NAME_PADDING));
        name.setStyle("-fx-font-weight: bold");
        className.setFont(new Font(FONT_CLASS_SIZE));
        className.setTextFill(Color.BLACK);
        className.setPadding(new Insets(0, NAME_PADDING, 0, NAME_PADDING));
        className.setStyle("-fx-border-color: black; -fx-border-width: 0 0 1 0");

        /* Attack steps and defenses */
        VBox fields = new VBox();
        attribs = _compAttackSteps(asset);

        for (MalDefense s : asset.defense.values()) {
            attribs.put(s.name, new Label("# " + s.name));
        }

        fields.getChildren().addAll(attribs.values());
        fields.setStyle("-fx-padding: 5");

        /* Layout */
        VBox vertLayout = new VBox(name, className, fields);
        setBackground(new Background(new BackgroundFill(BG_HEAD, null, null)));
        vertLayout.setAlignment(Pos.TOP_CENTER);

        getChildren().add(vertLayout);
    }

    /**
     * Compute attack step node attributes and color based on coverage.
     * 
     * @param asset mal asset desciption.
     * @return attribute, label map.
     */
    private Map<String, Label> _compAttackSteps(MalAsset asset) {
        Map<String, Label> attribs;

        attribs = new HashMap<>(asset.steps.size() + asset.defense.size());
        for (MalAttackStep step : asset.steps.values()) {
            Label lblStep = new Label(String.format("%s %s", step.type, step.name));

	    if (step.ttc < COMPROMISED_WITH_EFFORT_LOW) {
		lblStep.setTextFill(COLOR_COMPROMISED);
	    } else if (step.ttc < COMPROMISED_WITH_EFFORT_HIGH) {
		lblStep.setTextFill(COLOR_COMPROMISED_EFFORT);
	    } else {
		lblStep.setTextFill(COLOR_UNCOMPROMISED);
	    }
	    attribs.put(step.name, lblStep);
	}

	return attribs;
    }
}
