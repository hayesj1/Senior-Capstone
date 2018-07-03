package game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class Label extends CapstoneComponent {
	public static final Color DEFAULT_TEXT_COLOR = Color.black;

	private String str;
	private Color textColor;

	protected Label(Object textSrc, GUIContext container, int x, int y) {
		this(textSrc, container, x, y, 0, 0);
	}
	public Label(Object textSrc, GUIContext container, int x, int y, int width, int height) {
		this(textSrc, container, x, y, width, height, null);
	}
	public Label(Object textSrc, GUIContext container, int x, int y, int width, int height, Color backgroundColor) {
		super(container, x, y, width, height, backgroundColor);

		this.str = textSrc.toString();
		this.textColor = DEFAULT_TEXT_COLOR;
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
		Color oldC = g.getColor();

		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
		Font f = g.getFont();
		int tw = f.getWidth(str);
		int th = f.getHeight(str);
		int tx = x + ((width - tw) / 2);
		int ty = y + ((height - th) / 2);
		g.getFont().drawString(tx, ty, str, textColor);

		g.setColor(oldC);
	}

	public void setText(Object textSrc) {
		this.str = textSrc.toString();
	}

	public void setTextColor(Color color) {
		this.textColor = color;
	}
}
