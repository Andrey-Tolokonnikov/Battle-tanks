package graphs;

import javafx.scene.image.Image;
import Utils.Utils;

public class Sprites {
	private Image img;
	public int spriteCount;
	private int pixelsSize;
	private int spritesHorizontalCount;
	
	public Sprites(Image img, int spriteCount, int pixelsSize) {
		this.img = img;
		this.spriteCount = spriteCount;
		this.pixelsSize = pixelsSize;
		
		this.spritesHorizontalCount = ((int)this.img.getWidth()) / this.pixelsSize;
	}
	
	public Image getSprite(int index) {
		index = index % spriteCount;
		
		int x = index % this.spritesHorizontalCount * this.pixelsSize;
		
		int y = index / this.spritesHorizontalCount * this.pixelsSize;
		
		return Utils.getSubImage(this.img,x, y, pixelsSize, pixelsSize);
		
	}
}
