package game.input;

import game.Capstone;
import game.battle.Battle;
import game.character.IBattlable;
import game.gui.IButtonDelegate;
import game.gui.LabeledButton;
import org.newdawn.slick.Input;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.gui.AbstractComponent;

public class BattleCommandDelegate implements IButtonDelegate {
	private Input input;
	private Battle battle;
	private IBattlable actor;
	private boolean initialized;

	public BattleCommandDelegate() { this(null); }
	public BattleCommandDelegate(Input input) {
		this.input = input;
		this.battle = null;
		this.actor = null;
		this.initialized = false;
	}

	public void init(Battle battle) {
		this.battle = battle;
		this.actor = battle.getActiveActor();
		this.initialized = true;
	}

	public void setActor(IBattlable character) {
		this.actor = character;
	}

	@Override
	public void action(Command command) {
		if (actor == null) { return; }
		int moveSlot = -1;
		if (command == DemoInputHandler.attack1) { moveSlot = 1; }
		else if (command == DemoInputHandler.attack2) { moveSlot = 2; }
		else if (command == DemoInputHandler.attack3) { moveSlot = 3; }
		else if (command == DemoInputHandler.attack4) { moveSlot = 4; }
		else if (command == DemoInputHandler.attack5) { moveSlot = 5; }
		else if (command == DemoInputHandler.attack6) { moveSlot = 6; }
		else if (command == DemoInputHandler.attack && input != null) {
			int mx = input.getMouseX();
			int my = input.getMouseY();
			LabeledButton clicked = null;
			LabeledButton[] buttons = Capstone.getInstance().getButtons();
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].getBounds().contains(mx, my)) {
					clicked = buttons[i];
					break;
				}
			}

			if (clicked != null) {
				clicked.interacted();
			}
		}

		if (moveSlot > 0 && !battle.isOver()) {
			battle.advanceTurn(moveSlot);
			this.setActor(this.battle.getActiveActor());
		}
	}

	@Override
	public void action(LabeledButton button) {
		if (actor == null) { return; }

		String label = button.getLabel();
		int moveSlot = this.actor.getMoveSlotByLearnedMoveName(label);

		if (moveSlot > 0 && !battle.isOver()) {
			battle.advanceTurn(moveSlot);
			this.setActor(this.battle.getActiveActor());
		}
	}

	@Override
	public void componentActivated(AbstractComponent abstractComponent) {

	}
}
