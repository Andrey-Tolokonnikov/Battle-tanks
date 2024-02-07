package Updates;

import Game.Game;
import Utils.Time;
import javafx.scene.input.KeyCode;

public class GameUpdate implements UpdateCommand{
    @Override
    public void execute(Game game){
        if (game.input.getItem(KeyCode.P.getCode()) && Game.pause && Time.getTime() - game.pauseMoment > Game.pauseInterval) {
            game.resume();
            Game.pauseMoment = Time.getTime();
        } else if (game.input.getItem(KeyCode.P.getCode()) && !Game.pause
                && Time.getTime() - game.pauseMoment > Game.pauseInterval) {
            game.pause();
            Game.pauseMoment = Time.getTime();
        }
        if (Game.pause) {
            return;
        }
        game.level.updateEntities(game.input);
        game.level.update();
    }
}
