package game.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class ComponentCoordinates<T extends AbstractComponent> {
	private T comp;
	private int x;
	private int y;

	private boolean invalid;

	public ComponentCoordinates(T comp, int x, int y) {
		this.x = x;
		this.y = y;
		this.comp = comp;

		this.invalid = true;

	}

	public void setLocation(int originX, int originY) {
		comp.setLocation(x + originX, y + originY);
	}

	public void render(GUIContext container, Graphics g, int originX, int originY) throws SlickException {
		if (invalid) {
			setLocation(originX, originY);
		}

		comp.render(container, g);
	}

	public T getComponent() {
		return comp;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public void setEnabled(boolean enabled) {
		if (comp instanceof CapstoneComponent) {
			((CapstoneComponent) comp).setEnabled(enabled);
		}
	}

	public void setShown(boolean shown) {
		if (comp instanceof CapstoneComponent) {
			((CapstoneComponent) comp).setShown(shown);
		}
	}

	public void invalidate() {
		this.invalid = true;
	}


}
