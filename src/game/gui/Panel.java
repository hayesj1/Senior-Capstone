package game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import java.util.LinkedList;

/**
 * A Panel of GUI Elements. Allows drawing GUI Components to coordinates local to a specific segment of the screen
 */
public class Panel extends CapstoneComponent {
	private LinkedList<ComponentCoordinates<AbstractComponent>> children;

	public Panel(GUIContext container, int x, int y, int width, int height) {
		super(container, x, y, width, height);
		this.children = new LinkedList<>();
	}

	/**
	 * Adds a new child to this panel.
	 * @param child The component to add
	 * @param x the panel-local x-coordinate of the pixel to use as the top left corner of this child
	 * @param y the panel-local y-coordinate of the pixel to use as the top left corner of this child
	 */
	public void addChild(AbstractComponent child, int x, int y) {
		children.add(new ComponentCoordinates<>(child, x ,y));
	}

	/**
	 * Remove all children from this panel
	 */
	public void clearChildren() {
		children.clear();
	}

	/**
	 * Renders the background (if one is set) and the children of this panel
	 * @param container the GUIContext
	 * @param g the Graphics object to draw on
	 * @throws SlickException
	 */
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!shown) { return; }
		if (backgroundColor != null) {
			Color old = g.getColor();
			g.setColor(backgroundColor);
			g.fillRect(x, y, width, height);
			g.setColor(old);
		}

		for (ComponentCoordinates child : children) {
			child.render(container, g, x, y);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		children.forEach(child -> child.setEnabled(enabled));
	}

	@Override
	public void setShown(boolean shown) {
		super.setShown(shown);
		children.forEach(child -> child.setShown(shown));
	}
}
