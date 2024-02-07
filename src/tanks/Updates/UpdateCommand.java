package Updates;
import Game.Game;
public interface UpdateCommand {
    public void execute(Game game);
}
