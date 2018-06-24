package game.character;

import game.battle.Turn;
import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;

import java.util.Random;

/**
 * For characters which can use the battle system
 */
public interface IBattlable {

	Random rand = new Random();

	/**
	 * @return True if this character should be capturable, false otherwise
	 */
	boolean isCapturable();

	/**
	 * @return True if this IBattlable was captured in the current battle; false otherwise
	 */
	boolean justCaptured();

	/** Flags this <code>IBattlable</code> as captured in the current battle if <code>justCaptured</code> is true;
	 * otherwise flags this <code>IBattlable</code> as not captured or captured in a previous battle
	 * @param justCaptured true if this <code>IBattlable</code> was just captured, false to reset
	 */
	void justCaptured(boolean justCaptured);

	/**
	 * Attempt to capture this <code>IBattlable</code>. If successful, <code>capturer</code> claims this <code>IBattlable</code>
	 * @param capturer the <code>IBattlable</code> attempting to capture this <code>IBattlable</code>
	 * @return true on success, false if failure or this <code>IBattlable</code> is not capturable
	 * @see #isCapturable()
	 */
	default boolean capture(IBattlable capturer) {
		if (!isCapturable()) { return false; }
		int randNum = IBattlable.rand.nextInt(100) + 1;
		int catchChance = (1 - ( HP() / getStats().maxHP() )) * 100;
		if (randNum <= catchChance) {
			this.justCaptured(true);
			capturer.addToTeam(this);
			return true;
		} else { return false; }
	}

	/**
	 * Adds <code>captured</code> to this <code>IBattlable</code>'s team(if one exists)
	 * @param captured the newly captured <code>IBattlable</code>
	 */
	void addToTeam(IBattlable captured);

	/**
	 * This <code>IBattlable</code> will select a move to use on its upcoming turn
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	Turn planMove(IBattlable[] targets);

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

	default boolean isKOed() {
		return HP() == 0;
	}

	/**
	 * Render KO animation
	 */
	void KO();

	Stats getStats();
	int HP();
	int modifyHP(int delta);
	void setHP(int hp);
}
