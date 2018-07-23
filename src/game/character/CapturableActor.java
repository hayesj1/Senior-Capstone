package game.character;

import game.battle.Turn;
import game.character.moves.Move;

public class CapturableActor extends BattlableActor implements ICapturable, Cloneable {

	private boolean justCaptured;

	public CapturableActor(Species species, Stats stats, Move[] learnedMoves) { this(species, stats, 1, learnedMoves); }
	public CapturableActor(Species species, Stats stats, int level, Move[] learnedMoves) {
		super(species, species.getName(), stats, level, learnedMoves);

		this.justCaptured = false;
	}

	/**
	 * Clones this instance into a <code>PlayableActor</code> instance
	 * @return A PlayableActor cloned from this instance
	 */
	@Override
	public PlayableActor convertToPlayable() {
		PlayableActor res = new PlayableActor(this.getSpecies(), this.getName(), this.getStats(), this.level, this.getLearnedMoves());
		res.setHP(this.HP);
		res.setSelectedMove(-1);
		return res;
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

		Move move;
		do {
			selectedSlot = IBattlable.rand.nextInt(getMoveCount()) + 1;
			move = learnedMoves[selectedSlot-1];
		} while (move == null);
		turn.setAttack(move);

		if (!targets[0].isIncapacitated() && targets[1].isIncapacitated()) {
			turn.setTarget(targets[0]);
		} else if (targets[0].isIncapacitated() && !targets[1].isIncapacitated()) {
			turn.setTarget(targets[1]);
		} else {
			int targetIdx = ( IBattlable.rand.nextInt(10) < 5 ) ? 0 : 1;
			turn.setTarget(targets[targetIdx]);
		}

		plannedTurn = true;
		return turn;
	}

	/**
	 * Deals damage to <code>this</code> monster with <code>move</code> used by <code>attacker</code>
	 * via the Damage Equation: <code>damage = attacker.stats.attack() / this.stats.defense() * move.power()</code>
	 *
	 * @param attacker opposing monster
	 * @param move     the incoming attack
	 * @return True if this monster is Incapacitated - i.e. <code>this.HP == 0 || this.justCaptured</code> - by this attack, false otherwise
	 */
	@Override
	public boolean attackedBy(IBattlable attacker, Move move) {
		if (move == Move.capture) {
			return this.capture(attacker);
		} else {
			return super.attackedBy(attacker, move);
		}
	}

	/**
	 * @return True if this character is capturable, false otherwise
	 */
	@Override
	public boolean isCapturable() {
		return true;
	}

	/**
	 * @return True if this IBattlable was captured in the current battle; false otherwise
	 */
	@Override
	public boolean wasJustCaptured() {
		return this.justCaptured;
	}

	/** Flags this <code>IBattlable</code> as captured in the current battle */
	@Override
	public void justCaptured() {
		this.justCaptured = true;
	}

	/** Flags this <code>IBattlable</code> as captured in a previous battle */
	@Override
	public void clearJustCaptured() {
		this.justCaptured = false;
	}

	/**
	 * @return True if this IBattlable can't fight for any reason(ex. it's KOed, captued, etc.); false otherwise
	 */
	@Override
	public boolean isIncapacitated() {
		return this.isKOed() || this.wasJustCaptured();
	}

	/**
	 * Creates and returns a copy of this object.  The precise meaning
	 * of "copy" may depend on the class of the object. The general
	 * intent is that, for any object {@code x}, the expression:
	 * <blockquote>
	 * <pre>
	 * x.clone() != x</pre></blockquote>
	 * will be true, and that the expression:
	 * <blockquote>
	 * <pre>
	 * x.clone().getClass() == x.getClass()</pre></blockquote>
	 * will be {@code true}, but these are not absolute requirements.
	 * While it is typically the case that:
	 * <blockquote>
	 * <pre>
	 * x.clone().equals(x)</pre></blockquote>
	 * will be {@code true}, this is not an absolute requirement.
	 * <p>
	 * By convention, the returned object should be obtained by calling
	 * {@code super.clone}.  If a class and all of its superclasses (except
	 * {@code Object}) obey this convention, it will be the case that
	 * {@code x.clone().getClass() == x.getClass()}.
	 * <p>
	 * By convention, the object returned by this method should be independent
	 * of this object (which is being cloned).  To achieve this independence,
	 * it may be necessary to modify one or more fields of the object returned
	 * by {@code super.clone} before returning it.  Typically, this means
	 * copying any mutable objects that comprise the internal "deep structure"
	 * of the object being cloned and replacing the references to these
	 * objects with references to the copies.  If a class contains only
	 * primitive fields or references to immutable objects, then it is usually
	 * the case that no fields in the object returned by {@code super.clone}
	 * need to be modified.
	 * <p>
	 * The method {@code clone} for class {@code Object} performs a
	 * specific cloning operation. First, if the class of this object does
	 * not implement the interface {@code Cloneable}, then a
	 * {@code CloneNotSupportedException} is thrown. Note that all arrays
	 * are considered to implement the interface {@code Cloneable} and that
	 * the return type of the {@code clone} method of an array type {@code T[]}
	 * is {@code T[]} where T is any reference or primitive type.
	 * Otherwise, this method creates a new instance of the class of this
	 * object and initializes all its fields with exactly the contents of
	 * the corresponding fields of this object, as if by assignment; the
	 * contents of the fields are not themselves cloned. Thus, this method
	 * performs a "shallow copy" of this object, not a "deep copy" operation.
	 * <p>
	 * The class {@code Object} does not itself implement the interface
	 * {@code Cloneable}, so calling the {@code clone} method on an object
	 * whose class is {@code Object} will result in throwing an
	 * exception at run time.
	 *
	 * @return a clone of this instance.
	 * @throws CloneNotSupportedException if the object's class does not
	 *                                    support the {@code Cloneable} interface. Subclasses
	 *                                    that override the {@code clone} method can also
	 *                                    throw this exception to indicate that an instance cannot
	 *                                    be cloned.
	 * @see Cloneable
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		CapturableActor clone = (CapturableActor) super.clone();
		clone.species = this.getSpecies();
		clone.stats = this.getStats();
		clone.level = this.getLevel();
		clone.learnedMoves = this.getLearnedMoves();
		clone.name = this.getName()+" 2";
		clone.HP = clone.stats.maxHP();
		clone.turn = new Turn(clone, null, null);

		return clone;
	}
}
