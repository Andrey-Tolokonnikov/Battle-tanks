package Updates;
import Game.Game;
import Game.State;
import Utils.Time;

public class MainUpdate implements UpdateCommand{
    @Override
    public void execute(Game game){
        if(Game.LogoPos.y >= 225){
            Game.LogoVelocity *= -0.6;
            if(Math.abs(Game.LogoVelocity)<=1){
                Game.LogoVelocity = 0;
                Game.LogoAcceleration = 0;
            }
        }

        Game.LogoVelocity += Game.LogoAcceleration;

        Game.LogoPos.y += Game.LogoVelocity;

        //Game.LogoPos.y = Math.min(Game.LogoPos.y, 225);
        if (game.input.getItem(69)) {
            if(game.input.getItem(83)) {
                System.out.println("in update");
                game.setState(State.EDIT);
            } else{
                game.setState(State.LEVEL_CHOISE);
            }
        }
    }
}
