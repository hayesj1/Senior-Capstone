package game.input;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.command.*;

import java.util.HashSet;

public class DemoInputHandler implements InputProviderListener {
	static final BasicCommand attack1 = new BasicCommand("Attack_1");
	static final BasicCommand attack2 = new BasicCommand("Attack_2");
	static final BasicCommand attack3 = new BasicCommand("Attack_3");
	static final BasicCommand attack4 = new BasicCommand("Attack_4");
	static final BasicCommand attack5 = new BasicCommand("Attack_5");
	static final BasicCommand attack6 = new BasicCommand("Attack_6");
	static final BasicCommand attack = new BasicCommand("Attack");

	static final BasicCommand selectTarget = new BasicCommand("Select Target");
	static final BasicCommand runAway = new BasicCommand("Escape");

	static final BasicCommand forward = new BasicCommand("Forward");
	static final BasicCommand backward = new BasicCommand("backward");
	static final BasicCommand up = new BasicCommand("Up");
	static final BasicCommand down = new BasicCommand("Down");
	static final BasicCommand interact = new BasicCommand("Interact");

	static final BasicCommand quit = new BasicCommand("Quit");

	private HashSet<ICommandDelegate> commandDelegates;
	private GameContainer container;

	public DemoInputHandler(GameContainer container) {
		this.container = container;
		this.commandDelegates = new HashSet<>();
	}

	public void addCommandDelegate(ICommandDelegate del) {
		this.commandDelegates.add(del);
	}
	public void removeCommandDelegate(ICommandDelegate del) {
		this.commandDelegates.remove(del);
	}

	/**
	 * Register the Demo's command with <code>provider.bindCommand(Control, Command)</code>
	 * @param provider The Input povider to bind commands with
	 */
	public void registerCommands(InputProvider provider) {
		provider.bindCommand(new MouseButtonControl(Input.MOUSE_LEFT_BUTTON), attack);
		provider.bindCommand(new KeyControl(Input.KEY_1), attack1);
		provider.bindCommand(new KeyControl(Input.KEY_2), attack2);
		provider.bindCommand(new KeyControl(Input.KEY_3), attack3);
		provider.bindCommand(new KeyControl(Input.KEY_4), attack4);
		provider.bindCommand(new KeyControl(Input.KEY_5), attack5);
		provider.bindCommand(new KeyControl(Input.KEY_6), attack6);
		provider.bindCommand(new KeyControl(Input.KEY_NUMPAD1), attack1);
		provider.bindCommand(new KeyControl(Input.KEY_NUMPAD2), attack2);
		provider.bindCommand(new KeyControl(Input.KEY_NUMPAD3), attack3);
		provider.bindCommand(new KeyControl(Input.KEY_NUMPAD4), attack4);
		provider.bindCommand(new KeyControl(Input.KEY_NUMPAD5), attack5);
		provider.bindCommand(new KeyControl(Input.KEY_NUMPAD6), attack6);

		//provider.bindCommand(new MouseButtonControl(Input.MOUSE_LEFT_BUTTON), selectTarget);
		provider.bindCommand(new KeyControl(Input.KEY_R), runAway);

		provider.bindCommand(new KeyControl(Input.KEY_D), forward);
		provider.bindCommand(new KeyControl(Input.KEY_A), backward);
		provider.bindCommand(new KeyControl(Input.KEY_W), up);
		provider.bindCommand(new KeyControl(Input.KEY_S), down);
		provider.bindCommand(new KeyControl(Input.KEY_RIGHT), forward);
		provider.bindCommand(new KeyControl(Input.KEY_LEFT), backward);
		provider.bindCommand(new KeyControl(Input.KEY_UP), up);
		provider.bindCommand(new KeyControl(Input.KEY_DOWN), down);

		provider.bindCommand(new KeyControl(Input.KEY_SPACE), interact);

		provider.bindCommand(new KeyControl(Input.KEY_ESCAPE), quit);
	}

	@Override
	public void controlPressed(Command command) {
		//System.out.println(command.toString()+" pressed!");
	}

	/**
	 * Forwards all commands to the delegates for handling, excluding the QUIT command, which is processed directly.
	 */
	@Override
	public void controlReleased(Command command) {
		//System.out.println(command+" released!");
		if (command == quit) {
			container.exit();
		}
		commandDelegates.forEach(del -> del.action(command, container));
	}
}
