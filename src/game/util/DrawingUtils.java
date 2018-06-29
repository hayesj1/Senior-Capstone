package game.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public class DrawingUtils {
	public static void drawDisabledOverlay(GUIContext container, Graphics g, int x, int y, int width, int height) {
		Color oldC = g.getColor();
		float oldLW = g.getLineWidth();

		g.setColor(Color.red);
		g.setLineWidth(3.0f);
		g.drawRect(x, y, width, height);
		g.setLineWidth(2.0f);
		g.drawLine(x, y, x+width, y+height);
		g.drawLine(x+width, y, x, y+height);

		g.setLineWidth(oldLW);
		g.setColor(oldC);
	}
}
