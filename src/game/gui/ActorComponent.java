package game.gui;

import game.SuperDungeoneer;
import game.character.Actor;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public class ActorComponent extends BaseComponent {
	private Actor actor;
	private Animation anim;

	public ActorComponent(GUIContext container, Actor a, int x, int y, int spriteLength) {
		super(container, x, y, spriteLength, spriteLength, 0, null, null);

		this.actor = a;
		this.anim = new Animation(this.actor.getSpecies().getSpriteSheet(), 0, 1, 3, 1, true, 5, false);
		SuperDungeoneer.getInstance().addAnim(anim);
	}

	/**
	 * Calls this at the end of your {@link #render(GUIContext, Graphics)} implementation to draw a disabled overlay if
	 * this component is disabled. Or just conditionally call {@link #drawDisabledOverlay(GUIContext, Graphics)} yourself
	 *
	 * @param container the GUIContext
	 * @param g         the Graphics object to draw on
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
		if (actor == null) { System.out.println("actor == null"); return; }
		anim.draw(getX(), getY());
		//anim.start();
		//actor.getSpecies().getSpriteSheet().getSprite(0,1).draw(this.getX(), this.getY());
	}

	public Actor getActor() { return this.actor; }
	public void setActor(Actor actor) {
		this.actor = actor;
	}
}
