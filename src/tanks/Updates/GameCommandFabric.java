package Updates;

import Game.State;

public class GameCommandFabric {
    public static UpdateCommand getUpdate(State state){
        switch(state){
            case WELCOME_PAGE:
                return new MainUpdate();
            case GAME:
                return new GameUpdate();
            case LEVEL_CHOISE:
                return new LevelChoiseUpdate();
            case LEVEL_WON:
                return new LevelWonUpdate();
            case LEVEL_LOOSE:
                return new LevelLooseUpdate();
        }
        return null;
    }
}
