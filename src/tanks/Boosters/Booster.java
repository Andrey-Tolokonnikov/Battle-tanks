package Boosters;

import Entities.BoosterStar;
import IO.Input;
import Utils.Time;
import graphs.Sprite;
import graphs.Sprites;
import graphs.TextureHandler;
import javafx.scene.canvas.GraphicsContext;
import level.Level;

public class Booster {
    public final BoosterTypes type;
    public float x;
    public float y;
    private float opacity = 1;
    private float opacityStep = 0.02f;
    private Sprites sprites;
    private Sprite sprite;
    private boolean toDestroy;
    private TextureHandler texture;

    private static final long lifeTime = Time.second*15;
    private final long bornMoment = Time.getTime();

    public Booster(BoosterTypes type, TextureHandler texture, float x, float y) {
        this.x = x;
        this.type = type;
        this.y = y;
        this.texture = texture;
        switch(this.type){
            case SPEED:{
                this.sprites = new Sprites(texture.getImgPeace(17*16,7*16, 16, 16), 1, 16);
                this.sprite = new Sprite(this.sprites, 2f);
                break;
            }
            case DEFENCE:{
                this.sprites = new Sprites(texture.getImgPeace(16*16,7*16, 16, 16), 1, 16);
                this.sprite = new Sprite(this.sprites, 2f);
                break;
            }
            case LIFE:{
                this.sprites = new Sprites(texture.getImgPeace(21*16,7*16, 16, 16), 1, 16);
                this.sprite = new Sprite(this.sprites, 2f);
                break;
            }
            case SHOVEL:{
                this.sprites = new Sprites(texture.getImgPeace(18*16,7*16, 16, 16), 1, 16);
                this.sprite = new Sprite(this.sprites, 2f);
                break;
            }
        }
    }

    public void update(Level level){
        if(this.opacity<0 || this.opacity>1){
            this.opacityStep *= -1;
        }
        this.opacity += this.opacityStep;
        if(Time.getTime() > this.bornMoment + Booster.lifeTime){
            this.Destroy(level);
        }
    };
    public void render(GraphicsContext g){
        g.setGlobalAlpha(this.opacity);
        this.sprite.render(g, this.x, this.y);
        g.setGlobalAlpha(1);
    };
    public void Destroy(Level level){
        this.toDestroy = true;
        level.addEntity(new BoosterStar(this.x, this.y, texture));
    }
    public void getTaken(Level level){};
    public Boolean getToDestroy(){
        return this.toDestroy;
    }

}
