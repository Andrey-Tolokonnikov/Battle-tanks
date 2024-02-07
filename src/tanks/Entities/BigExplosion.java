package Entities;

import IO.Input;
import Utils.SoundPlayer;
import graphs.Sprite;
import graphs.Sprites;
import graphs.TextureHandler;
import javafx.scene.canvas.GraphicsContext;
import level.Level;

public class BigExplosion extends Entity{
    private float x;
    private float y;
    //public boolean toDestroy = false;
    private Sprites sprites;
    private Sprite sprite;
    private int framesPerLife = 60;

    public BigExplosion(float x, float y, TextureHandler texture) {
        super(EntityTypes.BigExplosion, x, y);
        this.x = x;
        this.y = y;
        this.sprites = new Sprites(texture.getImgPeace(19*16,8*16, 4*16, 2*16), 2, 32);
        this.sprite = new Sprite(sprites, 4f, 30, 1);
        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.playBigExplosion();
    }

    @Override
    public void update(Input input, Level level) {}
    @Override
    public void render(GraphicsContext g) {
        this.framesPerLife--;

        if(this.framesPerLife <= 0) {
            this.toDestroy = true;
            System.out.println("hello");
            this.toStopGame = true;
            return;
        }
        this.sprite.render(g, this.x-48, this.y-48);
    }

    @Override
    public void getShot(float x, float y, Level level) {
        // TODO Auto-generated method stub

    }
}
