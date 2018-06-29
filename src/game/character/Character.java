package game.character;

import game.Capstone;
import game.character.moves.Move;

public abstract class Character implements ILevelable {

	protected Species species;
	protected String name;
	protected int level;
	protected int exp;
	protected int requiredExp;

	public Character(Species species, String name, int level) {
		this.species = species;
		this.name = name;
		this.level = Math.max(Math.min(level, ILevelable.MAX_LEVEL), 1);
		this.requiredExp = ILevelable.requiredExpForLevel(this.level+1);
		this.exp =  ILevelable.requiredExpForLevel(this.level);
	}

	public Species getSpecies() {
		return species;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getLevel() {
		return this.level;
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
			int moveSlot = (this.getMoveCount() < IBattlable.MAX_MOVES) ? this.getMoveCount()+1 : 0; // TODO ask user for moveslot to swap
			Move move = this.species.getLearnableMoves().moveLearnedAt(this.level);
			this.learnNewMove(move, moveSlot);
			Capstone.getInstance().getMoveButtons()[moveSlot].setText(move);
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
