package game.character;

import game.character.moves.Move;

public class Player extends PlayableCharacter {
	private static final int MAX_PARTY_SIZE = 6;

	private Monster[] party = new Monster[MAX_PARTY_SIZE];

	public Player(Species species, Stats stats, Move[] learnedMoves, String name) {
		super(species, name, learnedMoves, stats);
	}
}
