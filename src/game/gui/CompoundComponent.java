package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import java.util.Collections;
import java.util.LinkedList;

public class CompoundComponent extends Panel {
	protected LinkedList<ComponentCoordinates<IBaseComponent>> components;

	public CompoundComponent(GUIContext container, int x, int y, int width, int height) {
		this(container, x, y, width, height, DrawingUtils.DEFAULT_MARGIN, null, null);
	}
	public CompoundComponent(GUIContext container, int x, int y, int width, int height, int margin, Color foreground, Color background) {
		super(container, x, y, width, height, margin, foreground, background);

		this.components = new LinkedList<>();
		this.drawBorder = false;
	}
	@SafeVarargs
	public CompoundComponent(GUIContext container, int x, int y, int width, int height, ComponentCoordinates<IBaseComponent>... components) {
		this(container, x, y, width, height, DrawingUtils.DEFAULT_MARGIN, null, null, components);
	}
	@SafeVarargs
	public CompoundComponent(GUIContext container, int x, int y, int width, int height, int margin, Color foreground, Color background, ComponentCoordinates<IBaseComponent>... components) {
		super(container, x, y, width, height, margin, foreground, background);

		this.components = new LinkedList<>();
		if (components != null) {
			Collections.addAll(this.components, components);
		}
		this.drawBorder = false;
	}

	/**
	 * Adds a new child to this panel.
	 *
	 * @param child The component to add
	 * @param x     the panel-local x-coordinate of the pixel to use as the top left corner of this child
	 * @param y     the panel-local y-coordinate of the pixel to use as the top left corner of this child
	 */
	@Override
	public void addChild(IBaseComponent child, int x, int y) {
		ComponentCoordinates<IBaseComponent> tmp = new ComponentCoordinates<>(child, x, y);
		components.add(tmp);
		tmp.invalidate();
	}

	/**
	 * Remove all children from this panel
	 */
	@Override
	public void clearChildren() {
		components.clear();
	}

	/**
	 * Renders the background (if one is set) and the children of this panel
	 *
	 * @param container the GUIContext
	 * @param g         the Graphics object to draw on
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
		if (!shown) { return; }

		Rectangle oldClip = g.getClip();
		g.setClip(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());
		this.drawBorder(container, g);
		for (ComponentCoordinates<IBaseComponent> comp : components) {
			comp.render(container, g, x, y);
			if (!enabled) {
				DrawingUtils.drawDisabledOverlay(container, g, comp.getComponent().getXWithMargin(), comp.getComponent().getYWithMargin(), comp.getComponent().getWidthWithMargin(), comp.getComponent().getHeightWithMargin());
			}
		}

		g.setClip(oldClip);
	}

	@Override
	protected void invalidateChildren() {
		components.forEach(ComponentCoordinates::invalidate);
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (components != null && !components.isEmpty()) {
			invalidateChildren();
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		components.forEach(component -> component.setEnabled(enabled));
	}

	@Override
	public void setShown(boolean shown) {
		super.setShown(shown);
		components.forEach(component -> component.setShown(shown));
	}
}
