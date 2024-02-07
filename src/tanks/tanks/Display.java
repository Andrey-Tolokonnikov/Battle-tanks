package tanks;

import java.util.Arrays;


import IO.Input;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Display {
	public static Stage stage;
	public static Scene scene;
	private static Canvas canvas;
	private static GraphicsContext context;

	public static void init(Stage stage, int width, int height) {
		Canvas canvas = new Canvas(width, height);
		Display.canvas = canvas;
		Display.context = canvas.getGraphicsContext2D();
	Display.context.setImageSmoothing(false);
		Group gr = new Group(canvas);

		Display.scene = new Scene(gr);

		Display.stage = stage;
		Display.stage.setScene(scene);
		Display.stage.setResizable(false);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Platform.exit();
				System.exit(0);
			}
		});
		Display.stage.show();
	}

	public static void clearDisplay() {
		Display.context.setFill(Color.BLACK);
		Display.context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public static GraphicsContext getContext() {
		return Display.context;
	}
	
	public static void close() {
		Display.stage.close();
	}
	
	public static void addEventListener(Input eventListener) {
		Display.scene.setOnKeyPressed(event->{
			eventListener.onKeyDown(event);
		});
		Display.scene.setOnKeyReleased(event->{
			eventListener.onKeyUp(event);
		});
	}
}
