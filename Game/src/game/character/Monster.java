package game.character;

import game.character.moves.Move;

public class Monster extends PlayableCharacter {

	public Monster(Species species, Stats stats, Move[] learnedMoves) {
		super(species, stats, learnedMoves, species.getName());
	}

	@Override
	public boolean isCapturable() { return true; }

}
