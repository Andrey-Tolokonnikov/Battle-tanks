package Entities;

import IO.Input;
import Utils.SoundPlayer;
import graphs.Sprite;
import graphs.Sprites;
import graphs.TextureHandler;
import javafx.scene.canvas.GraphicsContext;
import level.Level;

public class BoosterStar extends Entity{
    private float x;
    private float y;
    //public boolean toDestroy = false;
    private Sprites sprites;
    private Sprite sprite;
    private int framesPerLife = 32;

    public BoosterStar(float x, float y, TextureHandler texture) {
        super(EntityTypes.Explosion, x, y);
        this.x = x;
        this.y = y;
        this.sprites = new Sprites(texture.getImgPeace(16*16,6*16, 4*16, 16), 4, 16);
        this.sprite = new Sprite(sprites, 1.5f, 7, 1);
        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.playExplosion();
    }

    @Override
    public void update(Input input, Level level) {}
    @Override
    public void render(GraphicsContext g) {
        this.framesPerLife--;
        if(this.framesPerLife <= 0) {
            this.toDestroy = true;
            return;
        }
        this.sprite.render(g, this.x, this.y);
    }

    @Override
    public void getShot(float x, float y, Level level) {
        // TODO Auto-generated method stub

    }
}
