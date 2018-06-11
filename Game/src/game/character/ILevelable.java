package game.character;

import game.character.moves.Move;

public interface ILevelable {
	int MAX_LEVEL = 100;

	static int requiredExpForLevel(int level) {
		//TODO: replace with permanent formula
		return (level < MAX_LEVEL) ? (2 >> (level / 4) + 1) + (2 * level) : 0;
	}

	static int expForDefeating(int yourLevel, int theirLevel) {
		//TODO: replace with permanent formula
		int bigger = Math.max(yourLevel, theirLevel);
		return (int) (1 / Math.log( Math.abs(theirLevel - yourLevel) ) + ( Math.floorDiv(bigger, 10) * 100 ));
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
