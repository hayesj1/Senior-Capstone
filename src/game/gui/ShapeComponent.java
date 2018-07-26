package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.GUIContext;

/** Component responsible for rendering various shapes, with an optional fill */
public class ShapeComponent extends BaseComponent {
	private Shape shape;
	private ShapeFill fill;
	private boolean filled;

	protected ShapeComponent(GUIContext container, Shape shape, boolean filled) {
		this(container, shape, filled, DrawingUtils.DEFAULT_MARGIN, null);
	}
	public ShapeComponent(GUIContext container, Shape shape, boolean filled, int margin, Color foregroundColor) {
		this(container, shape, filled, margin, foregroundColor, null);
	}
	public ShapeComponent(GUIContext container, Shape shape, boolean filled, int margin, Color foregroundColor, Color backgroundColor) {
		super(container, (int) shape.getX(), (int) shape.getY(), (int) shape.getWidth(), (int) shape.getHeight(), margin, foregroundColor, backgroundColor);
		this.shape = shape;
		this.filled = filled;
		this.drawBorder = false;
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
		Color oldC = g.getColor();
		Rectangle oldClip = g.getClip();
		g.setClip(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());

		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
		}

		this.drawBorder(container, g);

		if (foregroundColor != null) {
			g.setColor(foregroundColor);
		} else {
			g.setColor(oldC);
		}

		if (filled) {
			if (fill != null) {
				g.fill(shape, fill);
			} else {
				g.fill(shape);
			}
		} else {
			g.draw(shape);
		}

		g.setClip(oldClip);
		g.setColor(oldC);
	}

	public void setFill(ShapeFill fill) {
		this.fill = fill;
	}

	public float getShapeX() { return this.shape.getX(); }
	public float getShapeY() { return this.shape.getY(); }
	public float getShapeWidth() { return this.shape.getWidth(); }
	public float getShapeHeight() { return this.shape.getHeight(); }

	@Override
	public int getXWithMargin() {
		return Math.round(this.getShapeX() - this.margin);
	}
	@Override
	public int getYWithMargin() {
		return Math.round(this.getShapeY() - this.margin);
	}
	@Override
	public int getWidthWithMargin() {
		return Math.round(this.getShapeWidth() + this.margin);	}
	@Override
	public int getHeightWithMargin() {
		return Math.round(this.getShapeHeight() + this.margin);	}
}
