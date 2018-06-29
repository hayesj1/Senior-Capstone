package game.input;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.Command;

public interface ICommandDelegate {

	/**
	 * Execute the action
	 * @param command The command that was triggered
	 * @param container The GUIContext
	 */
	void action(Command command, GameContainer container);
}
