package Updates;
import Game.Game;
import Utils.SoundPlayer;
import Utils.Time;
import javafx.scene.input.KeyCode;
import Game.State;

public class LevelChoiseUpdate implements UpdateCommand{
    @Override
    public void execute(Game game){
        if(Time.getTime() - Game.pressMoment  <= Game.pressInterval){
            return;
        }
        SoundPlayer soundPlayer = new SoundPlayer();
        if(game.input.getItem(82)){
            Game.currentLevelNumber ++;
            Game.pressMoment = Time.getTime();
            soundPlayer.playChoise();
        } else if(game.input.getItem(76)){
            Game.currentLevelNumber --;
            Game.pressMoment = Time.getTime();
           soundPlayer.playChoise();
        } else if(game.input.getItem(68)){
            Game.currentLevelNumber +=4;
            Game.pressMoment = Time.getTime();
            soundPlayer.playChoise();
        } else if(game.input.getItem(85)){
            Game.currentLevelNumber -=4;
            Game.pressMoment = Time.getTime();
            soundPlayer.playChoise();
        }

        if(Game.currentLevelNumber <= 0){
            Game.currentLevelNumber = Game.levelsCount +  Game.currentLevelNumber;
        }if(Game.currentLevelNumber > 12){
            Game.currentLevelNumber = Game.currentLevelNumber - Game.levelsCount;
        }
        if(game.input.getItem(69)){
            if(Time.getTime() - Game.screenChangedMoment > 0.5*Time.second) {
                game.changeLevel();
                game.setState(State.GAME);
            }
        }
    }
}
