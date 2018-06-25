package game.character;

import game.character.moves.Move;

public class Monster extends PlayableCharacter {

	public Monster(Species species, Stats stats, Move[] learnedMoves) { this(species, "", stats, learnedMoves, 1); }
	public Monster(Species species, String nickname, Stats stats, Move[] learnedMoves, int level) {
		super(species, (nickname.isEmpty() ? species.getName(): nickname), learnedMoves, stats, level);

	}

	@Override
	public boolean isCapturable() { return true; }

	/**
	 * Adds <code>captured</code> to this <code>IBattlable</code>'s team(if one exists)
	 *
	 * @param captured the newly captured <code>IBattlable</code>
	 */
	@Override
	public void addToTeam(IBattlable captured) {
		return; // Monster don't have a team
	}

}
