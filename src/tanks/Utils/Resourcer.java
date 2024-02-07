package Utils;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;


public class Resourcer {
	public static final String PATH = "resources/";
	public static Image loadImage(String name) {
		Image image = null;
			image = new Image(new File(Resourcer.PATH + name).toURI().toString());
		return image;
	};
}
