package game.monster;

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

	public int getMaxHP() {
		return maxHP;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getSpeed() {
		return speed;
	}
}
