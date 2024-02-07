package Entities;

//import java.awt.Graphics2D;
//import java.awt.event.KeyEvent;
//import java.awt.image.BufferedImage;

import java.util.*;

import Boosters.Booster;
import Boosters.BoosterTime;
import Boosters.BoosterTypes;
import Game.Game;

import IO.Input;
import Particles.ParticleEmitter;
import Utils.SoundPlayer;
import Utils.Time;
import graphs.Sprite;
import graphs.Sprites;
import graphs.TextureHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import level.Level;
import level.TileType;

public class Player extends Entity {
    public static final int PixelsSize = 16;
    public static final int SpritesPerDirection = 2;
    public static float scale;
    public static final long shootInterval = Time.second;
    private long lastShootTime = Time.getTime();
    private SoundPlayer soundPlayer = new SoundPlayer();
    public List<BoosterTime> activeBoosters = new ArrayList<BoosterTime>();
    private final long boosterLifetime = Time.second * 5;
    public int lives = 1;
    private List<ParticleEmitter> emitters = new ArrayList<ParticleEmitter>();

    public enum Direction {
        UP(0 * Player.PixelsSize, 0 * Player.PixelsSize, 2 * Player.PixelsSize, Player.PixelsSize),
        DOWN(4 * Player.PixelsSize, 0 * Player.PixelsSize, 2 * Player.PixelsSize, Player.PixelsSize),
        LEFT(2 * Player.PixelsSize, 0 * Player.PixelsSize, 2 * Player.PixelsSize, Player.PixelsSize),
        RIGHT(6 * Player.PixelsSize, 0 * Player.PixelsSize, 2 * Player.PixelsSize, Player.PixelsSize);

        private int x, y, width, height;

        Direction(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        protected Image getImgFromTexture(TextureHandler texture) {
            return texture.getImgPeace(x, y, width, height);
        }
    }

    private Direction direction;

    private Map<Direction, Sprite> DirectionSprites;
    private float speed;
    private TextureHandler texture;

    public Player(float x, float y, float scale, float speed, TextureHandler texture) {
        super(EntityTypes.Player, x, y);

        this.direction = Direction.UP;
        this.DirectionSprites = new HashMap<Direction, Sprite>();
        Player.scale = scale;
        this.speed = speed;
        this.texture = texture;
        for (Direction dir : Direction.values()) {
            Sprites sprites = new Sprites(dir.getImgFromTexture(texture), Player.SpritesPerDirection,
                    Player.PixelsSize);
            Sprite sprite = new Sprite(sprites, Player.scale);
            this.DirectionSprites.put(dir, sprite);
        }
        this.emitters.add(new ParticleEmitter(this.x, this.y, 2, Color.SADDLEBROWN, 0.25f, 2));
        this.emitters.add(new ParticleEmitter(this.x, this.y, 2, Color.SADDLEBROWN, 0.25f, 2));
    }

    public void shoot(Level level) {
        if (Time.getTime() - this.lastShootTime > Player.shootInterval) {
            Bullet bullet = new Bullet(this.x, this.y, 2f, 5f, this.texture, this.direction.ordinal(), this);
            level.addEntity(bullet);
            this.lastShootTime = Time.getTime();
            soundPlayer.playShot();
        }
    }

