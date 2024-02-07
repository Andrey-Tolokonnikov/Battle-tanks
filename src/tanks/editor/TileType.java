package editor;

public enum TileType {
    empty('0'), brick('1'), metal('2'), grass('3'), water('4'), ice('5'), enemy('6'), hard('7'), fast('8'), player('9'), eagle('e');

    private char num;

    TileType(char num) {
        this.num = num;
    }

    public char getNum() {
        return this.num;
    }

    public static TileType getTypeFromNum(char num) {
        switch (num) {
            case '0':
                return TileType.empty;
            case '1':
                return TileType.brick;
            case '2':
                return TileType.metal;
            case '3':
                return TileType.grass;
            case '4':
                return TileType.water;
            case '5':
                return TileType.ice;
            case '6':
                return TileType.enemy;
            case '7':
                return TileType.hard;
            case '8':
                return TileType.fast;
            case '9':
                return TileType.player;
            case'e':
                return TileType.eagle;
            default:
                return empty;
        }
    }
}
