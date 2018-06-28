package game.battle;

import game.character.IBattlable;
import game.character.Player;
import game.input.BattleCommandDelegate;

import java.util.Comparator;
import java.util.TreeSet;

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
			int speeds = Integer.compare(o1.getStats().speed(), o2.getStats().speed());
			return speeds == 0 ? IBattlable.rand.nextInt(3) - 1 : speeds;
		}

		public static BattleOrder getInstance() {
			return instance;
		}
	}

	private BattleCommandDelegate commandDelegate;
	private boolean prepared;
	private boolean started;
	private boolean ended;
	private boolean needsAction = false;
	private boolean playerWon;
	private int actor = -1;

	private IBattlable[] playerParty;
	private IBattlable[] opposingParty;
	private TreeSet<IBattlable> order;

	public Battle(BattleCommandDelegate del) {
		this.commandDelegate = del;
		this.prepared = false;
		this.started = false;
		this.ended = false;
		this.playerWon = false;
		this.playerParty = new IBattlable[2];
		this.opposingParty = new IBattlable[2];
		this.order = new TreeSet<>(BattleOrder.getInstance());
	}

	/**
	 * Initializes battle participants and attack order
	 * @param playerteam Array of Player's participants
	 * @param opposingTeam Array of Opponent's participants
	 */
	private void prepare(IBattlable[] playerteam, IBattlable[] opposingTeam) {
		if (prepared) { return; }

		System.arraycopy(playerteam, 0, this.playerParty, 0, 2);
		System.arraycopy(opposingTeam, 0, this.opposingParty, 0, 2);

		this.order.add(playerteam[0]);
		this.order.add(playerteam[1]);
		this.order.add(opposingTeam[0]);
		this.order.add(opposingTeam[1]);

		this.prepared = true;
	}

	/**
	 * Starts battle between playerTeam and opposingTeam
	 * @param playerTeam Array of Player's participants
	 * @param opposingTeam Array of Opponent's participants
	 */
	public void start(IBattlable[] playerTeam, IBattlable[] opposingTeam) {
		if (started) { return; }

		prepare(playerTeam, opposingTeam);
		actor = 0;
		needsAction = true;
		started = true;
	}

	public void advanceTurn(int moveSlot) {
		if (!started) { return; }

		// Do player moves
		if (actor == 0 && playerParty[1] == null) {
			playerParty[actor].planMove(moveSlot, opposingParty);
			actor++;
		} else {
			playerParty[actor].planMove(moveSlot, opposingParty);
		}
		actor++;

		// Do AI moves
		if (actor >= playerParty.length) {
			needsAction = false;
			int moveIdx1 = IBattlable.rand.nextInt(opposingParty[0].getMoveCount());
			int moveIdx2 = IBattlable.rand.nextInt(opposingParty[1].getMoveCount());
			opposingParty[0].planMove(moveIdx1+1, playerParty);
			opposingParty[1].planMove(moveIdx2+1, playerParty);
			executeTurns();

			actor = 0;
			needsAction = true;

		}
	}

	private void executeTurns() {
		if (!started) { return; }
		order.descendingSet().forEach(IBattlable::executeTurn);
		order.descendingSet().removeIf(IBattlable::isKOed);
		order.descendingSet().removeIf(IBattlable::justCaptured);

		if (order.size() < 4) {
			if (playerParty[0].isKOed()) {
				playerWon = false; // you LOSE cause you DEAD
				ended = true;
			} else if ((opposingParty[0].isKOed() || opposingParty[0].justCaptured()) && (opposingParty[1].isKOed() || opposingParty[1].justCaptured())) {
				playerWon = true; // you WIN cause opposingParty is DEAD or CAPTURED
				ended = true;
			} else if (playerParty[1].isKOed()) {
				playerParty[1] = ((Player) playerParty[0]).swapMonster();
			}
		}
	}

	public boolean isOver() { return this.ended; }
	public boolean playerVictory() { return this.playerWon; }
	public boolean needsUserAction() { return this.needsAction; }
	public IBattlable getActiveActor() { return this.playerParty[this.actor]; }

}
