package Entities;

import IO.Input;
import graphs.Sprite;
import graphs.Sprites;
import graphs.TextureHandler;
import javafx.scene.canvas.GraphicsContext;
import level.Level;

public class Eagle extends Entity{
    private Sprites sprites;
    private Sprite sprite;

    public Eagle(float x, float y, TextureHandler texture) {
        super(EntityTypes.Eagle, x, y);
        this.sprites = new Sprites(texture.getImgPeace(19*16,2*16, 1*16, 16), 1, 16);
        this.sprite = new Sprite(sprites, 2);
    }

    @Override
    public void update(Input input, Level level) {

    }

    @Override
    public void render(GraphicsContext g) {
        this.sprite.render(g, this.x, this.y);
    }

    @Override
    public void getShot(float x, float y, Level level) {
        level.addEntity(new BigExplosion(x, y, level.texture) );
    }
}
