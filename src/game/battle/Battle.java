package game.battle;

import game.SuperDungeoneer;
import game.character.*;
import game.input.BattleCommandDelegate;

import java.util.*;
import java.util.function.Predicate;

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
	private int playerIdx;
	private PlayerActor player;
	private ArrayList<PlayableActor> players;
	private ArrayList<CapturableActor> foes;
	private TreeSet<BattlableActor> order;

	public Battle(BattleCommandDelegate del) {
		this.commandDelegate = del;
		this.prepared = false;
		this.started = false;
		this.ended = false;
		this.playerWon = false;
		this.players = new ArrayList<>(2);
		this.foes = new ArrayList<>(2);
		this.order = new TreeSet<>(BattleOrder.getInstance());
	}

	/**
	 * Initializes battle participants and attack order
	 * @param playerteam Array of Player's participants
	 * @param foeTeam Array of Opponent's participants
	 */
	private void prepare(PlayableActor[] playerteam, CapturableActor[] foeTeam) {
		if (prepared) { return; }

		for (int i = 0; i < playerteam.length; i++) {
			if (playerteam[i] instanceof PlayerActor) {
				this.player = (PlayerActor) playerteam[i];

				if (i >= MAX_TEAM_SIZE) {
					playerteam[0] = this.player;
				}
			}
		}

		Collections.addAll(this.players, Arrays.copyOf(playerteam, MAX_TEAM_SIZE));
		Collections.addAll(this.foes, Arrays.copyOf(foeTeam, MAX_TEAM_SIZE));
		this.order.addAll(this.players);
		this.order.addAll(this.foes);

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
		if (!started) { return; }

		PlayableActor[] playerArr = players.toArray(new PlayableActor[0]);
		CapturableActor[] foeArr = foes.toArray(new CapturableActor[0]);

		// Do player moves
		if (activeActor < players.size() && (moveSlot > 0 && moveSlot <= players.get(activeActor).getMoveCount())) {
			Turn turn = players.get(activeActor).planMove(moveSlot, foeArr);
			if (turn.getTarget() == null) {
				SuperDungeoneer.getInstance().selectTarget(foeArr);
				try {
					int targetSlot = SuperDungeoneer.getInstance().getSelectedTargetSlot();
					turn.setTarget(foes.get(targetSlot - 1));
				} catch (IllegalStateException ise) {
					return;
				}
			}

			if (turn.ready()) {
				if (activeActor == 0 && (players.size() == 1 || players.get(1).isIncapacitated())) {
					activeActor++;
				}
				activeActor++;
			}
		}

		// Do AI moves
		if (activeActor >= players.size()) {
			if (activeActor >= players.size()) {
				needsAction = false;
				for (CapturableActor foe : foes) {
					if (foe.isIncapacitated()) {
						continue;
					}
					Turn turn = foe.planMove(playerArr);
					if (!turn.ready() && !foe.planMove(playerArr).ready()) {
						return;
					}
				}
			}
		}

		boolean ready = order.stream().allMatch(iBattlable -> iBattlable.getTurn().ready());
		if (ready) {
			executeTurns();
			activeActor = 0;
		} else {
			//	Stream<BattlableActor> unready = order.stream().filter(iBattlable -> !iBattlable.getTurn().ready());
			//	unready.forEachOrdered(iBattlable -> System.out.println(iBattlable.getTurn().getAttack()));//iBattlable.planMove(moveSlot, (iBattlable instanceof CapturableActor ? playerArr : foeArr)));
		}

		if (ended) {
			SuperDungeoneer.getInstance().addFeedback(playerWon ? "You WIN!" : "You LOST!");
		} else {
			needsAction = true;
		}
	}

	private void executeTurns() {
		if (!started) { return; }
		order.descendingSet().forEach(IBattlable::executeTurn);
		order.descendingSet().removeIf(IBattlable::isIncapacitated);

		Predicate<CapturableActor> isIncapacitated = CapturableActor::isIncapacitated;
		boolean allIncapacitated = foes.stream().allMatch(isIncapacitated);
		boolean someIncapacitated = foes.stream().anyMatch(isIncapacitated);
		if (order.size() < 4) {
			if (player.isKOed()) {
				playerWon = false; // you LOSE cause you DEAD
				ended = true;
			} else if (allIncapacitated) {
				playerWon = true; // you WIN cause foes is DEAD or CAPTURED
				ended = true;
			} else if (players.get(1).isKOed()) {
				PlayableActor next = player.getNextAvailablePartyMember(players.get(1));
				if (next != null) {
					players.set(1, next);
				}
			}
		}
	}

	public void clearState() {
		this.playerWon = false;
		this.playerIdx = 0;

		this.players.clear();
		this.foes.clear();
		this.order.clear();
		this.player = null;
		this.prepared = false;

		this.activeActor = -1;
		this.needsAction = false;
		this.started = false;
		this.ended = false;
	}

	public boolean isOver() { return this.ended; }
	public boolean isStarted() { return this.started; }
	public boolean playerVictory() { return this.playerWon; }
	public boolean needsUserAction() { return this.needsAction; }
	public IBattlable getActiveActor() { return this.activeActor >= 0 && this.activeActor < this.players.size() ? this.players.get(this.activeActor) : null; }

	public ArrayList<PlayableActor> getPlayers() { return players; }
	public ArrayList<CapturableActor> getFoes() { return foes; }

}
