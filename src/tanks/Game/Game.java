package Game;

import java.util.Random;

import IO.Input;
import Updates.GameCommandFabric;
import Utils.SoundPlayer;
import Utils.Time;
import editor.Editor;
import graphs.TextureHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import level.Level;
import level.Point;
import tanks.Display;

public class Game extends AnimationTimer {
    public static final int width = 800;
    public static final int height = 592;
    public static boolean pause = false;
    public static final int frameRate = 30;
    private static State state = State.WELCOME_PAGE;

    public static final float frameInterval = Time.second / Game.frameRate;
    public static final long freeTime = 1;

    public static Point LogoPos = new Point(25, -5);
    public static float LogoVelocity = 0;
    public static float LogoAcceleration = 0.27f;
    public static float LogoOpacity = 0;

    public static final float pressInterval = 0.2f * Time.second;
    public static float pressMoment = Time.getTime();

    public static float screenChangedMoment = Time.getTime();
    public static final float pauseInterval = 2 * Time.second;
    public static float pauseMoment;
    public static final int levelsCount = 12;
    public static int currentLevelNumber = 1;
    public static Random rand = new Random(System.currentTimeMillis());

    public static Editor editor;
    private boolean isRunning = false;
    private Thread gameThread;

    public SoundPlayer soundPlayer = new SoundPlayer();
    public Level level;
    public static long WinMoment = 0;
    public static final long ShowWinDuration = 4 * Time.second;

    private static Stage stage;
    private static GraphicsContext graphics;

    public static Input input;

    public Game(Stage stage) {
        Display.init(stage, Game.width, Game.height);
        Game.stage = stage;
        this.graphics = Display.getContext();
        this.input = new Input();
        Display.addEventListener(input);
        soundPlayer.playMusic();
    }

    @Override
    public void handle(long l) {
        this.update();
        this.render();
    }

    public static void setState(State state) {
        if (Game.state == State.EDIT && state == State.WELCOME_PAGE) {
            Display.init(Game.stage, Game.width, Game.height);
            Game.graphics = Display.getContext();
            Game.input = new Input();
            Display.addEventListener(input);
        }
        Game.state = state;
        //System.out.println(state);
        Game.screenChangedMoment = Time.getTime();
    }

    public void win() {
        Game.state = State.LEVEL_WON;
        Game.WinMoment = Time.getTime();
        Game.LogoPos = new Point(200, -5);
        Game.LogoVelocity = 0;
        SoundPlayer.pauseMusic();
        SoundPlayer.playWinSound();
    }

    public void loose() {
        Game.state = State.LEVEL_LOOSE;
        Game.WinMoment = Time.getTime();
        Game.LogoPos = new Point(200, 270);
        Game.LogoVelocity = 0;
        Game.LogoAcceleration = 0;
        Game.LogoOpacity = 0;
        SoundPlayer.pauseMusic();
        SoundPlayer.playLooseSound();
    }

    public void changeLevel() {
        this.level = new Level(Game.currentLevelNumber, new TextureHandler("texture.png"), this);
    }

    ;


    public synchronized void stopLevel() {
        this.setState(State.LEVEL_LOOSE);
    }

    public void pause() {

        //System.out.println("pause");
        Game.pause = true;
    }

    public void resume() {

        //System.out.println("resume");
        Game.pause = false;
    }

