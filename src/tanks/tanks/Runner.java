package tanks;

import Game.Game;
import editor.Editor;
import editor.Level;
import editor.TileType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Game.State;
import java.io.File;

public class Runner extends Application {
	private Game game = null;
	private Stage stage = null;
	public static void main(String[] args) {
		Runner.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.game = new Game(stage);
		this.stage = stage;
		game.start();
	}

	@FXML
	public void toggleButton1Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.brick;
	}
	@FXML
	public void toggleButton2Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.metal;
	}
	@FXML
	public void toggleButton3Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.water;
	}
	@FXML
	public void toggleButton4Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.grass;
	}
	@FXML
	public void toggleButton5Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.ice;
	}
	@FXML
	public void toggleButton6Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.empty;
	}
	@FXML
	public void toggleButton7Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.player;
	}
	@FXML
	public void toggleButton8Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.hard;
	}
	@FXML
	public void toggleButton9Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.fast;
	}
	@FXML
	public void toggleButton10Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.enemy;
	}
	@FXML
	public void toggleButton11Click(ActionEvent actionEvent){
		Level.tileSelected = TileType.eagle;
	}

	public void loadFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("levels", "*.lvl")
		);

		fileChooser.setInitialDirectory(new File("C:\\Users\\Andrey\\IdeaProjects"));
		// Показать диалог выбора файла
		File selectedFile = fileChooser.showOpenDialog(Editor.stage);

		if (selectedFile != null) {
			Game.editor.loadLevel(selectedFile.getAbsolutePath());
		}
	}

	public void exportFile(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("levels", "*.lvl")
		);

		fileChooser.setInitialDirectory(new File("C:\\Users\\Andrey\\IdeaProjects"));
		fileChooser.setInitialFileName("level" + Editor.spinner.getValue() + ".lvl");
		System.out.println(Editor.spinner);
		System.out.println(Editor.spinner.getValue());
		// Показать диалог выбора файла
		File selectedFile = fileChooser.showSaveDialog(Editor.stage);

		if (selectedFile != null) {
			Game.editor.exportLevel(selectedFile.getAbsolutePath());
		}
	}
	public void clearLevel(ActionEvent actionEvent) {
		Game.editor.clearLevel();
	}

	public void BackClick(ActionEvent actionEvent) {
		Game.setState(State.WELCOME_PAGE);
	}

	public void changeBG(ActionEvent actionEvent) {
		Color color = Editor.colorPicker.getValue();
		String hex = String.format("#%02X%02X%02X",
				(int) (color.getRed() * 255),
				(int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));

		Editor.tabPane.setStyle("-fx-background-color: "+ hex);
		System.out.println(Editor.colorPicker.getValue());
	}

	public void showAbout(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText(null);
		alert.setContentText("Программа разработана в качестве курсовой работы студентом группы 1413 Толоконниковым А. И.");
		alert.showAndWait();
	}
}
