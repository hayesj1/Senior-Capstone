package game.character;

import game.character.moves.Move;

/**
 * For character which use the leveling system
 */
public interface ILevelable {
	int MAX_LEVEL = 100;

	static int requiredExpForLevel(int level) {
		//TODO: replace with permanent formula
		return (level < MAX_LEVEL) ? (int) Math.floor( 10 * Math.sqrt(level + 1) ) : 0;
	}

	static int expForDefeating(int yourLevel, int theirLevel) {
		//TODO: replace with permanent formula
		return (int) Math.ceil(Math.sqrt(theirLevel + 5));
	}

	int getLevel();
	int levelUp();

	void addEXP(int exp);

	/**
	 * Learn a new move.
	 * @param newMove The move to learn
	 * @param slot The slot in which newMove will be placed
	 * @return the move which was replaced by newMove or null if the slot was empty
	 */
	Move learnNewMove(Move newMove, int slot);
}
