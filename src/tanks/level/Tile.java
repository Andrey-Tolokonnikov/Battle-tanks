package level;

import Utils.Utils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {
	private Image img;
	private TileType type;
	private int resizeWidth;
	private int resizeHeight;
	
	protected Tile(Image img, int scale, TileType type) {
		this.type = type;
		this.resizeWidth = (int)img.getWidth()*scale;
		this.resizeHeight = (int)img.getHeight()*scale;
		this.img = img;
	}
	protected TileType getType() {
		return this.type;
	}
	protected void render(GraphicsContext g, int x, int y) {
		g.drawImage(img, x, y, this.resizeWidth, this.resizeHeight);
	}
}
