package game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.Iterator;
import java.util.LinkedList;

public class TextHistory extends CapstoneComponent {
	public static final Color DEFAULT_TEXT_COLOR = Color.black;

	private LinkedList<String> history;
	private Color textColor;

	protected TextHistory(GUIContext container, int x, int y) {
		this(container, x, y, 0, 0);
	}
	public TextHistory(GUIContext container, int x, int y, int width, int height) {
		this(container, x, y, width, height, null);
	}
	public TextHistory(GUIContext container, int x, int y, int width, int height, Color backgroundColor) {
		super(container, x, y, width, height, backgroundColor);

		this.history = new LinkedList<>();
		this.textColor = DEFAULT_TEXT_COLOR;
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
	 * @throws SlickException
	 */
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!this.shown) {
			return;
		}

		Color old = g.getColor();

		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
		Font f = g.getFont();
		int lineH = f.getLineHeight();
		int x = getX(), y = getY() + getHeight();
		Iterator<String> it = history.descendingIterator();
		while(it.hasNext() && y >= getY()) {
			String str = it.next();
			y -= f.getHeight(str);
			f.drawString(x, y, str, textColor);
		}

		g.setColor(old);
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
}
