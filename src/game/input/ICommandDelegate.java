package game.input;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.Command;

public interface ICommandDelegate {

	void action(Command command, GameContainer container);
}
