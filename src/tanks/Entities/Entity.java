package Entities;


import IO.Input;
import javafx.scene.canvas.GraphicsContext;
import level.Level;

public abstract class Entity {
	public final EntityTypes type;
	public float x;
	public float y;
	public boolean toDestroy;
	public boolean toStopGame;
	
	protected Entity(EntityTypes type, float x, float y) {
		this.x = x;
		this.type = type;
		this.y = y;
	}
	public abstract void update(Input input, Level level);
	public abstract void render(GraphicsContext g);
	public abstract void getShot(float x, float y, Level level);
	
	public boolean getToDestroy(){
		return this.toDestroy;
	}
}
