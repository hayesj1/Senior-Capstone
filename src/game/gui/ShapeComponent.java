package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.GUIContext;

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
		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
		}

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

		g.setColor(oldC);
	}

	public void setFill(ShapeFill fill) {
		this.fill = fill;
	}

	public float getShapeWidth() { return this.shape.getWidth(); }
	public float getShapeHeight() { return this.shape.getHeight(); }

}
