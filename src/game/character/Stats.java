package game.character;

/**
 * The base stats for a given BattlableActor.
 * These are increased overtime by spending stat-points, which are awarded from leveling up (NYI).
 */
public class Stats {

	private int maxHP;
	private int attack;
	private int defense;
	private int speed;

	public Stats(int maxHP, int attack, int defense, int speed) {
		this.maxHP = maxHP;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
	}

	public int maxHP() {
		return maxHP;
	}
	public int attack() {
		return attack;
	}
	public int defense() {
		return defense;
	}
	public int speed() {
		return speed;
	}
}
