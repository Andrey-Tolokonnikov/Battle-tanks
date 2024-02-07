package level;

import java.util.ArrayList;

import Boosters.Booster;
import Boosters.BoosterTime;
import Boosters.BoosterTypes;
import Entities.*;
import graphs.Sprite;
import graphs.Sprites;
import javafx.scene.paint.Color;
import level.Point;
import Utils.SoundPlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Game.Game;
import IO.Input;
import Utils.Utils;
import Utils.Time;
import graphs.TextureHandler;
import javafx.scene.canvas.GraphicsContext;

public class Level {
    public static final int TileSize = 8;
    public static final int TileScale = 2;
    public static final int TilesHorizontalCount = Game.width / Level.TileSize * Level.TileScale;
    public static final int TilesVerticalCount = Game.height / Level.TileSize * Level.TileScale;

    private final long boostersInterval = 10 * Time.second;
    private long boosterSpawnMoment = Time.getTime();

    private Map<TileType, Tile> tiles;
    private char[][] TileTable;
    private List<Point> grassCoords;
    private List<Entity> entities = new ArrayList<Entity>();
    private List<Booster> boosters = new ArrayList<Booster>();
    public Eagle eagle = null;
    public TextureHandler texture;
    private Game game;

    static {

    }

    public Level(int levelNum, TextureHandler texture, Game game) {
        // this.TileTable = new
        // int[Level.TilesHorizontalCount][Level.TilesVerticalCount];
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
                new Tile(texture.getImgPeace(36 * Level.TileSize, 6 * Level.TileSize, Level.TileSize, Level.TileSize),
                        Level.TileScale, TileType.empty));

        this.TileTable = Utils.parseLevel("resources/level" + levelNum + ".lvl");
        if(Utils.parseSettings("resources/level" + levelNum + ".lvl")[0] == 0){
            SoundPlayer.setToPlaySounds(false);
        }else{
            SoundPlayer.setToPlaySounds(true);
        }
        SoundPlayer.setVolume( Utils.parseSettings("resources/level" + levelNum + ".lvl")[1]);


