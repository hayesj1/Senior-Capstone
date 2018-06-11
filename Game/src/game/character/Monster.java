package game.character;

import game.character.moves.Move;

public class Monster extends PlayableCharacter {

	public Monster(Species species, Stats stats, Move[] learnedMoves) { this(species, "", stats, learnedMoves, 1); }
	public Monster(Species species, String nickname, Stats stats, Move[] learnedMoves, int level) {
		super(species, (nickname.isEmpty() ? species.getName(): nickname), learnedMoves, stats, level);
	}

	@Override
	public boolean isCapturable() { return true; }

}
