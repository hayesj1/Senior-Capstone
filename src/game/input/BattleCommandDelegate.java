package game.input;

import game.battle.Battle;
import game.character.IBattlable;
import org.newdawn.slick.command.Command;

public class BattleCommandDelegate implements CommandDelegate {

	private Battle battle;
	private IBattlable actor;
	private boolean initialized = false;


	public void init(Battle battle) {
		this.battle = battle;
		this.actor = battle.getActiveActor();
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

		if (moveSlot > 0 && !battle.isOver()) {
			battle.advanceTurn(moveSlot);
			this.setActor(this.battle.getActiveActor());
		}
	}
}
