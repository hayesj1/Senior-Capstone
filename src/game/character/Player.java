package game.character;

import game.character.moves.Move;

public class Player extends PlayableCharacter {
	private static final int MAX_PARTY_SIZE = 6;

	private Monster[] party = new Monster[MAX_PARTY_SIZE];
	private int nextFreeSlot = 0;

	public Player(Species species, Stats stats, Move[] learnedMoves, String name) {
		super(species, name, learnedMoves, stats);
	}

	/**
	 * @return True if this character is capturable, false otherwise
	 */
	@Override
	public boolean isCapturable() {
		return false;
	}

	public IBattlable swapMonster() {
		//TODO: Ask user for swapee
		return this.party[0];
	}

	/**
	 * Attempt to capture this <code>IBattlable</code>. If successful, <code>capturer</code> claims this <code>IBattlable</code>
	 * @param capturer the <code>IBattlable</code> attempting to capture this <code>IBattlable</code>
	 * @return true on success, false if failure or this <code>IBattlable</code> is not capturable
	 * @see #isCapturable()
	 */
	@Override
	public boolean capture(IBattlable capturer) {
		return false;
	}

	/**
	 * Adds <code>captured</code> to this <code>IBattlable</code>'s team(if one exists)
	 * @param captured the newly captured <code>IBattlable</code>
	 */
	@Override
	public void addToTeam(IBattlable captured) {
		if (nextFreeSlot >= MAX_PARTY_SIZE) {
			//TODO: Ask user for swapee
		} else if (captured instanceof Monster){
			party[nextFreeSlot] = (Monster) captured;
		}

		captured.clearJustCaptured();
	}
}
