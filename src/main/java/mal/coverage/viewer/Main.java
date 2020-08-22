/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mal.coverage.viewer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import mal.coverage.viewer.model.MalAsset;
import mal.coverage.viewer.model.MalModel;
import mal.coverage.viewer.model.MalSimulation;
import mal.coverage.viewer.model.util.JSONLoader;
import mal.coverage.viewer.model.util.ModelLoader;
import mal.coverage.viewer.view.Cell;
import mal.coverage.viewer.view.DataCell;
import mal.coverage.viewer.view.Graph;

public class Main extends Application {
	private Stage stage;
	private Graph graph = new Graph();
	private BorderPane root = new BorderPane();
	private TreeView<String> simulationTree = new TreeView<>();
	private TreeItem<String> _simTreeRoot = new TreeItem<>();
	private Map<String, MalModel> _simulations = new HashMap<>();

	@Override
	public void start(Stage primaryStage) {
		MenuBar menuBar = createMenu();

		root.setCenter(graph.getScrollPane());
		root.setTop(menuBar);
		root.setLeft(simulationTree);

		simulationTree.setPrefWidth(200);
		simulationTree.setRoot(_simTreeRoot);
		simulationTree.setShowRoot(false);
		simulationTree.getSelectionModel().selectedItemProperty()
				.addListener(selectionChangedListener);

		Scene scene = new Scene(root, 1024, 769);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();

		stage = primaryStage;
	}

	/**
	 * Create the window menu bar
	 * 
	 * @return a javafx window menu object
	 */
	private MenuBar createMenu() {
		MenuBar menuBar = new MenuBar();

		// File menu
		Menu fileMenu = new Menu("File");
		MenuItem fitem1 = new MenuItem("Open");

		fitem1.setOnAction(e -> {
			FileChooser fChooser = new FileChooser();
			fChooser.setTitle("Open graph file");
			fChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON Files", "*.json"),
					new ExtensionFilter("All Files", "*.*"));

			File selectedFile = fChooser.showOpenDialog(stage);

			if (selectedFile != null)
				loadFile(selectedFile);
		});

		fileMenu.getItems().add(fitem1);

		// View menu
		Menu viewMenu = new Menu("View");
		MenuItem vzoomreset = new MenuItem("Zoom Reset");
		MenuItem vitem1 = new MenuItem("Rearrange Cells");

		vzoomreset.setOnAction(e -> graph.resetZoom());
		vitem1.setOnAction(e -> graph.layoutCells());

		viewMenu.getItems().addAll(vzoomreset, vitem1);
		menuBar.getMenus().addAll(fileMenu, viewMenu);

		return menuBar;
	}

	/**
	 * Construct MalModels from a JSON file containing MAl simulations.
	 * 
	 * @param file JSON file
	 */
	private void loadFile(File file) {
		_simTreeRoot.getChildren().clear();
		_simulations.clear();
		graph.clear();

		List<MalModel> models = getLoader(file).parse(file);

		int nMdls = 1;
		for (MalModel m : models) {
			String mdlName = "Model " + nMdls;
			nMdls++;

			_simulations.put(mdlName, m);
			TreeItem<String> mdlLeaf = new TreeItem<>(mdlName);
			_simTreeRoot.getChildren().add(mdlLeaf);

			Map<String, TreeItem<String>> testClasses = new HashMap<>();
			for (MalSimulation sim : m.simulations.values()) {
				TreeItem<String> simLeaf = new TreeItem<>(sim.name);

				TreeItem<String> testClass = testClasses.computeIfAbsent(sim.className, s -> new TreeItem<>(s));
				testClass.getChildren().add(simLeaf);
			}

			mdlLeaf.getChildren().addAll(testClasses.values());
		}
	}

	/**
	 * Returns a model loader base on the file extension. Defaults 
	 * to the json loader.	
	 *
	 * @param file containing model.
	 * @return loader for specified filetype (if supported);	
	 */	
	private ModelLoader getLoader(File file) { 
		String extension = file.getName().substring(
			file.getName().lastIndexOf('.')).substring(0);
	
			switch (extension) {
			case "json":
			default:
				return new JSONLoader();
			}
	}
	
	/**
	 * Display a MAL model in the graph view. Does not clear
	 * the graph.	
	 * 
	 * @param model MalModel to display
	 */
	private void displayMALModel(MalModel model) {
		for (MalAsset asset : model.assets.values()) {
			DataCell cell = new DataCell(asset);

			graph.addCell(asset.hash, cell);
		}

		// We need to apply the layout in order to get the correct
		// layout values (width, height, etc) from javafx elements.
		root.applyCss();
		root.layout();

		// Add edges between nodes
		for (MalAsset asset : model.assets.values()) {
			for (int nodeId : asset.connections) {
				if (asset.hash > nodeId) {
					graph.addEdge(asset.hash, nodeId);
				}
			}
		}
	}

	private ChangeListener<TreeItem<String>> selectionChangedListener = new ChangeListener<>() {
		private String getModelName(TreeItem<String> item) {
			if (item == null)
				return "";

			if (item.getParent().equals(_simTreeRoot)) {
				// Model node
				return item.getValue();
			} else if (item.getChildren().isEmpty()) {
				// Test method node
				return item.getParent().getParent().getValue();
			} else {
				// Test class node
				return item.getParent().getValue();
			}
		}

	/**
	 * Prepare the model display in the graph view. Return
	 * true if successful, false otherwise.
	 */
		private boolean updateModel(String modelName, String oldMdlName) {
			if (oldMdlName.equals(modelName)) {
				// Same model
				for (Cell c : graph.getCells()) {
					((DataCell) c).resetAllAttribColor();
				}

			} else {
				// New model
				if (_simulations.containsKey(modelName)) {
					graph.clear();
					displayMALModel(_simulations.get(modelName));

				} else {
					new Alert(Alert.AlertType.ERROR, String.format("Model with name '%s' does not exist.", modelName))
							.showAndWait();

					return false;
				}
			}

			return true;
		}

		@Override
		public void changed(ObservableValue<? extends TreeItem<String>> obs, TreeItem<String> old,
				TreeItem<String> selected) {
			// Selection cleared
			if (selected == null)
				return;

			String oldMdlName = getModelName(old);
			String modelName = getModelName(selected);

			if (updateModel(modelName, oldMdlName)) {
			}

		}
	};

	public static void main(String[] args) {
		launch(args);
	}
}
