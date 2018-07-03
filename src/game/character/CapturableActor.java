package game.character;

import game.battle.Turn;
import game.character.moves.Move;

public class CapturableActor extends BattlableActor implements ICapturable {

	private boolean justCaptured;

	public CapturableActor(Species species, Stats stats, Move[] learnedMoves) { this(species, stats, 1, learnedMoves); }
	public CapturableActor(Species species, Stats stats, int level, Move[] learnedMoves) {
		super(species, species.getName(), stats, level, learnedMoves);

		this.justCaptured = false;
	}

	/**
	 * Clones this instance into a <code>PlayableActor</code> instance
	 * @return A PlayableActor cloned from this instance
	 */
	@Override
	public PlayableActor convertToPlayable() {
		PlayableActor res = new PlayableActor(this.getSpecies(), this.getName(), this.getStats(), this.level, this.getLearnedMoves());
		res.setHP(this.HP);
		res.setSelectedMove(-1);
		return res;
	}

	/**
	 * This <code>BattlableActor</code> will select a move to use on its upcoming turn
	 *
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	@Override
	public Turn planMove(IBattlable[] targets) {
		if (plannedTurn) { return turn; }
		turn.setTarget(null);
		turn.setAttack(null);

		selectedSlot = IBattlable.rand.nextInt(getMoveCount()) + 1;
		turn.setAttack(learnedMoves[selectedSlot-1]);
		turn.setAttack(learnedMoves[selectedSlot-1]);

		if (!targets[0].isIncapacitated() && targets[1].isIncapacitated()) {
			turn.setTarget(targets[0]);
		} else if (targets[0].isIncapacitated() && !targets[1].isIncapacitated()) {
			turn.setTarget(targets[1]);
		} else {
			int targetIdx = ( IBattlable.rand.nextInt(10) < 5 ) ? 0 : 1;
			turn.setTarget(targets[targetIdx]);
		}

		plannedTurn = true;
		return turn;
	}

	/**
	 * @return True if this character is capturable, false otherwise
	 */
	@Override
	public boolean isCapturable() {
		return true;
	}

	/**
	 * @return True if this IBattlable was captured in the current battle; false otherwise
	 */
	@Override
	public boolean wasJustCaptured() {
		return this.justCaptured;
	}

	/** Flags this <code>IBattlable</code> as captured in the current battle */
	@Override
	public void justCaptured() {
		this.justCaptured = true;
	}

	/** Flags this <code>IBattlable</code> as captured in a previous battle */
	@Override
	public void clearJustCaptured() {
		this.justCaptured = false;
	}

	/**
	 * @return True if this IBattlable can't fight for any reason(ex. it's KOed, captued, etc.); false otherwise
	 */
	@Override
	public boolean isIncapacitated() {
		return this.isKOed() || this.wasJustCaptured();
	}
}
