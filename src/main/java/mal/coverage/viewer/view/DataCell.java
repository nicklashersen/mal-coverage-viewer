package mal.coverage.viewer.view;

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

    public static final double FONT_NAME_SIZE = 24;
    public static final double FONT_CLASS_SIZE = 12;

    public static Color BG_HEAD = Color.LIGHTSKYBLUE;

    public DataCell(MalAsset asset) {
        Label className = new Label(asset.classDesc.className);
        Label name = new Label(asset.name);

        VBox vertLayout = new VBox(name, className);

        setBackground(new Background(new BackgroundFill(BG_HEAD, null, null)));
        vertLayout.setAlignment(Pos.BASELINE_CENTER);
        vertLayout.setPadding(new Insets(10));

        /* Font sizes */
        className.setFont(new Font(FONT_CLASS_SIZE));
        className.setTextFill(Color.WHITE);
        name.setFont(new Font(FONT_NAME_SIZE));
        name.setTextFill(Color.WHITE);

        getChildren().add(vertLayout);
    }
}