package game.character;

public abstract class Character {

	protected Species species;
	protected String name;

	public Character(Species species, String name) {
		this.species = species;
		this.name = name;
	}

	public Species getSpecies() {
		return species;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
