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
		if (!ready()) { throw new IllegalStateException("Turn is not ready for Execution! "+this); }
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

	/**
	 * Returns a string representation of the object. In general, the
	 * {@code toString} method returns a string that
	 * "textually represents" this object. The result should
	 * be a concise but informative representation that is easy for a
	 * person to read.
	 * It is recommended that all subclasses override this method.
	 * <p>
	 * The {@code toString} method for class {@code Object}
	 * returns a string consisting of the name of the class of which the
	 * object is an instance, the at-sign character `{@code @}', and
	 * the unsigned hexadecimal representation of the hash code of the
	 * object. In other words, this method returns a string equal to the
	 * value of:
	 * <blockquote>
	 * <pre>
	 * getClass().getName() + '@' + Integer.toHexString(hashCode())
	 * </pre></blockquote>
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return attacker.toString()+" --> "+attack+" --> "+target;
	}
}
