package graphs;

import Utils.Utils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	private Sprites sprites;
	private float size;
	private int resizeWidth;
	private int resizeHeight;
	private Image img;
	private int  AnimationMaxCount = 10;
	private int AnimationCount = 0;
	private int frameNum = 0;
	public int interval;

	public Sprite(Sprites sprites, float size, int duration, int interval) {
		this.sprites = sprites;
		this.size = size;
		this.img = sprites.getSprite(0);
		this.resizeWidth = (int)(img.getWidth() * this.size);
		this.resizeHeight = (int)(img.getHeight() * this.size);
		this.AnimationMaxCount = duration;
		this.interval = interval;
	}
	public Sprite(Sprites sprites, float size) {
		this.sprites = sprites;
		this.size = size;
		this.img = sprites.getSprite(0);
		this.resizeWidth = (int)(img.getWidth() * this.size);
		this.resizeHeight = (int)(img.getHeight() * this.size);
		this.AnimationMaxCount = 10;
		this.interval = 2;
	}

	public void render(GraphicsContext g, float x, float y) {
		this.AnimationCount+=this.interval;

		if(this.AnimationCount == this.AnimationMaxCount) {
			this.frameNum++;
			this.frameNum %= sprites.spriteCount;
			this.AnimationCount = 0;
			
		}
		this.img = sprites.getSprite(this.frameNum);
		g.drawImage(this.img, x, y, this.resizeWidth, this.resizeHeight);
	}
}
