package game.character;

import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;

import java.util.Random;

public interface IBattlable {

	Random rand = new Random();

	/**
	 * @return True if this character should be capturable, false otherwise
	 */
	boolean isCapturable();

	/**
	 * <code>this</code> monster attempts to deal damage to <code>defender</code> with <code>move</code>. If the move
	 * lands, <code>defender.attackedBy(this, move)</code> is called. Otherwise the move misses. The chance to hit is
	 * <code>move.accuracy() / 100</code>.
	 * @param defender opposing monster
	 * @param move the incoming attack
	 * @return True if the attack lands, false otherwise
	 */
	boolean attack(IBattlable defender, Move move) throws MoveOutOfUsesException;

	/**
	 * Deals damage to <code>this</code> monster with <code>move</code> used by <code>attacker</code>
	 * via the Damage Equation: <code>damage = attacker.stats.attack() / this.stats.defense() * move.power()</code>
	 * @param attacker opposing monster
	 * @param move the incoming attack
	 * @return True if this monster is KO-ed - i.e. <code>this.HP == 0</code> - by this attack, false otherwise
	 */
	boolean attackedBy(IBattlable attacker, Move move);

	/**
	 * @param move the move in question
	 * @return True if <code>this</code> can use <code>move</code>, false otherwise
	 */
	boolean canUseMove(Move move);
	Stats getStats();
	int HP();
	int modifyHP(int delta);
	void setHP(int hp);

}
