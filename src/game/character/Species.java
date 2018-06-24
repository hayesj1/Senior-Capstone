package game.character;

import game.character.moves.MoveSet;
import org.newdawn.slick.SpriteSheet;

public abstract class Species {
	private MoveSet learnableMoves;
	private String name;

	private SpriteSheet spriteSheet;

	public Species(String name, MoveSet learnableMoves, SpriteSheet spriteSheet) {
		this.learnableMoves = learnableMoves;
		this.name = name;
		this.spriteSheet = spriteSheet;
	}

	public MoveSet getLearnableMoves() {
		return learnableMoves;
	}

	public String getName() {
		return name;
	}

	public SpriteSheet getSpriteSheet() {
		return this.spriteSheet;
	}
}
