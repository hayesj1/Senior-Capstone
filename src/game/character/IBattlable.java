package game.character;

import game.battle.Turn;
import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;

import java.util.Random;

/**
 * Interface for actors which can battle other actors
 */
public interface IBattlable extends INamed {

	Random rand = new Random();
	int MAX_MOVES = 6;

	/**
	 * How much EXP is rewarding for defeating a monster of level <code>theirLevel</code> at <code>yourLevel</code>
	 * @param yourLevel your level when the foe is defeated
	 * @param theirLevel the defeated foe's level
	 * @return the EXP you'll gain
	 */
	static int expForDefeating(int yourLevel, int theirLevel) {
		//TODO: replace with permanent formula
		return (int) Math.ceil(Math.sqrt(theirLevel + 5));
	}

	/**
	 * This <code>IBattlable</code> will select a move to use on its upcoming turn
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	Turn planMove(IBattlable[] targets);

	/**
	 * This <code>IBattlable</code> will select the move in moveSLot to use on its upcoming turn
	 * @param moveSlot slot# of the selected move; values are 1 through 6
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	Turn planMove(int moveSlot, IBattlable[] targets);

	/**
	 * Execute the turn saved by a previous call to {@link #planMove(IBattlable[])}
	 * @return true if the move lands, false if it missed, if it is out of uses, or if there is some other failure
	 */
	boolean executeTurn();

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
	default boolean canUseMove(Move move) {
		return move.uses() > 0;
	}

	/**
	 * @return True if this IBattlable has 0 HP; false otherwise
	 */
	default boolean isKOed() {
		return HP() == 0;
	}

	/**
	 * @return True if this IBattlable can't fight for any reason(ex. it's KOed, captued, etc.); false otherwise
	 */
	default boolean isIncapacitated() { return isKOed(); }

	/**
	 * Render KO animation
	 */
	void KO();

	Stats getStats();
	int HP();
	int modifyHP(int delta);
	void setHP(int hp);

	int getMoveCount();
	Move[] getLearnedMoves();
	int getMoveSlotByMoveName(String name);

	int getLevel();
}
