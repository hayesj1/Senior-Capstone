package game.character.moves;

import game.character.ILevelable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A set of Moves which can be learned by a species or group of species. After a new instanace is created, use {@link #putMove}
 * to add moves to this set. Once all moves have been added, call {@link #ready} to flag this moveset as complete. Completed
 * movesets are immutable.
 * A move learned at level:<ul>
 * <li>-1 will never be learned (this is useful for organizing moves into groups, or master lists of moves)</li>
 * <li>0 or 1 will be one of up to six "default" moves available to all species with this moveset</li>
 * <li>2 - 100 will be learned upon advancing to that level</li>
 * </ul>
 */
public class MoveSet {
	private HashMap<Move, Integer> moves;
	private boolean complete;

	public MoveSet() {
		this.moves = new HashMap<>(10);
		this.complete = false;
	}

	/**
	 * Populate this moveSet.
	 * Any negative elements of learnLevels are clamped to -1.
	 * Any elements of learnLevels which are greater than <code>ILevelable.MAX_LEVEL</code>, are clamped to <code>ILevelable.MAX_LEVEL</code>
	 * @param moves The moves
	 * @param learnLevels The levels those moves are learned at
	 */
	public void init(Move[] moves, int[] learnLevels) {
		if (moves.length != learnLevels.length) { throw new IllegalArgumentException("learnLevels array must be same size as moves array!"); }
		for (int i = 0; i < moves.length; i++) { putMove(moves[i], learnLevels[i]); }
		this.ready();
	}

	protected final void ready() {
		this.complete = true;
	}

	/**
	 * Adds a new move to the set or updates the level an existing move is learned.
	 * If levelLearned is null or negative, it is clamped to -1. If levelLearned is greater than <code>ILevelable.MAX_LEVEL</code>, it is clamped to <code>ILevelable.MAX_LEVEL</code>
	 * @param move the move to add/update
	 * @param levelLearned the level a species learns this move
	 * @return tue if this call updated an existing value, false otherwise
	 */
	protected boolean putMove(Move move, Integer levelLearned) {
		if (this.complete && !moves.containsKey(move)) {
			throw new IllegalStateException("Moveset is flagged as complete; no more moves can be added!");
		} else {
			boolean willReplace = false;
			if (moves.containsKey(move)) { willReplace = true; }
			// conform realLevel to the range -1 - 100. If levelLearned is null default to -1, otherwise clamp levelLearned at the extrema
			int realLevel = ( levelLearned == null ) ? -1 : (levelLearned < -1 ? -1 : ( levelLearned > ILevelable.MAX_LEVEL ? ILevelable.MAX_LEVEL : levelLearned ));
			moves.put(move, realLevel);
			return willReplace;
		}
	}

	/**
	 * @return true if move is in this moveset; false otherwise
	 */
	public boolean containsMove(Move move) {
		return moves.containsKey(move);
	}

	/**
	 * @return true if at least one move will be learned at the passed level; false otherwise
	 */
	public boolean containsMoveAtLevel(Integer levelLearned) {
		return moves.containsValue(levelLearned);
	}

	/**
	 * @return null if the passed move isn't in this moveset; otherwise, the level the passed move will be learned at
	 */
	public Integer willLearnMoveAt(Move move) {
		return moves.getOrDefault(move, null);
	}

	/**
	 * @return null if there aren't any moves in the set learned at the passed level; otherwise, the first move learned at the passed level
	 */
	public Move moveLearnedAt(int levelLearned) {
		Iterator<Map.Entry<Move, Integer>> it = moves.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Move, Integer> e = it.next();
			if (e.getValue().equals(levelLearned)) {
				return e.getKey();
			}
		}

		return null;
	}
}