    @Override
    public void update(Input input, Level level) {
        float newX = this.x;
        float newY = this.y;
        if(this.checkActiveBooster(BoosterTypes.DEFENCE)){
            level.defenceEagle(true);
        }else{
            level.defenceEagle(false);
        }
        if(this.checkActiveBooster(BoosterTypes.LIFE)){
            this.lives++;
            this.removeBoosterType(BoosterTypes.LIFE);
        }
        Booster booster = level.getBooster(newX, newY, Player.PixelsSize * Player.scale);
        if (booster != null) {
            this.refreshBooster(new BoosterTime(booster.type, Time.getTime()));
            level.removeBooster(booster);
        }
        this.destroyExpiredBoosters();
        if(this.checkActiveBooster(BoosterTypes.SPEED)){
            this.speed = 3f;
        }else{
            this.speed = 2f;
        }
        if (level.checkCoordsForTileType(newX, newY, Player.PixelsSize * Player.scale, TileType.ice)) {
            if (this.direction == Direction.UP) {
                newY -= this.speed;
            } else if (this.direction == Direction.DOWN) {
                newY += this.speed;
            } else if (this.direction == Direction.LEFT) {
                newX -= this.speed;
            } else if (this.direction == Direction.RIGHT) {
                newX += this.speed;
            }
            this.x = newX;
            this.y = newY;
            return;
        }
        //System.out.println(KeyCode.UP.getCode());
        if (input.getItem(85)) {
            newY -= this.speed;
            this.direction = Direction.UP;
            this.DirectionSprites.get(this.direction).interval = 2;
        } else if (input.getItem(68)) {
            newY += this.speed;
            this.direction = Direction.DOWN;
            this.DirectionSprites.get(this.direction).interval = 2;
        } else if (input.getItem(76)) {
            newX -= this.speed;
            this.direction = Direction.LEFT;
            this.DirectionSprites.get(this.direction).interval = 2;
        } else if (input.getItem(82)) {
            newX += this.speed;
            this.direction = Direction.RIGHT;
            this.DirectionSprites.get(this.direction).interval = 2;
        } else{
            this.DirectionSprites.get(this.direction).interval = 0;
        }
        //System.out.println(level.getTank(newX, newY, Player.PixelsSize * Player.scale));
        List<Entity> tanks = level.getTank(newX, newY, Player.PixelsSize * Player.scale);
        for (Entity tank : tanks) {
            if (tank != this
                    && tank != null) {
                return;
            }
        }

        if (newX < 0) {
            newX = 0;
        } else if (newX >= Game.width - Player.PixelsSize * Player.scale) {
            newX = Game.width - Player.PixelsSize * Player.scale;
        }
        if (newY < 0) {
            newY = 0;
        } else if (newY >= Game.height - Player.PixelsSize * Player.scale) {
            newY = Game.height - Player.PixelsSize * Player.scale;
        }
        if (input.getItem(83)) {
            this.shoot(level);
        }
        if(this.x != newX || this.y != newY){
            this.updateEmitters();
        } else{
            for(ParticleEmitter emitter: this.emitters){
                emitter.clearParticles();
            }
        }
        if (level.checkCoordsForTileType(newX, newY, Player.PixelsSize * Player.scale, TileType.brick)
                || level.checkCoordsForTileType(newX, newY, Player.PixelsSize * Player.scale, TileType.metal)
                || level.checkCoordsForTileType(newX, newY, Player.PixelsSize * Player.scale, TileType.water)
                || level.checkCoordsForEagle(newX, newY, Player.PixelsSize * Player.scale)) {
            return;
        }




        this.x = newX;
        this.y = newY;

    }
    private void updateEmitters(){
        switch(this.direction){
            case RIGHT:{
                this.emitters.get(0).update(x+4, y+6);
                this.emitters.get(1).update(x+4, y+24);
                break;
            }
            case LEFT:{
                this.emitters.get(0).update(x+28, y+6);
                this.emitters.get(1).update(x+28, y+24);
                break;
            }
            case UP:{
                this.emitters.get(0).update(x+6, y+28);
                this.emitters.get(1).update(x+24, y+28);
                break;
            }
            case DOWN:{
                this.emitters.get(0).update(x+6, y+4);
                this.emitters.get(1).update(x+24, y+4);
            }
        }
    }

    private void removeBoosterType(BoosterTypes type) {
        Iterator iter = this.activeBoosters.iterator();
        while(iter.hasNext()){
            BoosterTime boost = (BoosterTime) iter.next();
            if(boost.boosterType == type){
                iter.remove();
            }
        }
    }

    public Boolean checkActiveBooster(BoosterTypes type){
        for(BoosterTime boost:
             this.activeBoosters) {
            if(boost.boosterType == type){
                return true;
            }
        }
        return false;
    }
    public void refreshBooster(BoosterTime boosterTime) {
        for (BoosterTime item : this.activeBoosters) {
			if(item.boosterType == boosterTime.boosterType){
                item.takeTime = Time.getTime();
                return;
            }
        }
        this.activeBoosters.add(boosterTime);
    }

    public void destroyExpiredBoosters(){
        Iterator iter = this.activeBoosters.iterator();
        while (iter.hasNext()) {
            BoosterTime boost = (BoosterTime) iter.next();
            if (Time.getTime() - boost.takeTime > this.boosterLifetime) {
                iter.remove();
            }
        }
    }

    @Override
    public void render(GraphicsContext g) {
        for(ParticleEmitter emitter: this.emitters){
            emitter.render(g);
        }
        this.DirectionSprites.get(this.direction).render(g, this.x, this.y);
    }

    @Override
    public void getShot(float x, float y, Level level) {
        level.addEntity(new Explosion(x, y, this.texture));
        if(this.lives == 1) {
            this.toDestroy = true;
            level.addEntity(new BigExplosion(this.x, this.y, texture));
        } else{
            this.lives--;
        }
    }

}
