package game.battle;

import game.character.IBattlable;
import game.character.Player;

import java.util.Comparator;
import java.util.TreeMap;

public class Battle {
	public static class BattleOrder implements Comparator<IBattlable> {
		private static final BattleOrder instance = new BattleOrder();
		private BattleOrder() {}

		/**
		 * Compares its two arguments for order.  Returns a negative integer,
		 * zero, or a positive integer as the first argument is less than, equal
		 * to, or greater than the second.<p>
		 * <p>
		 * In the foregoing description, the notation
		 * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
		 * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
		 * <tt>0</tt>, or <tt>1</tt> according to whether the value of
		 * <i>expression</i> is negative, zero or positive.<p>
		 * <p>
		 * The implementor must ensure that <tt>sgn(compare(x, y)) ==
		 * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
		 * implies that <tt>compare(x, y)</tt> must throw an exception if and only
		 * if <tt>compare(y, x)</tt> throws an exception.)<p>
		 * <p>
		 * The implementor must also ensure that the relation is transitive:
		 * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
		 * <tt>compare(x, z)&gt;0</tt>.<p>
		 * <p>
		 * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt>
		 * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
		 * <tt>z</tt>.<p>
		 * <p>
		 * It is generally the case, but <i>not</i> strictly required that
		 * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
		 * any comparator that violates this condition should clearly indicate
		 * this fact.  The recommended language is "Note: this comparator
		 * imposes orderings that are inconsistent with equals."
		 *
		 * @param o1 the first object to be compared.
		 * @param o2 the second object to be compared.
		 * @return a negative integer, zero, or a positive integer as the
		 * first argument is less than, equal to, or greater than the
		 * second.
		 * @throws NullPointerException if an argument is null and this
		 *                              comparator does not permit null arguments
		 * @throws ClassCastException   if the arguments' types prevent them from
		 *                              being compared by this comparator.
		 */
		@Override
		public int compare(IBattlable o1, IBattlable o2) {
			return Integer.compare(o1.getStats().speed(), o2.getStats().speed());
		}

		public static BattleOrder getInstance() {
			return instance;
		}
	}

	private IBattlable[] playerParty;
	private IBattlable[] opposingParty;

	private TreeMap<IBattlable, IBattlable[]> order;

	public Battle() {
		this.playerParty = new IBattlable[2];
		this.opposingParty = new IBattlable[2];
		this.order = new TreeMap<>(BattleOrder.getInstance());
	}

	/**
	 * Initializes battle participants and attack order
	 * @param playerteam Array of Player's participants
	 * @param opposingTeam Array of Opponent's participants
	 */
	private void prepare(IBattlable[] playerteam, IBattlable[] opposingTeam) {

		System.arraycopy(playerteam, 0, this.playerParty, 0, 2);
		System.arraycopy(opposingTeam, 0, this.opposingParty, 0, 2);

		this.order.put(playerteam[0], opposingTeam);
		this.order.put(playerteam[1], opposingTeam);
		this.order.put(opposingTeam[0], playerteam);
		this.order.put(opposingTeam[1], playerteam);

	}

	/**
	 * Starts battle between playerTeam and opposingTeam
	 * @param playerTeam Array of Player's participants
	 * @param opposingTeam Array of Opponent's participants
	 * @return true if the Player won, false otherwise
	 */
	public boolean start(IBattlable[] playerTeam, IBattlable[] opposingTeam) {
		prepare(playerTeam, opposingTeam);
		boolean playerWon = false;
		do {
			order.descendingMap().forEach(IBattlable::planMove);
			order.descendingKeySet().forEach(IBattlable::executeTurn);
			order.descendingKeySet().removeIf(IBattlable::isKOed);
			order.descendingKeySet().removeIf(IBattlable::justCaptured);

			if (order.size() < 4) {
				if (this.playerParty[0].isKOed()) {
					playerWon = false;
					break; // you LOSE cause you DEAD
				} else if (this.opposingParty[0].isKOed() && this.opposingParty[1].isKOed()) {
					playerWon = true;
					break; // you WIN cause opposingParty is DEAD or CAPTURED
				} else if (this.playerParty[1].isKOed()) {
					this.playerParty[1] = ((Player) this.playerParty[0]).swapMonster();
				}
			}
		} while(order.size() > 0);

		return playerWon;
	}
}
