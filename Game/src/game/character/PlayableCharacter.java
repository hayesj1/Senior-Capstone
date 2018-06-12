package game.character;

import game.battle.Turn;
import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;

public class PlayableCharacter extends Character implements IBattlable, ILevelable {
	public static final int MAX_MOVES = 6;
	private final Move[] learnedMoves = new Move[MAX_MOVES];
	private int moveCount = 0;
	private Turn turn = new Turn(this, null, null);

	private Stats stats;
	private int HP;
	private int level;
	private int exp;
	private int requiredExp;

	public PlayableCharacter(Species species, String name, Move[] learnedMoves, Stats stats) { this(species, name, learnedMoves, stats, 1); }
	public PlayableCharacter(Species species, String name, Move[] learnedMoves, Stats stats, int level) {
		super(species, name);
		this.stats = stats;
		for (int i = 0, j = 0; i < learnedMoves.length && moveCount <= MAX_MOVES; i++) {
			if (learnedMoves[i] == null) { continue; }
			this.learnedMoves[j] = learnedMoves[i];
			moveCount++;
		}
		this.level = Math.max(Math.min(level, ILevelable.MAX_LEVEL), 1);
		this.exp =  ILevelable.requiredExpForLevel(this.level);
		this.requiredExp = ILevelable.requiredExpForLevel(this.level+1);
	}

	/**
	 * @return True if this character should be capturable, false otherwise
	 */
	@Override
	public boolean isCapturable() {
		return false;
	}

	@Override
	public Turn planMove(IBattlable[] targets) {
		//TODO Implement move Selection
		this.turn.setTarget(targets[0]);
		this.turn.setAttack(learnedMoves[0]);
		return this.turn;
	}

	@Override
	public boolean executeTurn() {
		try {
			return this.turn.execute();
		} catch (MoveOutOfUsesException e) {
			System.out.println(e.getMessage());
			return false;
		}
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
			if (defender.attackedBy(this, move)) {
				defender.KO();
			}
			return true;
		} else if (move.accuracy() == 0) { return false; }
		else {
			int randNum = IBattlable.rand.nextInt(100) + 1;
			if (randNum <= move.accuracy()) {
				if (defender.attackedBy(this, move)) {
					defender.KO();
				}
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

	@Override
	public void KO() {

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

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int levelUp() {
		return ++this.level;
	}

	@Override
	public void addEXP(int exp) {
		this.exp += exp;
		if (this.requiredExp > 0 && this.exp >= this.requiredExp) {

		}
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
			Move old = learnedMoves[slot];
			learnedMoves[slot] = newMove;
			return old;
		} else {
			learnedMoves[moveCount++] = newMove;
			return null;
		}
	}
}
