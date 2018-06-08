package game.character;

import game.character.moves.Move;

public class Player extends PlayableCharacter {
	private static final int MAX_TEAM_SIZE = 6;

	private Monster[] team = new Monster[MAX_TEAM_SIZE];

	public Player(Species species, Stats stats, Move[] learnedMoves, String name) {
		super(species, stats, learnedMoves, name);
	}
}
