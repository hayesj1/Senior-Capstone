package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import java.util.LinkedList;

/**
 * A Panel of GUI Elements. Allows drawing GUI Components to coordinates local to a specific segment of the screen
 */
public class Panel extends BaseComponent {
	private LinkedList<ComponentCoordinates<BaseComponent>> children;

	public Panel(GUIContext container, int x, int y, int width, int height) { this(container, x, y, width, height, DrawingUtils.DEFAULT_MARGIN, null, null); }
	public Panel(GUIContext container, int x, int y, int width, int height, int margin, Color foreground, Color background) {
		super(container, x, y, width, height, margin, foreground, background);
		this.children = new LinkedList<>();
	}

	/**
	 * Adds a new child to this panel.
	 * @param child The component to add
	 * @param x the panel-local x-coordinate of the pixel to use as the top left corner of this child
	 * @param y the panel-local y-coordinate of the pixel to use as the top left corner of this child
	 */
	public void addChild(BaseComponent child, int x, int y) {
		ComponentCoordinates<BaseComponent> tmp = new ComponentCoordinates<>(child, x ,y);
		children.add(tmp);
		tmp.invalidate();
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
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
		if (!shown) { return; }

		Rectangle oldClip = g.getClip();
		g.setClip(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());

		if (backgroundColor != null) {
			Color old = g.getColor();
			g.setColor(backgroundColor);
			g.fillRect(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());
			g.setColor(old);
		}

		for (ComponentCoordinates child : children) {
			child.render(container, g, x, y);
		}

		g.setClip(oldClip);
	}

	protected void invalidateChildren() {
		children.forEach(ComponentCoordinates::invalidate);
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (children != null && !children.isEmpty()) {
			invalidateChildren();
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
