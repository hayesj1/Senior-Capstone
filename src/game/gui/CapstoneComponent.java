package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * Extends {@link AbstractComponent} with enable/disable and shown/not-shown functionality.
 * Also provides a routine to display a simple disabled overlay.
 */
public abstract class CapstoneComponent extends AbstractComponent {
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	protected boolean enabled;
	protected boolean shown;
	protected Color backgroundColor;

	protected CapstoneComponent(GUIContext container, int x, int y) { this(container, x, y, 0 ,0); }
	public CapstoneComponent(GUIContext container, int x, int y, int width, int height) { this(container, x, y, width, height, null); }
	public CapstoneComponent(GUIContext container, int x, int y, int width, int height, Color backgroundColor) {
		super(container);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.enabled = true;
		this.shown = true;
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Calls this at the end of your {@link #render(GUIContext, Graphics)} implementation to draw a disabled overlay if
	 * this component is disabled. Or just conditionally call {@link #drawDisabledOverlay(GUIContext, Graphics)} yourself
	 * @param container the GUIContext
	 * @param g the Graphics object to draw on
	 * @throws SlickException
	 */
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!shown) { return; }
		if (!enabled) {
			drawDisabledOverlay(container, g);
		}
	}

	/**
	 * Draws an overlay of red Xs over the bounding rect of this component
	 * @param container the GUIcontext
	 * @param g the Graphics oject to draw on
	 * @see #render(GUIContext, Graphics)
	 */
	protected void drawDisabledOverlay(GUIContext container, Graphics g) {
		DrawingUtils.drawDisabledOverlay(container, g, x, y, width, height);
	}

	@Override
	public int getX() {
		return this.x;
	}
	@Override
	public int getY() {
		return this.y;
	}
	@Override
	public int getWidth() {
		return this.width;
	}
	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
	public boolean isShown() {
		return this.shown;
	}
	public Color getBackgroundColor() { return this.backgroundColor; }

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setShown(boolean shown) {
		this.shown = shown;
	}
	public void setBackgroundColor(Color c) {
		this.backgroundColor = c;
	}
}
