package game.util;

import game.SuperDungeoneer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.gui.GUIContext;

/**
 * Helper methods for drawing as well as color and component constants
 */
public class DrawingUtils {
	public static final int DEFAULT_MARGIN = SuperDungeoneer.COMPONENT_SPACING;

	public static final Color DEFAULT_TEXT_COLOR = Color.darkGray;
	public static final Color DEFAULT_FOREGROUND_COLOR = Color.lightGray;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.transparent;
	public static final Color DEFAULT_BASE_COLOR = Color.darkGray;

	public static final Color TEXT_COLOR = Color.white;
	public static final Color BUTTON_COLOR = Color.blue.addToCopy(Color.white.scaleCopy(0.125f)).darker(0.25f);
	public static final Color FOREGROUND_COLOR = Color.white;
	public static final Color TIER1_BACKGROUND_COLOR = Color.blue.addToCopy(Color.white.scaleCopy(0.25f));
	public static final Color TIER2_BACKGROUND_COLOR = Color.blue.addToCopy(Color.white.scaleCopy(0.25f));
	public static final Color TIER3_BACKGROUND_COLOR = Color.transparent;
	public static final Color BASE_COLOR = Color.gray;

	/**
	 * Draws a red X over the bounding rect of this component
	 * @param container the GUIContext
	 * @param g the Graphics object to draw on
	 *
	 * @see game.gui.BaseComponent#drawDisabledOverlay(GUIContext, Graphics)
	 */
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

	public static GradientFill getHPGradient(float startX, float startY, float endX, float endY) {
		return new GradientFill(startX, startY, Color.red, endX, endY, Color.green, false);
	}
}
