package IO;

//import java.awt.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;


public class Input {
	private boolean[] map;

	public Input() {
		map = new boolean[256];
		for (int i = 0; i < map.length; i++) {
			map[i] = false;
		}
	}
	public void onKeyDown(KeyEvent event){
		int keyCode = (int) event.getCode().toString().charAt(0);
		map[keyCode] = true;
	}
	public void onKeyUp(KeyEvent event){
		int keyCode = (int) event.getCode().toString().charAt(0);
		map[keyCode] = false;
	}

	public boolean[] getMap() {
		return Arrays.copyOf(this.map, this.map.length);
	}

	public boolean getItem(int key) {
		return this.map[key];
	}

	public void clear() {
		Arrays.fill(this.map, false);
	}
}
