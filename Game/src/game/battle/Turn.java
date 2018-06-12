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

	public boolean execute() throws MoveOutOfUsesException {
		return attacker.attack(target, attack);
	}

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
