package game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public interface IBaseComponent {
	/**
	 * Calls this at the end of your {@link #render(GUIContext, Graphics)} implementation to draw a disabled overlay if
	 * this component is disabled. Or just conditionally call {@link #drawDisabledOverlay(GUIContext, Graphics)} yourself
	 * @param container the GUIContext
	 * @param g the Graphics object to draw on
	 */
	void render(GUIContext container, Graphics g);

	int getMargin();

	int getXWithMargin();

	int getYWithMargin();

	int getWidthWithMargin();

	int getHeightWithMargin();

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	void setMargin(int margin);

	void setX(int x);

	void setY(int y);

	void setWidth(int width);

	void setHeight(int height);

	void setLocation(int x, int y);

	boolean isEnabled();

	boolean isShown();

	Color getForegroundColor();

	Color getBackgroundColor();

	void setEnabled(boolean enabled);

	void setShown(boolean shown);

	void setForegroundColor(Color foregroundColor);

	void setBackgroundColor(Color backgroundColor);
}
