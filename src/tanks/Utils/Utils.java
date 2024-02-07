package Utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

	public static char[][] parseLevel(String filePath) {
		char[][] result = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
			String line = null;
			List<char[]> levelLines = new ArrayList<char[]>();
			while ((line = reader.readLine()) != null) {
				// String[] codes = line.split(" ");
				levelLines.add(Utils.strToInt(line.split(" ")));
			}
			result = new char[levelLines.size()][levelLines.get(0).length];
			for (int i = 0; i < levelLines.size(); i++) {
				result[i] = levelLines.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static float[] parseSettings(String path){
		float[] result = {0, 0};
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
			String line = null;
			List<char[]> levelLines = new ArrayList<char[]>();
			int count = 0;
			while ((line = reader.readLine()) != null) {
				// String[] codes = line.split(" ");

				if(count == 37){
					result[0] = Integer.parseInt(line.split(" ")[0]);
					result[1] = Float.parseFloat(line.split(" ")[1]);
					break;
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static final char[] strToInt(String[] arr) {
		char[] newArr = new char[arr.length];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i].charAt(0);
		}
		return newArr;
	}

	public static Image getSubImage(Image img, int x, int y, int w, int h) {
		PixelReader reader = img.getPixelReader();
		WritableImage image = new WritableImage(reader, x, y, w, h);
		return image;
	}
}
