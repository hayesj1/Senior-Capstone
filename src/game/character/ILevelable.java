package game.character;

import game.character.moves.Move;

/**
 * For character which use the leveling system
 */
public interface ILevelable extends IBattlable {
	int MAX_LEVEL = 100;

	/**
	 * How much EXP is a ILevelable is required to have to level up to the passed level
	 * @param level the target level
	 * @return the amount of EXP required to levelup to <code>level</code>
	 */
	static int requiredExpForLevel(int level) {
		//TODO: replace with permanent formula
		return (level < MAX_LEVEL) ? (int) Math.floor( 10 * Math.sqrt(level + 1) ) : 0;
	}

	/**
	 * Attempt to level up
	 * @return True if levelUp was successful, false otherwise
	 */
	boolean levelUp();

	/**
	 * Learn a new move.
	 * @param newMove The move to learn
	 * @param slot The slot in which newMove will be placed
	 * @return the move which was replaced by newMove or null if the slot was empty
	 */
	Move learnNewMove(Move newMove, int slot);

	void addEXP(int exp);
}
