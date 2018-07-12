package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;



/**
 * Extends {@link AbstractComponent} with enable/disable and shown/not-shown functionality.
 * Also provides a routine to display a simple disabled overlay over this component.
 */
public abstract class BaseComponent extends AbstractComponent implements IBaseComponent {
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
	public int getMargin() {
		return margin;
	}
	@Override
	public int getXWithMargin() {
		return this.getX() - this.margin;
	}
	@Override
	public int getYWithMargin() {
		return this.getY() - this.margin;
	}
	@Override
	public int getWidthWithMargin() {
		return this.getWidth() + this.margin;
	}
	@Override
	public int getHeightWithMargin() {
		return this.getHeight() + this.margin;
	}

	@Override
	public int getX() {
		return x;
	}
	@Override
	public int getY() {
		return y;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setMargin(int margin) {
		this.margin = margin;
	}
	@Override
	public void setX(int x) {
		this.x = x;
	}
	@Override
	public void setY(int y) {
		this.y = y;
	}
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	@Override
	public boolean isShown() {
		return this.shown;
	}
	@Override
	public Color getForegroundColor() {
		return foregroundColor;
	}
	@Override
	public Color getBackgroundColor() { return this.backgroundColor; }

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public void setShown(boolean shown) {
		this.shown = shown;
	}
	@Override
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	@Override
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
