package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;



/**
 * Extends {@link AbstractComponent} with enable/disable and shown/not-shown functionality.
 * Also provides a routine to display a simple disabled overlay.
 */
public abstract class BaseComponent extends AbstractComponent {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int margin;

	protected boolean enabled;
	protected boolean shown;
	protected Color backgroundColor;
	protected Color foregroundColor;

	protected BaseComponent(GUIContext container, int x, int y) { this(container, x, y, 0 ,0, DrawingUtils.DEFAULT_MARGIN); }
	public BaseComponent(GUIContext container, int x, int y, int width, int height, int margin) { this(container, x, y, width, height, margin, null, null); }
	public BaseComponent(GUIContext container, int x, int y, int width, int height, int margin, Color foregroundColor, Color backgroundColor) {
		super(container);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.margin = Math.max(margin, 0);

		this.enabled = true;
		this.shown = true;
		this.foregroundColor = foregroundColor == null ? DrawingUtils.DEFAULT_FOREGROUND_COLOR : foregroundColor;
		this.backgroundColor = backgroundColor == null ? DrawingUtils.DEFAULT_BACKGROUND_COLOR : backgroundColor;
	}

	/**
	 * Calls this at the end of your {@link #render(GUIContext, Graphics)} implementation to draw a disabled overlay if
	 * this component is disabled. Or just conditionally call {@link #drawDisabledOverlay(GUIContext, Graphics)} yourself
	 * @param container the GUIContext
	 * @param g the Graphics object to draw on
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
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
		return this.x - this.margin;
	}
	@Override
	public int getY() {
		return this.y - this.margin;
	}
	@Override
	public int getWidth() {
		return this.width + this.margin;
	}
	@Override
	public int getHeight() {
		return this.height + this.margin;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
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
	public Color getForegroundColor() {
		return foregroundColor;
	}
	public Color getBackgroundColor() { return this.backgroundColor; }

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setShown(boolean shown) {
		this.shown = shown;
	}
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	public void setBackgroundColor(Color c) {
		this.backgroundColor = c;
	}
}
