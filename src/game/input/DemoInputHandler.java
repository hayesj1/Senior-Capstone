package game.input;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.command.*;

import java.util.HashSet;

public class DemoInputHandler implements InputProviderListener {
	public static final BasicCommand attack1 = new BasicCommand("Attack_1");
	public static final BasicCommand attack2 = new BasicCommand("Attack_2");
	public static final BasicCommand attack3 = new BasicCommand("Attack_3");
	public static final BasicCommand attack4 = new BasicCommand("Attack_4");
	public static final BasicCommand attack5 = new BasicCommand("Attack_5");
	public static final BasicCommand attack6 = new BasicCommand("Attack_6");
	public static final BasicCommand attack = new BasicCommand("Attack");

	public static final BasicCommand selectTarget = new BasicCommand("Select Target");
	public static final BasicCommand runAway = new BasicCommand("Escape");

	public static final BasicCommand forward = new BasicCommand("Forward");
	public static final BasicCommand backward = new BasicCommand("backward");
	public static final BasicCommand left = new BasicCommand("Left");
	public static final BasicCommand right = new BasicCommand("Right");

	public static final BasicCommand interact = new BasicCommand("Interact");

	public static final BasicCommand quit = new BasicCommand("Quit");

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

		provider.bindCommand(new KeyControl(Input.KEY_W), forward);
		provider.bindCommand(new KeyControl(Input.KEY_S), backward);
		provider.bindCommand(new KeyControl(Input.KEY_A), left);
		provider.bindCommand(new KeyControl(Input.KEY_D), right);
		provider.bindCommand(new KeyControl(Input.KEY_UP), forward);
		provider.bindCommand(new KeyControl(Input.KEY_DOWN), backward);
		provider.bindCommand(new KeyControl(Input.KEY_LEFT), left);
		provider.bindCommand(new KeyControl(Input.KEY_RIGHT), right);

		provider.bindCommand(new KeyControl(Input.KEY_SPACE), interact);

		provider.bindCommand(new KeyControl(Input.KEY_ESCAPE), quit);
	}



	@Override
	public void controlPressed(Command command) {
		System.out.println(command.toString()+" pressed!");
	}

	@Override
	public void controlReleased(Command command) {
		System.out.println(command+" released!");
		if (command == quit) {
			container.exit();
		}
		commandDelegates.forEach(del -> del.action(command, container));
	}
}
