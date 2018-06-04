package game.monster;

import game.monster.moves.Move;

public class Monster {

	private Species species;
	private Stats stats;

	private int hp;
	private Move[] learnedMoves;

	public boolean isCapturable() { return true; }

	public boolean attackedBy(Move mv, Monster other) {
		int damage = other.stats.getAttack() / this.stats.getDefense() * mv.getPower();
		this.hp -= damage;

		if (this.hp <= 0) {
			this.hp = 0;
			return true;
		}

		return false;
	}
}
