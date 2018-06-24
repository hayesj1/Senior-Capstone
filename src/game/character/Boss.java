package game.character;

import game.character.moves.Move;

public class Boss extends Monster {

	public Boss(Species species, Stats stats, Move[] learnedMoves) {
		super(species, stats, learnedMoves);
	}

	/**
	 * @return True if this character should be capturable, false otherwise
	 */
	@Override
	public boolean isCapturable() { return false; }
}
