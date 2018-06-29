package game.character;

import game.battle.Turn;
import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;
import org.newdawn.slick.Animation;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class PlayableCharacter extends Character implements IBattlable {

	private Turn turn = new Turn(this, null, null);

	private final Move[] learnedMoves = new Move[MAX_MOVES];
	private int selectedSlot = -1;
	private int moveCount = 0;
	private boolean justCaptured = false;

	private Stats stats;
	private int HP;
	private Animation deathAnim;
	private boolean plannedTurn;

	public PlayableCharacter(Species species, String name, Move[] learnedMoves, Stats stats) { this(species, name, learnedMoves, stats, 1); }
	public PlayableCharacter(Species species, String name, Move[] learnedMoves, Stats stats, int level) {
		super(species, name, level);
		this.stats = stats;
		for (int i = 0, j = 0; i < learnedMoves.length && moveCount <= MAX_MOVES; i++) {
			if (learnedMoves[i] == null) { continue; }
			this.learnedMoves[moveCount] = learnedMoves[i];
			moveCount++;
		}
		this.HP = this.stats.maxHP();
		this.plannedTurn = false;
	}

	/**
	 * @return True if this character is be capturable, false otherwise
	 */
	@Override
	public boolean isCapturable() {
		return false;
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

	public void setSelectedMove(int slot) {
		if (learnedMoves[slot] == null) {
			return;
		}
		this.selectedSlot = slot;
	}

	/**
	 * This <code>IBattlable</code> will select a move to use on its upcoming turn
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	@Override
	public Turn planMove(IBattlable[] targets) {
		if (plannedTurn) { return turn; }
		this.turn.setTarget(null);
		this.turn.setAttack(null);

		if (selectedSlot <= 0) {
			return this.turn;
		}
		this.turn.setAttack(learnedMoves[selectedSlot-1]);
		selectedSlot = -1;
		//TODO Implement target selection
		if (!(targets[0].isKOed() || targets[0].wasJustCaptured()) && (targets[1].isKOed() || targets[1].wasJustCaptured())) {
			this.turn.setTarget(targets[0]);
		} else if ((targets[0].isKOed() || targets[0].wasJustCaptured()) && !(targets[1].isKOed() || targets[1].wasJustCaptured())) {
			this.turn.setTarget(targets[1]);
		}
		plannedTurn = true;
		return this.turn;
	}

	/**
	 * This <code>IBattlable</code> will select the move in moveSLot to use on its upcoming turn
	 * @param moveSlot slot# of the selected move; values are 1 through 6
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	@Override
	public Turn planMove(int moveSlot, IBattlable[] targets) {
		this.selectedSlot = moveSlot;
		return this.planMove(targets);
	}

	/**
	 * Execute the turn saved by a previous call to {@link #planMove(IBattlable[])}
	 * @return true if the move lands, false if it missed, if it is out of uses, or if there is some other failure
	 */
	@Override
	public boolean executeTurn() {
		try {
			boolean ret = this.turn.execute();
			this.plannedTurn = false;
			return ret;
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
				this.addEXP(ILevelable.expForDefeating(this.getLevel(), ((ILevelable) defender).getLevel()));
			}
			Logger.getLogger(PlayableCharacter.class.getName()).log(Level.INFO, "Hit with move "+move.getName());
			return true;
		} else if (move.accuracy() == 0) {
			Logger.getLogger(PlayableCharacter.class.getName()).log(Level.INFO, "Miss with move "+move.getName());
			return false;
		}
		else {
			int randNum = IBattlable.rand.nextInt(100) + 1;
			if (randNum <= move.accuracy()) {
				if (defender.attackedBy(this, move)) {
					defender.KO();
				}
				Logger.getLogger(PlayableCharacter.class.getName()).log(Level.INFO, "Hit with move "+move.getName());
				return true;
			} else {
				Logger.getLogger(PlayableCharacter.class.getName()).log(Level.INFO, "Miss with move "+move.getName());
				return false;
			}
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
		int damage = (attacker.getStats().attack() / this.stats.defense() + 1) * move.power();
		this.modifyHP(-damage);
		this.validateHP();

		Logger.getLogger(PlayableCharacter.class.getName()).log(Level.INFO, "Hit by move "+move.getName()+" dealing "+damage+" damage and leaving "+HP()+"HP!");
		return this.HP == 0;
	}

	/**
	 * Render KO animation
	 */
	@Override
	public void KO() {
		deathAnim = new Animation(this.species.getSpriteSheet(), 6,0, 8,0,true, 1000, true);
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

	@Override
	public Move[] getLearnedMoves() { return learnedMoves; }

	@Override
	public int getMoveSlotByLearnedMoveName(String name) {
		int slot = -1;
		for (int i = 0; i < moveCount; i++) {
			if (learnedMoves[i].getName().equalsIgnoreCase(name)) {
				slot = i+1;
				break;
			}
		}

		return slot;
	}

	@Override
	public int getMoveCount() { return this.moveCount; }

	public Animation getDeathAnimation() { return this.deathAnim; }
}
