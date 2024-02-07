package Entities;

import java.util.*;

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
import javafx.scene.paint.Color;
import level.Level;
import level.TileType;

public class EnemyTank extends Entity {
	public static final int PixelsSize = 16;
	public static final int SpritesPerDirection = 2;
	public static float scale;
	public  long shootInterval = Time.second;
	private long lastShootTime = Time.getTime();
	private SoundPlayer soundPlayer = new SoundPlayer();
	private List<ParticleEmitter> emitters = new ArrayList<ParticleEmitter>();

	public enum Direction {
		UP(0 * EnemyTank.PixelsSize, 8 * EnemyTank.PixelsSize, 2 * EnemyTank.PixelsSize, EnemyTank.PixelsSize),
		DOWN(4 * EnemyTank.PixelsSize, 8 * EnemyTank.PixelsSize, 2 * EnemyTank.PixelsSize, EnemyTank.PixelsSize),
		LEFT(2 * EnemyTank.PixelsSize, 8 * EnemyTank.PixelsSize, 2 * EnemyTank.PixelsSize, EnemyTank.PixelsSize),
		RIGHT(6 * EnemyTank.PixelsSize, 8 * EnemyTank.PixelsSize, 2 * EnemyTank.PixelsSize, EnemyTank.PixelsSize);

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

	public EnemyTank(float x, float y, float scale, float speed, TextureHandler texture) {
		super(EntityTypes.EnemyTank, x, y);

		this.direction = Direction.DOWN;
		this.DirectionSprites = new HashMap<Direction, Sprite>();
		EnemyTank.scale = scale;
		this.speed = speed;
		this.texture = texture;
		for (Direction dir : Direction.values()) {
			Sprites sprites = new Sprites(dir.getImgFromTexture(texture), Player.SpritesPerDirection,
					Player.PixelsSize);
			Sprite sprite = new Sprite(sprites, EnemyTank.scale);
			this.DirectionSprites.put(dir, sprite);
		}
		this.emitters.add(new ParticleEmitter(this.x, this.y, 2, Color.SADDLEBROWN, 0.25f, 2));
		this.emitters.add(new ParticleEmitter(this.x, this.y, 2, Color.SADDLEBROWN, 0.25f, 2));

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

	public void shoot(Level level) {
		if (Time.getTime() - this.lastShootTime > this.shootInterval) {
			Bullet bullet = new Bullet(this.x, this.y, 2f, 5f, this.texture, this.direction.ordinal(), this);
			level.addEntity(bullet);
			this.lastShootTime = Time.getTime();
			this.shootInterval = (long) (Time.second + Game.rand.nextFloat()*0.5*Time.second);
			soundPlayer.playShot();
		}

	}

	@Override
	public void update(Input input, Level level) {
		float newX = this.x;
		float newY = this.y;
		
		int rand  = (int)(Game.rand.nextFloat() * 70);
		
		if(rand == 5) {
			this.rotate(this.direction);
		} else {
			if(this.direction == Direction.UP) {
				newY -= this.speed;
			}else if(this.direction == Direction.DOWN) {
				newY += this.speed;
			}else if(this.direction == Direction.LEFT) {
				newX -= this.speed;
			}else if(this.direction == Direction.RIGHT) {
				newX += this.speed;
			}
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
				|| level.checkCoordsForTileType(newX, newY, Player.PixelsSize * Player.scale, TileType.water)) {
			newX = this.x;
			newY = this.y;
		this.rotate(this.direction);
		}
		List<Entity> tanks = level.getTank(newX, newY, Player.PixelsSize * Player.scale);
		for (Entity tank : tanks) {
			if (tank != this && tank != null) {
				newX = x;
				newY = y;
				this.rotate(this.direction);
				return;
			}
		}
		
		if(newX < 0 || newY < 0 || newX >= Game.width - Player.PixelsSize * Player.scale || newY >= Game.height - Player.PixelsSize * Player.scale) {
			newX = this.x;
			newY = this.y;
			this.rotate(this.direction);
		}
		//System.out.println(rand);
		
		this.shoot(level);
		this.x = newX;
		this.y = newY;
	}
	private void rotate(Direction dir) {
		int rand = (int)(Game.rand.nextFloat()*4);
		if(rand == 0 && dir != Direction.RIGHT) {
			this.direction = Direction.RIGHT;
		} else if(rand == 1 && dir != Direction.LEFT){
			this.direction = Direction.LEFT;
		} else if(rand == 2 && dir != Direction.UP) {
			this.direction = Direction.UP;
		}else if(rand == 3 && dir != Direction.DOWN) {
			this.direction = Direction.DOWN;
		}
		//System.out.println(rand);
	}

	@Override
	public void render(GraphicsContext g) {
		for(ParticleEmitter emitter: this.emitters){
			emitter.render(g);
		}
		this.DirectionSprites.get(this.direction).render(g, this.x, this.y)
		;
	}

	@Override
	public void getShot(float x, float y, Level level) {
		level.addEntity(new Explosion(x, y, this.texture));
		this.toDestroy = true;
	}

}
