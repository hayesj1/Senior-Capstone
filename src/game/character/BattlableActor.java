package game.character;

import game.SuperDungeoneer;
import game.battle.Turn;
import game.character.moves.Move;
import game.exception.MoveOutOfUsesException;
import org.newdawn.slick.Animation;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BattlableActor extends Actor implements IBattlable {
	protected int HP;
	protected int level;
	protected Stats stats;

	protected Move[] learnedMoves = new Move[MAX_MOVES];
	protected int moveCount = 0;
	protected int selectedSlot = -1;

	protected boolean plannedTurn;
	protected Turn turn = new Turn(this, null, null);

	protected Animation deathAnim;

	public BattlableActor(Species species, String name, Stats stats, Move[] learnedMoves) { this(species, name, stats, 1, learnedMoves); }
	public BattlableActor(Species species, String name, Stats stats, int level, Move[] learnedMoves) {
		super(species, name);

		this.HP = stats.maxHP();
		this.level = Math.max(Math.min(level, ILevelable.MAX_LEVEL), 1);
		this.stats = stats;

		if (learnedMoves != null) {
			for (int i = 0; i < learnedMoves.length && moveCount < MAX_MOVES; i++) {
				if (learnedMoves[i] != null) {
					this.learnedMoves[moveCount] = learnedMoves[i];
					moveCount++;
				}
			}
		}

		this.plannedTurn = false;
	}

	/**
	 * This <code>BattlableActor</code> will select a move to use on its upcoming turn
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	@Override
	public Turn planMove(IBattlable[] targets) {
		if (plannedTurn) { return turn; }

		if (selectedSlot <= 0) {
			return turn;
		}
		Move move = learnedMoves[selectedSlot-1];
		if (move == null) {
			return turn;
		}
		turn.setAttack(move);

		selectedSlot = -1;
		if (!targets[0].isIncapacitated() && targets[1].isIncapacitated()) {
			turn.setTarget(targets[0]);
		} else if (targets[0].isIncapacitated() && !targets[1].isIncapacitated()) {
			turn.setTarget(targets[1]);
		}
		plannedTurn = true;
		return turn;
	}

	/**
	 * This <code>BattlableActor</code> will select the move in moveSLot to use on its upcoming turn
	 * @param moveSlot slot# of the selected move; values are 1 through 6
	 * @param targets the available targets for a move
	 * @return a <code>Turn</code> instance for later execution of the move chosen
	 */
	@Override
	public Turn planMove(int moveSlot, IBattlable[] targets) {
		selectedSlot = moveSlot;
		return this.planMove(targets);
	}

	/**
	 * Execute the turn saved by a previous call to {@link #planMove(IBattlable[])}
	 *
	 * @return true if the move lands, false if it missed, if it is out of uses, or if there is some other failure
	 */
	@Override
	public boolean executeTurn() {
		if (this.isIncapacitated()) { return false; }
		try {
			//System.out.println(this.turn.toString()+" | "+this.plannedTurn);
			boolean ret = this.turn.execute();
			plannedTurn = false;
			return ret;
		} catch (MoveOutOfUsesException e) {
			System.out.println(e.getMessage());
			plannedTurn = false;
			return false;
		}
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
		if (!this.canUseMove(move)) { throw new MoveOutOfUsesException(move, this); }
		else if (move.accuracy() == 100) {
			if (defender.attackedBy(this, move)) {
				defender.KO();
				SuperDungeoneer.getInstance().addFeedback(this.getName()+" KO-ed "+defender.getName()+"!");
			}
			SuperDungeoneer.getInstance().addFeedback(this.getName()+" used a move with 100% Accuracy!");
			Logger.getLogger(CapturableActor.class.getName()).log(Level.ALL, "Hit with move "+move.getName());
			return true;
		} else if (move.accuracy() == 0) {
			SuperDungeoneer.getInstance().addFeedback(this.getName()+" used a move with 0% Accuracy!");
			Logger.getLogger(CapturableActor.class.getName()).log(Level.ALL, "Miss with move "+move.getName());
			return false;
		}
		else {
			int randNum = IBattlable.rand.nextInt(100) + 1;
			if (randNum <= move.accuracy()) {
				if (defender.attackedBy(this, move)) {
					defender.KO();
					SuperDungeoneer.getInstance().addFeedback(this.getName()+" KO-ed "+defender.getName()+"!");
				}
				Logger.getLogger(CapturableActor.class.getName()).log(Level.ALL, "Hit with move "+move.getName());
				return true;
			} else {
				SuperDungeoneer.getInstance().addFeedback(this.getName()+" MISSED "+defender.getName()+"!");
				Logger.getLogger(CapturableActor.class.getName()).log(Level.ALL, "Miss with move "+move.getName());
				return false;
			}
		}

	}

	/**
	 * Deals damage to <code>this</code> monster with <code>move</code> used by <code>attacker</code>
	 * via the Damage Equation: <code>damage = attacker.stats.attack() / this.stats.defense() * move.power()</code>
	 *
	 * @param attacker opposing monster
	 * @param move     the incoming attack
	 * @return True if this monster is KO-ed - i.e. <code>this.HP == 0</code> - by this attack, false otherwise
	 */
	@Override
	public boolean attackedBy(IBattlable attacker, Move move) {
		int damage = (attacker.getStats().attack() / this.stats.defense() + 1) * move.power();
		this.modifyHP(-damage);
		this.validateHP();

		SuperDungeoneer.getInstance().addFeedback(this.getName()+" HIT by "+attacker.getName()+", dealing "+damage+" DAMAGE!");
		Logger.getLogger(CapturableActor.class.getName()).log(Level.ALL, "Hit by move "+move.getName()+" dealing "+damage+" damage and leaving "+HP()+"HP!");
		return this.isKOed();

	}

	@Override
	public int getMoveSlotByMoveName(String name) {
		int slot = -1;
		for (int i = 0; i < moveCount; i++) {
			if (learnedMoves[i].getName().equalsIgnoreCase(name)) {
				slot = i+1;
				break;
			}
		}

		return slot;
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
	public int modifyHP(int delta) {
		this.HP += delta;
		this.validateHP();
		return this.HP;
	}
	@Override
	public int HP() {
		return this.HP;
	}
	@Override
	public void setHP(int hp) {
		this.HP = hp;
		this.validateHP();
	}
	@Override
	public Stats getStats() {
		return this.stats;
	}

	@Override
	public Move[] getLearnedMoves() { return learnedMoves; }
	@Override
	public int getMoveCount() { return this.moveCount; }
	public void setSelectedMove(int slot) {
		if (learnedMoves[slot-1] == null) {
			return;
		}
		this.selectedSlot = slot;
	}

	public Animation getDeathAnimation() { return this.deathAnim; }

	@Override
	public int getLevel() {
		return this.level;
	}

	public Turn getTurn() { return this.turn; }
}
