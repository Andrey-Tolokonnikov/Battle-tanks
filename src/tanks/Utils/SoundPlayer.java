package Utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class SoundPlayer {
    private static boolean toPlaySounds = true;

    private static final Media music = new Media(new File("resources/music.mp3").toURI().toString());
    private static final MediaPlayer mediaPlayer = new MediaPlayer(SoundPlayer.music);

    private final Media choise = new Media(new File("resources/choise.mp3").toURI().toString());
    private final MediaPlayer choisePlayer = new MediaPlayer(choise);

    private final Media shot = new Media(new File("resources/shot.mp3").toURI().toString());
    private final MediaPlayer shotPlayer = new MediaPlayer(this.shot);

    private final Media explosion = new Media(new File("resources/explosion.mp3").toURI().toString());
    private final MediaPlayer explosionPlayer = new MediaPlayer(this.explosion);

    private final Media bigExplosion = new Media(new File("resources/bigExplosion.mp3").toURI().toString());
    private final MediaPlayer bigExplosionPlayer = new MediaPlayer(this.bigExplosion);

    private static final Media winSound = new Media(new File("resources/winSound.mp3").toURI().toString());
    private static final MediaPlayer winSoundPlayer = new MediaPlayer(winSound);

    private static final Media looseSound = new Media(new File("resources/looseSound.mp3").toURI().toString());
    private static final MediaPlayer looseSoundPlayer = new MediaPlayer(looseSound);
    static{
        mediaPlayer.setVolume(0.05);
    }

    public static void playMusic(){
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
    }
    public static void pauseMusic(){
        mediaPlayer.pause();
    }
    public static void resumeMusic(){
        mediaPlayer.play();
    }

    public static void setVolume(float v) {
        mediaPlayer.setVolume(v);
    }

    public void playShot(){
        if(SoundPlayer.toPlaySounds){
        shotPlayer.seek(shotPlayer.getStartTime());
        shotPlayer.play();}
    }
    public static void setToPlaySounds(boolean value){
        SoundPlayer.toPlaySounds = value;
    }
    public static void playWinSound(){
        SoundPlayer.winSoundPlayer.seek(winSoundPlayer.getStartTime());
        SoundPlayer.winSoundPlayer.play();
    }
    public static void playLooseSound(){
        SoundPlayer.looseSoundPlayer.seek(looseSoundPlayer.getStartTime());
        SoundPlayer.looseSoundPlayer.play();
    }
    public void playExplosion(){
        if(SoundPlayer.toPlaySounds) {
            explosionPlayer.seek(explosionPlayer.getStartTime());
            explosionPlayer.play();
        }
    }
    public void playChoise(){
        choisePlayer.seek(choisePlayer.getStartTime());
        choisePlayer.play();
    }

    public void playBigExplosion() {
        if(SoundPlayer.toPlaySounds) {
            bigExplosionPlayer.seek(bigExplosionPlayer.getStartTime());
            bigExplosionPlayer.play();
        }
    }
}
