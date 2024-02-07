package editor;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tanks.Display;
import graphs.TextureHandler;

import java.io.IOException;
import java.net.URL;

public class Editor extends AnimationTimer {

    //@FXML
    public static Canvas canvas;
    public static CheckBox checkbox;
    public static Slider slider;
    public static Spinner spinner;
    public static ColorPicker colorPicker;
    public static TabPane tabPane;
    private TextureHandler texture;
    public static Stage stage;
    static Scene scene;
    //private static Canvas canvas;
    private static GraphicsContext context;
    private boolean isRunning = false;
    private Level level;
    private Thread gameThread;

    public Editor() {
    }

    public void init(Stage stage) throws IOException {
//        Canvas canvas = new Canvas(800, 600);
//        Editor.canvas = canvas;
//        Editor.context = canvas.getGraphicsContext2D();
//        Editor.context.setImageSmoothing(false);
//        Group gr = new Group(canvas);

//        Editor.scene = new Scene(gr);

        //scene.setBuffer(PlatformImpl.BUFFER_SCALE);


//        Editor.scene.setOnMouseClicked(event -> {
//            System.out.println((int) event.getX() / 16);
//        });





        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/test.fxml");
        loader.setLocation(xmlUrl);
        //Editor.loader = loader;
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Editor.scene = scene;
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        Editor.canvas = (Canvas) loader.getNamespace().get("canvas");
        Editor.checkbox = (CheckBox) loader.getNamespace().get("sounds");
        Editor.slider = (Slider) loader.getNamespace().get("music");
        Editor.spinner = (Spinner) loader.getNamespace().get("levelNum");
        Editor.colorPicker = (ColorPicker) loader.getNamespace().get("background");
        Editor.tabPane = (TabPane) loader.getNamespace().get("pane");
        Editor.spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,12));
        Editor.context = canvas.getGraphicsContext2D();
        Editor.context.setImageSmoothing(false);

        Editor.stage = stage;
        Display.stage.close();
        Display.stage.setScene(scene);
        Display.stage.show();
        //Editor.stage.setScene(scene);
        Editor.stage.setResizable(true);
        //Editor.stage.show();

        this.texture = new TextureHandler("texture.png");
        this.level = new Level("C:\\Users\\Andrey\\IdeaProjects\\EditorFX\\resources\\level1.lvl",texture);
        this.start();
    }
    public void loadLevel(String path){
        this.level = new Level(path, this.texture);
    }
    public void exportLevel(String path){
        this.level.export(path);
    }

//    public void start() {
//        this.isRunning = true;
//        this.gameThread.start();
//    }

    public void render() {
       this.level.render(Editor.context);
    }

    public void update() {
    }

    @Override
    public void handle(long l) {
        this.update();
        this.render();
    }

    public void clearLevel() {
        this.level.clear();
    }
}
