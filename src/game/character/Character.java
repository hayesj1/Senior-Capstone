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

	/**
	 * Returns a string representation of the object. In general, the
	 * {@code toString} method returns a string that
	 * "textually represents" this object. The result should
	 * be a concise but informative representation that is easy for a
	 * person to read.
	 * It is recommended that all subclasses override this method.
	 * <p>
	 * The {@code toString} method for class {@code Object}
	 * returns a string consisting of the name of the class of which the
	 * object is an instance, the at-sign character `{@code @}', and
	 * the unsigned hexadecimal representation of the hash code of the
	 * object. In other words, this method returns a string equal to the
	 * value of:
	 * <blockquote>
	 * <pre>
	 * getClass().getName() + '@' + Integer.toHexString(hashCode())
	 * </pre></blockquote>
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
