package game.monster.moves;

public class Move {
	private String name;
	private String description;
	private int power;
	private int accuracy;

	public Move(String name, String description, int power, int accuracy) {
		this.name = name;
		this.description = description;
		this.power = power;
		this.accuracy = accuracy;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getPower() {
		return power;
	}

	public int getAccuracy() {
		return accuracy;
	}
}
