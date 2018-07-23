package game.character;

import game.SuperDungeoneer;
import game.battle.Turn;
import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;

public class PlayableActor extends BattlableActor implements ILevelable {
	protected int exp;
	protected int requiredExp;

	public PlayableActor(Species species, Stats stats, Move[] learnedMoves) { this(species, species.getName(), stats, learnedMoves); }
	public PlayableActor(Species species, String name, Stats stats, Move[] learnedMoves) { this(species, name, stats, 1, learnedMoves); }
	public PlayableActor(Species species, String name, Stats stats, int level, Move[] learnedMoves) {
		super(species, name, stats, level, learnedMoves);

		this.exp =  ILevelable.requiredExpForLevel(this.level);
		this.requiredExp = ILevelable.requiredExpForLevel(this.level+1);
	}

	/**
	 * This <code>BattlableActor</code> will select a move to use on its upcoming turn
	 *
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	@Override
	public Turn planMove(IBattlable[] targets) {
		if (plannedTurn) { return turn; }
		//turn.setTarget(null);
		//turn.setAttack(null);

		if (selectedSlot < 0) {
			return turn;
		} else if (selectedSlot == 0) {
			turn.setAttack(Move.capture);
		} else {
			turn.setAttack(learnedMoves[selectedSlot - 1]);
			selectedSlot = -1;
		}

		if (!targets[0].isIncapacitated() && targets[1].isIncapacitated()) {
			turn.setTarget(targets[0]);
		} else if (targets[0].isIncapacitated() && !targets[1].isIncapacitated()) {
			turn.setTarget(targets[1]);
		}
		plannedTurn = true;
		return turn;
	}

	/**
	 * <code>this</code> BattleableActor attempts to deal damage to <code>defender</code> with <code>move</code>. If the move
	 * lands, <code>defender.attackedBy(this, move)</code> is called. Otherwise the move misses. The chance to hit is
	 * <code>move.accuracy() / 100</code>.
	 *
	 * @param defender opposing monster
	 * @param move     the incoming attack
	 * @return True if the attack lands, false otherwise
	 */
	@Override
	public boolean attack(IBattlable defender, Move move) throws MoveOutOfUsesException {
		boolean res = super.attack(defender, move);
		if (res && defender.isIncapacitated()) {
			this.addEXP(IBattlable.expForDefeating(this.getLevel(), defender.getLevel()));
		}

		return res;
	}

	/**
	 * Learn a new move. If {@link #moveCount} is less than {@link #MAX_MOVES}, the slot param is ignored and the new
	 * move is placed in the first free move slot
	 * @param newMove The move to learn
	 * @param slot The moveSlot which wil be replaced.
	 * @return the move which was replaced by newMove or null if the slot was empty
	 */
	@Override
	public Move learnNewMove(Move newMove, int slot) {
		if (moveCount == MAX_MOVES) {
			Move old = learnedMoves[slot-1];
			learnedMoves[slot-1] = newMove;
			return old;
		} else {
			learnedMoves[moveCount++] = newMove;
			return null;
		}
	}

	@Override
	public void setSelectedMove(int slot) {
		if (slot <= 0) {
			return;
		}
		super.setSelectedMove(slot);
	}

	/**
	 * Attempt to level up
	 * @return True if levelUp was successful, false otherwise
	 */
	@Override
	public boolean levelUp() {
		if (this.exp < ILevelable.requiredExpForLevel(this.level)) { return false; }

		this.requiredExp = ILevelable.requiredExpForLevel(++this.level);
		if (this.species.getLearnableMoves().containsMoveAtLevel(this.level)) {
			// TODO ask user for moveSlot to swap
			int moveSlot = (this.getMoveCount() < IBattlable.MAX_MOVES) ? this.getMoveCount()+1 : 0;
			Move move = this.species.getLearnableMoves().moveLearnedAt(this.level);
			this.learnNewMove(move, moveSlot);
			SuperDungeoneer.getInstance().getMoveButtons()[moveSlot].setLabelText(move);
		}
		return true;
	}

	@Override
	public void addEXP(int exp) {
		this.exp += exp;
		if (this.requiredExp > 0 && this.exp >= this.requiredExp) {
			this.levelUp();
		}
	}
}
