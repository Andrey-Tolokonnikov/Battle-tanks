package Updates;

import Game.Game;
import Game.State;
import Utils.SoundPlayer;
import Utils.Time;

public class LevelLooseUpdate implements UpdateCommand{

    @Override
    public void execute(Game game) {
        Game.LogoOpacity += 0.01;
        Game.LogoOpacity = Math.min(Game.LogoOpacity, 1);
        if(Time.getTime() - Game.WinMoment > Game.ShowWinDuration){
            Game.setState(State.LEVEL_CHOISE);
            SoundPlayer.resumeMusic();
        }
    }
}
