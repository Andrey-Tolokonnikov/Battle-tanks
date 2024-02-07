package editor;

import javafx.scene.canvas.GraphicsContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import graphs.TextureHandler;
import Utils.Utils;

public class Level {
    public static final int TileSize = 8;
    public static final int TileScale = 2;
    public static TileType tileSelected = TileType.brick;
    private Map<TileType, Tile> tiles;
    private char[][] TileTable;
    private TextureHandler texture;

    public Level(String path, TextureHandler texture) {
        this.texture = new TextureHandler("texture.png");
        tiles = new HashMap<TileType, Tile>();
        tiles.put(TileType.brick,
                new Tile(texture.getImgPeace(32 * Level.TileSize, 0 * Level.TileSize, Level.TileSize, Level.TileSize),
                        Level.TileScale, TileType.brick));
        tiles.put(TileType.metal,
                new Tile(texture.getImgPeace(32 * Level.TileSize, 2 * Level.TileSize, Level.TileSize, Level.TileSize),
                        Level.TileScale, TileType.metal));
        tiles.put(TileType.water,
                new Tile(texture.getImgPeace(32 * Level.TileSize, 4 * Level.TileSize, Level.TileSize, Level.TileSize),
                        Level.TileScale, TileType.water));
        tiles.put(TileType.grass,
                new Tile(texture.getImgPeace(34 * Level.TileSize, 4 * Level.TileSize, Level.TileSize, Level.TileSize),
                        Level.TileScale, TileType.grass));
        tiles.put(TileType.ice,
                new Tile(texture.getImgPeace(36 * Level.TileSize, 4 * Level.TileSize, Level.TileSize, Level.TileSize),
                        Level.TileScale, TileType.ice));
        tiles.put(TileType.empty,
                new Tile(texture.getImgPeace(45 * Level.TileSize, 6 * Level.TileSize, Level.TileSize, Level.TileSize),
                        Level.TileScale, TileType.empty));
        tiles.put(TileType.player,
                new Tile(texture.getImgPeace(0 * Level.TileSize, 0 * Level.TileSize, 16, 16),
                        2, TileType.player));
        tiles.put(TileType.hard,
                new Tile(texture.getImgPeace(16 * Level.TileSize, 6 * Level.TileSize, 16, 16),
                        2, TileType.hard));
        tiles.put(TileType.fast,
                new Tile(texture.getImgPeace(16 * Level.TileSize, 16 * Level.TileSize, 16, 16),
                        2, TileType.fast));
        tiles.put(TileType.enemy,
                new Tile(texture.getImgPeace(0 * Level.TileSize, 16 * Level.TileSize, 16, 16),
                        2, TileType.enemy));
        tiles.put(TileType.eagle,
                new Tile(texture.getImgPeace(38 * Level.TileSize, 4 * Level.TileSize, 16, 16),
                        2, TileType.eagle));
        this.TileTable = Utils.parseLevel(path);

        Editor.canvas.setOnMouseDragged(e -> {
            int x = (int) e.getX() / 16;
            int y = (int) e.getY() / 16;
            if(x >= 0 && x <= 50 && y>=0 && y<= 36){
                this.update(x, y, Level.tileSelected);
            }
        });
        Editor.canvas.setOnMouseClicked(e -> {
            int x = (int) e.getX() / 16;
            int y = (int) e.getY() / 16;
            if(x >= 0 && x <= 50 && y>=0 && y<= 36){
                this.update(x, y, Level.tileSelected);
            }
        });
        Editor.scene.setOnKeyPressed(e -> {
                    if (e.getCode().ordinal() == 0) {
                        this.export("resources/level1.lvl");
                    }
                    ;
                    if(e.getCode().ordinal()==40){
                        Level.tileSelected = TileType.eagle;
                        return;
                    };
                    Level.tileSelected = TileType.getTypeFromNum(Integer.toString(e.getCode().ordinal() - 24).charAt(0));
                }
        );
    }


    public void export(String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            for (char[] integers : this.TileTable) {
                for (char integer : integers) {
                    bw.write(integer + " ");
                }
                bw.newLine();
            }
            int soundFlag = 0;
            if(Editor.checkbox.isSelected()){
                soundFlag = 1;
            }
            double musicVolume = Editor.slider.getValue();
            bw.write(soundFlag + " " + musicVolume);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(GraphicsContext g) {
        for (int i = 0; i < this.TileTable.length; i++) {
            for (int j = 0; j < this.TileTable[i].length; j++) {
                Tile tile = tiles.get(TileType.getTypeFromNum(this.TileTable[i][j]));
                if (this.TileTable[i][j] < '6') {
                    this.tiles.get(TileType.getTypeFromNum(this.TileTable[i][j])).render(g,
                            j * Level.TileSize * Level.TileScale, i * Level.TileSize * Level.TileScale);
                } else {
                    this.tiles.get(TileType.empty).render(g, j * Level.TileSize * Level.TileScale, i * Level.TileSize * Level.TileScale);
                }

            }
        }
        for (int i = 0; i < this.TileTable.length; i++) {
            for (int j = 0; j < this.TileTable[i].length; j++) {
                Tile tile = tiles.get(TileType.getTypeFromNum(this.TileTable[i][j]));
                if (this.TileTable[i][j] >= '6') {
                    this.tiles.get(TileType.getTypeFromNum(this.TileTable[i][j])).render(g,
                            j * Level.TileSize * Level.TileScale, i * Level.TileSize * Level.TileScale);
                }

            }
        }
    }

    public void update(int x, int y, TileType tyle) {
        this.TileTable[y][x] = tyle.getNum();
    }

    public void clear() {
        for(char[] arr : this.TileTable)
        Arrays.fill(arr, '0');
    }
}
