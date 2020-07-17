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

public class DataCell extends Cell {
    protected MalAsset asset;

    public static final double FONT_NAME_SIZE = 12;
    public static final double FONT_CLASS_SIZE = 8;
    public static final double NAME_PADDING = 5;
    public static final Color BG_HEAD = Color.LIGHTGREY;
    public static final Color COLOR_COMPROMISED = Color.RED;
    public static final Color COLOR_UNCOMPROMISED = Color.GREEN;

    private Label className = new Label();
    private Label name = new Label();

    public Map<String, Label> attribs;

    public DataCell(MalAsset asset) {
        setStyle("-fx-border-color: black; -fx-border-width: 1");
        className.setText(asset.classDesc.className);
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
        attribs = new HashMap<>(asset.classDesc.attackSteps.length + asset.classDesc.defense.length);
        for (String s : asset.classDesc.attackSteps) {
            Label step = new Label(s);

            if (asset.coverage.containsKey(s.toLowerCase())) {
                step.setTextFill(COLOR_COMPROMISED);
            } else {
                step.setTextFill(COLOR_UNCOMPROMISED);
            }

            attribs.put(s, step);
        }

        for (String s : asset.classDesc.defense) {
            attribs.put(s, new Label(s));
        }

        fields.getChildren().addAll(attribs.values());
        fields.setStyle("-fx-padding: 5");

        /* Layout */
        VBox vertLayout = new VBox(name, className, fields);
        setBackground(new Background(new BackgroundFill(BG_HEAD, null, null)));
        vertLayout.setAlignment(Pos.TOP_CENTER);

        getChildren().add(vertLayout);
    }
}