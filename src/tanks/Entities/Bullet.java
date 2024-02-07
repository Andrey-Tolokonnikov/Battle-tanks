package Entities;

//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Boosters.BoosterTypes;
import Game.Game;

import IO.Input;
import graphs.Sprite;
import graphs.Sprites;
import graphs.TextureHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import level.Level;
import level.TileType;

public class Bullet extends Entity {
	public static int PixelsSize = 8;
	
	
	
	private Map<Direction, Sprite> DirectionSprites = new HashMap<Direction, Sprite>();
	private float x;
	private float y;
	private static float scale;
	private float speed;
	private int directionNum;
	private Direction direction;
	private TextureHandler texture;
	private Entity owner;
	//public boolean toDestroy = false;
	
	public enum Direction {
		UP(40 * Bullet.PixelsSize, 12 * Bullet.PixelsSize+2, 1 * Bullet.PixelsSize, Bullet.PixelsSize),
		DOWN(42 * Bullet.PixelsSize, 12 * Bullet.PixelsSize+2, 1 * Bullet.PixelsSize, Bullet.PixelsSize),
		LEFT(41 * Bullet.PixelsSize, 12 * Bullet.PixelsSize+2, 1 * Bullet.PixelsSize, Bullet.PixelsSize),
		RIGHT(43 * Bullet.PixelsSize, 12 * Bullet.PixelsSize+2, 1 * Bullet.PixelsSize, Bullet.PixelsSize);

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

	public Bullet(float x, float y, float scale, float speed, TextureHandler texture, int direction, Entity owner) {
		super(EntityTypes.Bullet, x, y);
		this.x = x+5;
		this.y = y+3;
		this.scale = scale;
		this.speed = speed;
		this.directionNum = direction;
		this.texture = texture;
		this.owner = owner;
		if (direction == 0) {
			this.direction = Direction.UP;
		} else if (direction == 1) {
			this.direction = Direction.DOWN;
		} else if (direction == 2) {
			this.direction = Direction.LEFT;
		} else if (direction == 3) {
			this.direction = Direction.RIGHT;
		};

		for (Direction dir : Direction.values()) {
			Sprites sprites = new Sprites(dir.getImgFromTexture(texture), 1,
					Bullet.PixelsSize);
			Sprite sprite = new Sprite(sprites, Bullet.scale);
			this.DirectionSprites.put(dir, sprite);
		}
	}

	@Override
	public void render(GraphicsContext g) {
		this.DirectionSprites.get(this.direction).render(g, this.x, this.y);
	}

	@Override
	public void update(Input input, Level level) {
		float newX = this.x-6;
		float newY = this.y-3;
		if(this.direction == Direction.UP) {
			newY -= this.speed;
		}
		if(this.direction == Direction.DOWN) {
			newY += this.speed;
		}
		if(this.direction == Direction.LEFT) {
			newX -= this.speed;
		}
		if(this.direction == Direction.RIGHT) {
			newX += this.speed;
		}
		
		
		
		if (newX < 0 || newX >= Game.width || newY < 0 || newY >= Game.height) {	
			this.toDestroy = true;
			return;
		} 
		if(level.getTileTypeFromCoords(newX, newY-3) == TileType.brick ||
				level.getTileTypeFromCoords(newX, newY) == TileType.metal) {
			if(level.getTileTypeFromCoords(newX, newY-3) == TileType.brick ||(level.getPlayer() != null && level.getPlayer().checkActiveBooster(BoosterTypes.SHOVEL))) {
				level.destroyTile(newX, newY-3);
				level.addEntity(new Explosion(newX, newY-3, this.texture));
			}
			if(level.getTileTypeFromCoords(newX, newY-3) == TileType.metal) {
				level.addEntity(new Explosion(newX, newY-3, this.texture));
			}
			this.toDestroy = true;
		}
		
		List<Entity> tanks = level.getTank(newX, newY, EnemyTank.PixelsSize*EnemyTank.scale);
		
		//if(tank != null && tank.getClass() != this.owner.getClass() ) {
		for(Entity tank: tanks) {
		if(tank != null && this.owner != tank && (this.owner.getClass() == Player.class || (this.owner.getClass() != Player.class && tank.getClass() == Player.class))) {
			tank.getShot(newX, newY, level);
			this.toDestroy = true;
		}}
		if(level.checkCoordsForEagle(newX, newY, 16)){
			this.toDestroy = true;
			level.eagle.getShot(newX, newY, level);
		}
		this.x = newX+6;
		this.y = newY+3;
		
	}

	@Override
	public void getShot(float x, float y, Level level) {
		// TODO Auto-generated method stub
		
	}
}
