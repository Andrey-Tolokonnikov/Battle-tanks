package level;

public enum TileType {
	empty(0), brick(1), metal(2), grass(3), water(4), ice(5);

	private int num;

	TileType(int num) {
		this.num = num;
	}

	public int getNum() {
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
		default:
			return empty;
		}
	}
}
