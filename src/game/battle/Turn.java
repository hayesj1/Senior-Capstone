package game.battle;

import game.character.IBattlable;
import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;

public class Turn {
	private IBattlable attacker;
	private IBattlable target;
	private Move attack;

	public Turn(IBattlable attacker, IBattlable target, Move attack) {
		this.attacker = attacker;
		this.target = target;
		this.attack = attack;
	}

	/**
	 * Executes the saved move
	 * @return true if the move lands, false if it missed, if it is out of uses, or if there is some other failure
	 * @throws MoveOutOfUsesException if the move has no uses up
	 */
	public boolean execute() throws MoveOutOfUsesException {
		boolean res = attacker.attack(target, attack);
		this.target = null;
		this.attack = null;

		return res;
	}

	/**
	 * @return True if a call to <code>{@link this.execute()}</code> would not throw a NPE, false otherwise
	 */
	public boolean ready() { return this.attacker != null && this.target != null && this.attack != null; }

	public IBattlable getAttacker() {
		return attacker;
	}
	public void setAttacker(IBattlable attacker) {
		this.attacker = attacker;
	}

	public IBattlable getTarget() {
		return target;
	}
	public void setTarget(IBattlable target) {
		this.target = target;
	}

	public Move getAttack() {
		return attack;
	}
	public void setAttack(Move attack) {
		this.attack = attack;
	}
}
