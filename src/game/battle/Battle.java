package game.battle;

import game.Capstone;
import game.character.CapturableActor;
import game.character.IBattlable;
import game.character.PlayableActor;
import game.character.PlayerActor;
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
	private int activeActor = -1;

	private PlayableActor[] players;
	private CapturableActor[] foes;
	private TreeSet<IBattlable> order;

	public Battle(BattleCommandDelegate del) {
		this.commandDelegate = del;
		this.prepared = false;
		this.started = false;
		this.ended = false;
		this.playerWon = false;
		this.players = new PlayableActor[2];
		this.foes = new CapturableActor[2];
		this.order = new TreeSet<>(BattleOrder.getInstance());
	}

	/**
	 * Initializes battle participants and attack order
	 * @param playerteam Array of Player's participants
	 * @param foeTeam Array of Opponent's participants
	 */
	private void prepare(PlayableActor[] playerteam, CapturableActor[] foeTeam) {
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
	public void start(PlayableActor[] playerTeam, CapturableActor[] foeTeam) {
		if (started) { return; }

		prepare(playerTeam, foeTeam);
		activeActor = 0;
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
		if (!started || (moveSlot < 1 || moveSlot > players[activeActor].getMoveCount())) { return; }

		// Do player moves
		Turn turn = players[activeActor].planMove(moveSlot, foes);
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
			if (activeActor == 0 && players[1].isIncapacitated()) { activeActor++; }
			activeActor++;
		}

		// Do AI moves
		if (activeActor >= players.length) {
			needsAction = false;
			for (CapturableActor foe : foes) {
				if (foe.isIncapacitated()) { continue; }
				foe.planMove(players);
			}

			executeTurns();
			activeActor = 0;

			if (ended) {
				Capstone.getInstance().addFeedback(playerWon ? "You WIN!" : "You LOSE!");
				return;
			}

			needsAction = true;
		}
	}

	private void executeTurns() {
		if (!started) { return; }
		order.descendingSet().forEach(IBattlable::executeTurn);
		order.descendingSet().removeIf(IBattlable::isIncapacitated);

		if (order.size() < 4) {
			if (players[0].isKOed()) {
				playerWon = false; // you LOSE cause you DEAD
				ended = true;
			} else if (foes[0].isIncapacitated() && foes[1].isIncapacitated()) {
				playerWon = true; // you WIN cause foes is DEAD or CAPTURED
				ended = true;
			} else if (players[1].isKOed()) {
				players[1] = ((PlayerActor) players[0]).getNextAvailablePartyMember(players[1]);
			}
		}
	}

	public boolean isOver() { return this.ended; }
	public boolean isStarted() { return this.started; }
	public boolean playerVictory() { return this.playerWon; }
	public boolean needsUserAction() { return this.needsAction; }
	public IBattlable getActiveActor() { return this.players[this.activeActor]; }

}
