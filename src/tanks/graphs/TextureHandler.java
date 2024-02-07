package graphs;

import Utils.Resourcer;
import Utils.Utils;
import javafx.scene.image.Image;

public class TextureHandler {
	public Image img;
	
	public TextureHandler(String name) {
		this.img = Resourcer.loadImage(name);
	}
	public Image getImgPeace(int x, int y, int width, int height) {
		return Utils.getSubImage(this.img, x, y, width, height);
	}
}
