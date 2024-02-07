package Boosters;

import graphs.TextureHandler;

public enum BoosterTypes {
    SPEED, DEFENCE, LIFE, SHOVEL;

    public static BoosterTypes getFromNum(int i) {
        switch (i){
            case 0:
                return BoosterTypes.SPEED;
            case 1:
                return BoosterTypes.DEFENCE;
            case 2:
                return BoosterTypes.LIFE;
            case 3:
                return BoosterTypes.SHOVEL;
            default:
                return BoosterTypes.SPEED;
        }
    }
}