        this.grassCoords = new ArrayList<Point>();
        for (int i = 0; i < this.TileTable.length; i++) {
            for (int j = 0; j < this.TileTable[i].length; j++) {
                if (this.TileTable[i][j] >= '0' && this.TileTable[i][j] <= '5') {

                    Tile tile = tiles.get(TileType.getTypeFromNum(this.TileTable[i][j]));
                    if (tile.getType() == TileType.grass) {
                        this.grassCoords.add(
                                new Point(j * Level.TileSize * Level.TileScale, i * Level.TileSize * Level.TileScale));
                    }
                } else {
                    switch (this.TileTable[i][j]) {
                        case '6': {
                            this.entities.add(new EnemyTank(j * 16, i * 16, 2, 2, this.texture));
                            break;
                        }
                        case '7': {
                            this.entities.add(new HardTank(j * 16, i * 16, 2, 1.5f, this.texture));
                            break;
                        }
                        case '8': {
                            this.entities.add(new FastTank(j * 16, i * 16, 2, 2.5f, this.texture));
                            break;
                        }
                        case '9': {
                            this.entities.add(new Player(j * 16, i * 16, 2, 2, this.texture));
                        }
                        case 'e': {
                            this.eagle = new Eagle(j * 16, i * 16, texture);
                        }
                    }
                }
            }
        }
        this.game = game;
    }

    public void update() {
        if (Time.getTime() > this.boosterSpawnMoment + this.boostersInterval) {
            int x = Game.rand.nextInt(Game.width - 16);
            int y = Game.rand.nextInt(Game.height - 16);
            BoosterTypes type = BoosterTypes.getFromNum(Game.rand.nextInt(BoosterTypes.values().length));
            this.boosters.add(new Booster(type, this.texture, x, y));
            this.boosterSpawnMoment = Time.getTime();
        }
        if(this.entities.size() == 1 && this.entities.get(0).type == EntityTypes.Player ){
            this.game.win();
        }
    }

    public void defenceEagle(Boolean toDefence) {
        int EagleX = (int) (this.eagle.x / 16);
        int EagleY = (int) (this.eagle.y / 16);
        Point[] points = {new Point(EagleX - 1, EagleY + 1),
                new Point(EagleX - 1, EagleY),
                new Point(EagleX - 1, EagleY - 1),
                new Point(EagleX, EagleY - 1),
                new Point(EagleX + 1, EagleY - 1),
                new Point(EagleX + 2, EagleY - 1),
                new Point(EagleX + 2, EagleY),
                new Point(EagleX + 2, EagleY + 1)};
        for(Point point: points){
            if(toDefence){
                this.TileTable[(int)point.y][(int)point.x] = '2';
            } else if (!toDefence && this.TileTable[(int)point.y][(int)point.x] == '2'){
                this.TileTable[(int)point.y][(int)point.x] = '1';
            }
        }
    }

    public List<Entity> getTank(float x, float y, float size) {
        List<Entity> result = new ArrayList<Entity>();
        for (Entity ent : this.entities) {
            if ((ent.type == EntityTypes.EnemyTank || ent.type == EntityTypes.Player)
                    && Math.abs(ent.x - x) < EnemyTank.PixelsSize * EnemyTank.scale
                    && Math.abs(ent.y - y) < EnemyTank.PixelsSize * EnemyTank.scale) {
                result.add(ent);
            }
        }
        return result;
    }

    public void destroyTile(float x, float y) {
        int xTile = (int) Math.ceil(x / (Level.TileSize * Level.TileScale));
        int yTile = (int) Math.ceil(y / (Level.TileSize * Level.TileScale));
        this.TileTable[yTile][xTile] = 0;

    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void updateEntities(Input input) {
        // List<Entity> tmp = new ArrayList<>();
        // tmp.addAll(this.entities);
        for (int i = 0; i < this.entities.size(); i++) {
            this.entities.get(i).update(input, this);
        }
        Iterator iter = this.entities.iterator();
        while (iter.hasNext()) {
            Entity ent = (Entity) iter.next();
            if (ent.toStopGame) {
                game.loose();
            }
            if (ent.getToDestroy()) {
                iter.remove();
            }
        }
        for (int i = 0; i < this.boosters.size(); i++) {
            this.boosters.get(i).update(game.level);
        }
        iter = this.boosters.iterator();
        while (iter.hasNext()) {
            Booster boost = (Booster) iter.next();
            if (boost.getToDestroy()) {
                iter.remove();
            }
        }
        // System.out.println(this.entities.toString());

//		for(Entity entity: tmp) {
//			entity.update(input, this);
//		}
        // System.out.println(this.getTank(500, 500, 32));
    }

    public void renderEntites(GraphicsContext g) {
        this.eagle.render(g);
        for (Entity entity : this.entities) {
            entity.render(g);
        }
        for (Booster booster : this.boosters) {
            booster.render(g);
        }

    }

    public void render(GraphicsContext g) {
        for (int i = 0; i < this.TileTable.length; i++) {
            for (int j = 0; j < this.TileTable[i].length; j++) {
                Tile tile = tiles.get(TileType.getTypeFromNum(this.TileTable[i][j]));
                if (tile.getType() != TileType.grass) {
                    this.tiles.get(TileType.getTypeFromNum(this.TileTable[i][j])).render(g,
                            j * Level.TileSize * Level.TileScale, i * Level.TileSize * Level.TileScale);
                }

            }
        }
        this.renderLives(g);
        this.renderBoosters(g);
    }
    private void renderLives(GraphicsContext g){
        Sprites sprites = new Sprites(texture.getImgPeace(0*16,0*16, 16, 16), 1, 16);
        Sprite sprite = new Sprite(sprites, 2.5f);

        sprite.render(g, 20, 20);
        g.setStroke(Color.WHITE);
        g.setFill(Color.WHITE);
        g.strokeRect(17, 17, 100, 46);
        g.fillText("X"+ Integer.toString(this.getPlayer()!=null?this.getPlayer().lives:0), 59, 57);
    }
    private void renderBoosters(GraphicsContext g){
        if(this.getPlayer()!=null && this.getPlayer().activeBoosters.size()>0) {
            for (int i = 0; i <  this.getPlayer().activeBoosters.size(); i++) {
                BoosterTime boost = this.getPlayer().activeBoosters.get(i);
                Sprites sprites = null;
                Sprite sprite = null;
                switch (boost.boosterType) {
                    case SPEED: {
                        sprites = new Sprites(texture.getImgPeace(17 * 16, 7 * 16, 16, 16), 1, 16);
                        sprite = new Sprite(sprites, 1.5f);
                        sprite.render(g, 20 + 25 * i, 560);
                        break;
                    }
                    case DEFENCE: {
                        sprites = new Sprites(texture.getImgPeace(16 * 16, 7 * 16, 16, 16), 1, 16);
                        sprite = new Sprite(sprites, 1.5f);
                        sprite.render(g, 20 + 25 * i, 560);
                        break;
                    }
                    case SHOVEL: {
                        sprites = new Sprites(texture.getImgPeace(18 * 16, 7 * 16, 16, 16), 1, 16);
                        sprite = new Sprite(sprites, 1.5f);
                        sprite.render(g, 20 + 25 * i, 560);
                        break;
                    }
                    default: {
                    }
                }
            }
        }
    }
    public Player getPlayer(){
        for(Entity ent: this.entities){
            if(ent.type == EntityTypes.Player){
                return (Player)ent;
            }
        }
        return null;
    }

    public TileType getTileTypeFromCoords(float newX, float newY) {
        // System.out.println(newX);
        int xTile = (int) Math.ceil(newX / (Level.TileSize * Level.TileScale));
        int yTile = (int) Math.ceil(newY / (Level.TileSize * Level.TileScale));
        // System.out.println(TileType.getTypeFromNum(this.TileTable[yTile][xTile]));
        // System.out.println(xTile + " " + yTile);
        if (xTile < 0 || xTile >= 50 || yTile < 0 || yTile >= 36) {
            return TileType.empty;
        }
        return TileType.getTypeFromNum(this.TileTable[yTile][xTile]);
        // System.out.println(xTile);
        // return null;

    }

    public boolean checkCoordsForTileType(float x, float y, float pixelsSize, TileType tileType) {
        // System.out.println(TileType.getTypeFromNum(this.TileTable[(int)(int)
        // Math.ceil(y/(Level.TileSize*Level.TileScale))][(int)
        // Math.ceil(x/(Level.TileSize*Level.TileScale))]));
        return ((this.getTileTypeFromCoords(x + pixelsSize / 2 - 1, y + pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x + pixelsSize / 2 - 1, y - pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x - pixelsSize / 2 + 1, y + pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x - pixelsSize / 2 + 1, y - pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x + pixelsSize / 2 - 1, y) == tileType)
                || (this.getTileTypeFromCoords(x + pixelsSize / 2 - 1, y) == tileType)
                || (this.getTileTypeFromCoords(x - pixelsSize / 2 + 1, y) == tileType)
                || (this.getTileTypeFromCoords(x - pixelsSize / 2 + 1, y) == tileType) ||

                (this.getTileTypeFromCoords(x + pixelsSize / 2 - 1, y + pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x - pixelsSize / 2 + 1, y + pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x + pixelsSize / 2 - 1, y - pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x - pixelsSize / 2 + 1, y - pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x, y + pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x, y + pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x, y - pixelsSize / 2) == tileType)
                || (this.getTileTypeFromCoords(x, y - pixelsSize / 2) == tileType));
        // (this.getTileTypeFromCoords(x-pixelsSize, y) == tileType) ||
        // (this.getTileTypeFromCoords(x, y+pixelsSize) == tileType) ||
        // TileType tmp = this.getTileTypeFromCoords(x, y-pixelsSize);
    }

    public void renderGrass(GraphicsContext g) {
        for (Point p : this.grassCoords) {
            this.tiles.get(TileType.grass).render(g, (int)p.x, (int)p.y);
        }
    }

    public void removeBooster(Booster booster) {
        this.boosters.remove(booster);
    }

    public Booster getBooster(float newX, float newY, float size) {
        for (Booster booster :
                this.boosters) {
            if ((newX - size / 2 < booster.x + 16) &&
                    (newX + size / 2 > booster.x - 16) &&
                    (newY - size / 2 < booster.y + 16) &&
                    (newY + size / 2 > booster.y - 16)) {
                return booster;
            }
        }
        return null;
    }

    public boolean checkCoordsForEagle(float newX, float newY, float playerSize) {
        return newX - this.eagle.x < playerSize &&
                newX > this.eagle.x - playerSize &&
                newY > this.eagle.y - playerSize &&
                newY - this.eagle.y < playerSize;
    }
}
