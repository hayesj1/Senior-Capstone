package game.battle;

import game.Capstone;
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
	public static final int MAX_TEAM_SIZE = 2;

	private BattleCommandDelegate commandDelegate;
	private boolean prepared;
	private boolean started;
	private boolean ended;
	private boolean needsAction = false;
	private boolean playerWon;
	private int actor = -1;

	private IBattlable[] players;
	private IBattlable[] foes;
	private TreeSet<IBattlable> order;

	public Battle(BattleCommandDelegate del) {
		this.commandDelegate = del;
		this.prepared = false;
		this.started = false;
		this.ended = false;
		this.playerWon = false;
		this.players = new IBattlable[2];
		this.foes = new IBattlable[2];
		this.order = new TreeSet<>(BattleOrder.getInstance());
	}

	/**
	 * Initializes battle participants and attack order
	 * @param playerteam Array of Player's participants
	 * @param foeTeam Array of Opponent's participants
	 */
	private void prepare(IBattlable[] playerteam, IBattlable[] foeTeam) {
		if (prepared) { return; }

		System.arraycopy(playerteam, 0, this.players, 0, MAX_TEAM_SIZE);
		System.arraycopy(foeTeam, 0, this.foes, 0, MAX_TEAM_SIZE);

		this.order.add(playerteam[0]);
		this.order.add(playerteam[1]);
		this.order.add(foeTeam[0]);
		this.order.add(foeTeam[1]);

		this.prepared = true;
	}

	/**
	 * Starts battle between playerTeam and foeTeam
	 * @param playerTeam Array of Player's participants
	 * @param foeTeam Array of Opponent's participants
	 */
	public void start(IBattlable[] playerTeam, IBattlable[] foeTeam) {
		if (started) { return; }

		prepare(playerTeam, foeTeam);
		actor = 0;
		needsAction = true;
		started = true;
	}

	/**
	 * Attempt to advance the state of the battle by the active actor's <code>planMove(...)</code> method.
	 * If the advancing is successful, and all Human players have planned their moves,
	 *    the AI players plan theirs and the turns are executed
	 * @param moveSlot The slot of the selected move
	 */
	public void advanceTurn(int moveSlot) {
		if (!started || (moveSlot < 1 || moveSlot > players[actor].getMoveCount())) { return; }

		// Do player moves
		Turn turn = players[actor].planMove(moveSlot, foes);
		if (turn.getTarget() == null) {
			Capstone.getInstance().selectTarget(foes);
			try {
				int targetSlot = Capstone.getInstance().getSelectedTargetSlot();
				turn.setTarget(foes[targetSlot - 1]);
			} catch (IllegalStateException ise) {
				return;
			}
		}

		if (turn.ready()) {
			if (actor == 0 && players[1].isKOed()) { actor++; }
			actor++;
		}

		// Do AI moves
		if (actor >= players.length) {
			needsAction = false;
			for (IBattlable foe : foes) {
				if (foe.isKOed() || foe.wasJustCaptured()) {
					continue;
				}

				int moveIdx = IBattlable.rand.nextInt(foe.getMoveCount());
				turn = foe.planMove(moveIdx + 1, players);
				if (turn.getTarget() == null) {
					int targetIdx = ( IBattlable.rand.nextInt(10) < 5 ) ? 0 : 1;
					turn.setTarget(players[targetIdx]);
				}
			}

			executeTurns();

			actor = 0;
			needsAction = true;

		}
	}

	private void executeTurns() {
		if (!started) { return; }
		order.descendingSet().forEach(IBattlable::executeTurn);
		order.descendingSet().removeIf(IBattlable::isKOed);
		order.descendingSet().removeIf(IBattlable::wasJustCaptured);

		if (order.size() < 4) {
			if (players[0].isKOed()) {
				playerWon = false; // you LOSE cause you DEAD
				ended = true;
			} else if (( foes[0].isKOed() || foes[0].wasJustCaptured()) && ( foes[1].isKOed() || foes[1].wasJustCaptured())) {
				playerWon = true; // you WIN cause foes is DEAD or CAPTURED
				ended = true;
			} else if (players[1].isKOed()) {
				players[1] = ((Player) players[0]).swapMonster();
			}
		}
	}

	public boolean isOver() { return this.ended; }
	public boolean isStarted() { return this.started; }
	public boolean playerVictory() { return this.playerWon; }
	public boolean needsUserAction() { return this.needsAction; }
	public IBattlable getActiveActor() { return this.players[this.actor]; }

}
