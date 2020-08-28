package mal.coverage.viewer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mal.coverage.viewer.model.coverage.CoverageData;

public class CoverageWindow {
	public final int DEFAULT_HEIGHT = 200;
	public final int DEFAULT_WIDTH = 500;

	private CoverageData data;
	private Stage stage = new Stage();
	private VBox root = new VBox();
	private TableView table = new TableView();

	public CoverageWindow(CoverageData data) {
		if (data != null) {
			TableColumn tcName = new TableColumn("Type");
			TableColumn tcAll = new TableColumn("Total");
			TableColumn tcComp = new TableColumn("Compromised");
			TableColumn tcPercentage = new TableColumn("Percentage");

			this.data = data;

			tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
			tcAll.setCellValueFactory(new PropertyValueFactory<>("max"));
			tcComp.setCellValueFactory(new PropertyValueFactory<>("compromised"));
			tcPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));

			tcName.setMinWidth(200);
			table.getColumns().addAll(tcName, tcAll, tcComp, tcPercentage);
			root.getChildren().add(table);

			stage.setTitle("Coverage - " + data.selectionName);
			stage.setScene(new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT));

			displayData();

			stage.sizeToScene();
			stage.show();
		} else {
			new Alert(Alert.AlertType.ERROR, "Failed to read coverage data: was null").showAndWait();
			stage.close();
		}
	}

	public void displayData() {
		table.getItems().clear();

		for (CoverageData.Entry entry : data) {
			table.getItems().add(new CoverageProperty(entry));
		}
	}

	protected class CoverageProperty {
		public Double percentage;
		public Integer max;
		public Integer compromised;
		public String name;

		public CoverageProperty(CoverageData.Entry entry) {
			name = entry.name;
			compromised = new Integer(entry.nCompromised);
			max = new Integer(entry.nMax);
			percentage = new Double((double) entry.nCompromised / entry.nMax);
		}

		public Double getPercentage() {
			return percentage;
		}

		public Integer getMax() {
			return max;
		}

		public Integer getCompromised() {
			return compromised;
		}

		public String getName() {
			return name;
		}
	}
}
