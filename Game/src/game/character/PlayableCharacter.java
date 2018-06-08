package game.character;

import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;

public class PlayableCharacter extends Character implements IBattlable{

	protected Move[] learnedMoves;
	protected Stats stats;
	protected int HP;

	public PlayableCharacter(Species species, Stats stats, Move[] learnedMoves, String name) {
		super(species, name);
		this.stats = stats;
		this.learnedMoves = learnedMoves;
	}

	/**
	 * @return True if this character should be capturable, false otherwise
	 */
	@Override
	public boolean isCapturable() {
		return false;
	}

	/**
	 * <code>this</code> monster attempts to deal damage to <code>defender</code> with <code>move</code>. If the move
	 * lands, <code>defender.attackedBy(this, move)</code> is called. Otherwise the move misses. The chance to hit is
	 * <code>move.accuracy() / 100</code>.
	 * @param defender opposing monster
	 * @param move the incoming attack
	 * @return True if the attack lands, false otherwise
	 */
	@Override
	public boolean attack(IBattlable defender, Move move) throws MoveOutOfUsesException {
		if (!this.canUseMove(move)) { throw new MoveOutOfUsesException(move, this); }
		else if (move.accuracy() == 100) {
			defender.attackedBy(this, move);
			return true;
		} else if (move.accuracy() == 0) { return false; }
		else {
			int randNum = IBattlable.rand.nextInt(100) + 1;
			if (randNum <= move.accuracy()) {
				defender.attackedBy(this, move);
				return true;
			} else { return false; }
		}
	}

	/**
	 * Deals damage to <code>this</code> monster with <code>move</code> used by <code>attacker</code>
	 * via the Damage Equation: <code>damage = attacker.stats.attack() / this.stats.defense() * move.power()</code>
	 * @param attacker opposing monster
	 * @param move the incoming attack
	 * @return True if this monster is KO-ed - i.e. <code>this.HP == 0</code> - by this attack, false otherwise
	 */
	@Override
	public boolean attackedBy(IBattlable attacker, Move move) {
		int damage = attacker.getStats().attack() / this.stats.defense() * move.power();
		this.modifyHP(-damage);
		this.validateHP();

		return this.HP == 0;
	}

	/**
	 * @param move the move in question
	 * @return True if <code>this</code> can use <code>move</code>, false otherwise
	 */
	@Override
	public boolean canUseMove(Move move) {
		return move.uses() > 0;
	}

	/**
	 * Ensures the the Characters HP is within the range <code>0 <= HP <= this.stats.maxHP()</code>
	 */
	protected void validateHP() {
		if (this.HP < 0) {
			this.HP = 0;
		} else if (this.HP > this.stats.maxHP()) {
			this.HP = this.stats.maxHP();
		}
	}

	@Override
	public Stats getStats() {
		return this.stats;
	}

	@Override
	public int HP() {
		return this.HP;
	}

	@Override
	public int modifyHP(int delta) {
		this.HP += delta;
		this.validateHP();
		return this.HP;
	}

	@Override
	public void setHP(int hp) {
		this.HP = hp;
		this.validateHP();
	}
}
