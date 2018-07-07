package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class SolidFill implements ShapeFill {
	private Color fill;
	private boolean local;

	public SolidFill(Color color) { this(color, false); }
	public SolidFill(Color fill, boolean local) {
		this.fill = fill;
		this.local = local;
	}



	@Override
	public Color colorAt(Shape shape, float x, float y) {
		if (!local) {
			x -= shape.getCenterX();
			y -= shape.getCenterY();
		}
		return shape.contains(x, y) ? fill : DrawingUtils.DEFAULT_FOREGROUND_COLOR;
	}


	@Override
	public Vector2f getOffsetAt(Shape shape, float x, float y) {
		return new Vector2f(0, 0);
	}
}
