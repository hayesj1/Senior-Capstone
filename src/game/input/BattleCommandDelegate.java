package game.input;

import game.Capstone;
import game.battle.Battle;
import game.character.IBattlable;
import game.gui.LabeledButton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.command.Command;

public class BattleCommandDelegate implements ICommandDelegate {
	private Battle battle;
	private IBattlable actor;
	private boolean initialized;

	public BattleCommandDelegate() {
		this.battle = null;
		this.actor = null;
		this.initialized = false;
	}

	public void init(Battle battle) {
		this.battle = battle;
		this.actor = battle.getActiveActor();
		this.initialized = true;
	}

	@Override
	public void action(Command command, GameContainer container) {
		if (!initialized || actor == null || !battle.needsUserAction()) { return; }

		Input input = container.getInput();
		int moveSlot = -1;
		if (command == DemoInputHandler.attack1) { moveSlot = 1; }
		else if (command == DemoInputHandler.attack2) { moveSlot = 2; }
		else if (command == DemoInputHandler.attack3) { moveSlot = 3; }
		else if (command == DemoInputHandler.attack4) { moveSlot = 4; }
		else if (command == DemoInputHandler.attack5) { moveSlot = 5; }
		else if (command == DemoInputHandler.attack6) { moveSlot = 6; }
		else if (command == DemoInputHandler.attack) {
			int mx = input.getMouseX();
			int my = input.getMouseY();
			LabeledButton[] buttons = Capstone.getInstance().getMoveButtons();
			LabeledButton clicked = getClicked(mx, my, buttons);

			if (clicked == null) { return; }
			moveSlot = this.actor.getMoveSlotByLearnedMoveName(clicked.getLabel());
		}

		if (moveSlot > 0 && moveSlot <= this.actor.getMoveCount() && !battle.isOver()) {
			Capstone.getInstance().setMoveSlot(moveSlot);
			this.actor = this.battle.getActiveActor();
		}
	}

	private LabeledButton getClicked(int mx, int my, LabeledButton[] buttons) {
		if (buttons == null) { return null; }
		LabeledButton clicked = null;
		for (LabeledButton button : buttons) {
			if (button.getBounds().contains(mx, my)) {
				clicked = button;
				break;
			}
		}
		return clicked;
	}
}
