package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import java.util.Iterator;
import java.util.LinkedList;

public class TextHistory extends BaseComponent {

	private LinkedList<String> history;
	private Color textColor;

	protected TextHistory(GUIContext container, int x, int y) {
		this(container, x, y, 0, 0, DrawingUtils.DEFAULT_MARGIN, null);
	}
	public TextHistory(GUIContext container, int x, int y, int width, int height, int margin, Color textColor) {
		this(container, x, y, width, height, margin, textColor, null, null);
	}
	public TextHistory(GUIContext container, int x, int y, int width, int height, int margin, Color textColor, Color foregroundColor, Color backgroundColor) {
		super(container, x, y, width, height, margin, foregroundColor, backgroundColor);

		this.history = new LinkedList<>();
		this.textColor = textColor == null ? DrawingUtils.DEFAULT_TEXT_COLOR : textColor;
	}

	public void addLine(String line) {
		history.addLast(line);
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
		if (!this.shown) {
			return;
		}

		Color old = g.getColor();
		Rectangle oldClip = g.getClip();
		g.setClip(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());

		g.setColor(backgroundColor);
		g.fillRect(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());
		Font f = g.getFont();
		int lineH = f.getLineHeight();
		int x = getXWithMargin(), y = getYWithMargin() + getHeightWithMargin();
		Iterator<String> it = history.descendingIterator();
		while(it.hasNext() && y >= getYWithMargin()) {
			String str = it.next();
			y -= f.getHeight(str);
			f.drawString(x, y, str, textColor);
		}

		g.setClip(oldClip);
		g.setColor(old);
	}

	public Color getTextColor() {
		return textColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
}
