package game.character.moves;

public class Move {
	private String name;
	private String description;
	private int power;
	private int accuracy;
	private int maxUses;
	private int uses;

	public Move(String name, String description, int power, int accuracy, int maxUses) {
		this.name = name;
		this.description = description;
		this.power = power;
		this.accuracy = accuracy;
		this.maxUses = maxUses;
		this.uses = maxUses;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int power() {
		return power;
	}

	public int accuracy() {
		return accuracy;
	}

	public int maxUses() {
		return maxUses;
	}

	public int uses() {
		return uses;
	}

	public int modifyUses(int delta) {
		this.uses += delta;
		this.validateUses();
		return this.uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
		this.validateUses();
	}

	private void validateUses() {
		if (this.uses < 0) {
			this.uses = 0;
		} else if (this.uses > this.maxUses) {
			this.uses = this.maxUses;
		}
	}
}
