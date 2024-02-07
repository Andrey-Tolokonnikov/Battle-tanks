package Updates;

import Game.Game;
import Utils.SoundPlayer;
import Utils.Time;
import Game.State;
public class LevelWonUpdate implements UpdateCommand{

    @Override
    public void execute(Game game) {
        if(Game.LogoPos.y >= 270){
            Game.LogoVelocity *= -0.6;
            if(Math.abs(Game.LogoVelocity)<=1){
                Game.LogoVelocity = 0;
                Game.LogoAcceleration = 0;
            }
        }

        Game.LogoVelocity += Game.LogoAcceleration;

        Game.LogoPos.y += Game.LogoVelocity;
        if(Time.getTime() - Game.WinMoment > Game.ShowWinDuration){
            Game.setState(State.LEVEL_CHOISE);
            SoundPlayer.resumeMusic();
        }
    }
}