    private void update() {
        if (Game.state == State.EDIT && Game.editor == null) {
            Game.editor = new Editor();
            try {
                Game.editor.init(stage);
                this.input.clear();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (Game.state == State.EDIT) {
            return;
        }
        GameCommandFabric.getUpdate(Game.state).execute(this);
    }

    private void render() {
        Display.clearDisplay();
        if (Game.state == State.WELCOME_PAGE) {
            this.renderWelcomePage(this.graphics);
        } else if (Game.state == State.GAME) {
            this.level.render(this.graphics);
            this.level.renderEntites(this.graphics);
            this.level.renderGrass(this.graphics);
        } else if (Game.state == State.LEVEL_CHOISE) {
            this.renderChoisePage(this.graphics);
        } else if (Game.state == State.LEVEL_WON) {
            this.renderWonPage(this.graphics);
        } else if (Game.state == State.LEVEL_LOOSE) {
            this.renderLoosePage(this.graphics);
        }
    }

    private void renderWonPage(GraphicsContext g) {
        Display.clearDisplay();
        g.setFill(Color.ORANGE);
        //System.out.println(Game.LogoPos.y);
        g.fillText("LEVEL COMPLETED", Game.LogoPos.x, Game.LogoPos.y);
    }

    private void renderWelcomePage(GraphicsContext g) {
        Display.clearDisplay();
        //g.setFill(Color.ORANGE);
        g.setFill(Color.BROWN);
        g.setFont(Font.font("Broken Console", 90));
        g.fillText("BATTLE TANKS", Game.LogoPos.x, Game.LogoPos.y);
        g.fillText("BATTLE TANKS", Game.LogoPos.x, Game.LogoPos.y + 5);
        //g.fillText("BATTLE TANKS", 35, 250);
        g.setFill(Color.ORANGE);
        g.fillText("BATTLE TANKS", Game.LogoPos.x + 5, Game.LogoPos.y);

        //g.drawString("TANKS", 250, 250);
        g.setFont(Font.font("Broken Console", 20));
        g.fillText("PRESS ENTER TO START", Game.LogoPos.x + 225, Game.LogoPos.y + 100);
        g.fillText("PRESS SHIFT + ENTER TO OPEN LEVEL EDITOR", Game.LogoPos.x + 120, Game.LogoPos.y + 140);
        //Display.drawCanvas();
    }

    private void renderLoosePage(GraphicsContext g) {
        g.setGlobalAlpha(Game.LogoOpacity);
        Display.clearDisplay();
        g.setFill(Color.ORANGE);
        g.fillText("GAME OVER", 300, 270);
    }

    private void renderChoisePage(GraphicsContext g) {
        g.setFill(Color.ORANGE);
        g.setFont(Font.font("Broken Console", 40));
        g.fillText("CHOOSE THE LEVEL", 200, 70);
        g.setLineWidth(5);
        for (int i = 1; i <= Game.levelsCount; i++) {
            int y = (i - 1) / 4;
            int x = (i - 1) % 4;
            if (Game.currentLevelNumber == i) {
                g.setFill(Color.WHITE);
                g.setStroke(Color.WHITE);
            } else {
                g.setFill(Color.ORANGE);
                g.setStroke(Color.ORANGE);
            }
            g.strokeRoundRect(20 + 200 * x, 100 + y * 150, 150, 100, 20, 20);

            g.fillText(Integer.toString(i), 80 + 200 * x, 170 + y * 150);
        }
    }

    ;

    //@Override
//    public synchronized void run() {
//        float timesToUpdate = 0;
//
//        int fps = 0;
//        int upd = 0;
//        int updLoops = 0;
//
//        long count = 0;
//
//        long lastTime = Time.getTime();
//        while (this.isRunning) {
//
//            long currentTime = Time.getTime();
//            long interval = currentTime - lastTime;
//            lastTime = currentTime;
//
////			if(Game.pause) {
////				continue;
////			}
//            count += interval;
//            timesToUpdate += (interval / Game.frameInterval);
//            boolean toRender = false;
//            while (timesToUpdate > 1) {
//                this.update();
//                timesToUpdate--;
//
//                if (toRender) {
//                    updLoops++;
//                } else {
//                    toRender = true;
//                }
//                upd++;
//            }
//            if (toRender) {
//                this.render();
//                fps++;
//            } else {
//                try {
//                    this.gameThread.sleep(Game.freeTime);
//                } catch (InterruptedException e) {
//                }
//            }
//
//            if (count >= Time.second) {
//
//                upd = 0;
//                updLoops = 0;
//                fps = 0;
//                count = 0;
//            }
//        }
//    }

    public void cleanUp() {
        Display.clearDisplay();
    }
}
