package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public class Label extends BaseComponent {
	private String str;
	private Color textColor;
	private int cutoff;
	private Font font;
	private boolean fitStr;

	protected Label(GUIContext container, Object textSrc, int x, int y, boolean fitText) {
		this(container, textSrc, x, y, 0, 0, DrawingUtils.DEFAULT_MARGIN, fitText, null);
	}
	public Label(GUIContext container, Object textSrc, int x, int y, int width, int height, int margin, boolean fitText, Color textColor) {
		this(container, textSrc, x, y, width, height, margin, fitText, textColor, null, null);
	}
	public Label(GUIContext container, Object textSrc, int x, int y, int width, int height, int margin, boolean fitText, Color textColor, Color foregroundColor, Color backgroundColor) {
		super(container, x, y, width, height, margin, foregroundColor, backgroundColor);

		this.font = container.getDefaultFont();
		this.str = textSrc.toString();
		this.fitStr = fitText;
		this.textColor = textColor == null ? DrawingUtils.DEFAULT_TEXT_COLOR : textColor;
		if (fitText) {
			this.fitWidth();
		}
		this.cutoff = findCutoff();
	}

	public void fitWidth(){
		setWidth(font.getWidth("_"+str+"_"));
	}

	private int findCutoff() {
		int cutoff;
		int tw = font.getWidth(str);
		for (cutoff = str.length()-1; tw >= width && cutoff >= 0; cutoff--) { tw = font.getWidth(str.substring(0, cutoff +1)); }

		if (cutoff < 0) {
			cutoff = 0;
		}

		return cutoff;
	}

	/**
	 * Call this at the end of your {@link #render(GUIContext, Graphics)} implementation to draw a disabled overlay if
	 * this component is disabled. Or just conditionally call {@link #drawDisabledOverlay(GUIContext, Graphics)} yourself
	 *
	 * @param container the GUIContext
	 * @param g         the Graphics object to draw on
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
		if (!shown) { return; }
		if (!font.equals(g.getFont())) { font = g.getFont(); }

		if (fitStr) {
			fitWidth();
			setCutoff(findCutoff());
		} else if (width < font.getWidth(str)) {
			setCutoff(findCutoff());
		}
		Color oldC = g.getColor();

		g.setColor(foregroundColor);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 6);
		int tw = font.getWidth(str);
		int th = font.getHeight(str);
		int tx = getX() + ((getWidth() - tw) / 2);
		int ty = getY() + ((getHeight() - th) / 2);
		g.getFont().drawString(tx, ty, str, textColor, 0, cutoff);

		g.setColor(oldC);
	}

	@Override
	public int getWidth() {
		return this.font.getWidth(this.getText()) + this.margin;
	}
	@Override
	public int getHeight() {
		return this.font.getHeight(this.getText()) + this.margin;
	}
	public String getText() {
		return str;
	}

	public void setText(Object textSrc) {
		this.str = textSrc.toString();
	}
	public void setTextColor(Color color) {
		this.textColor = color;
	}
	public void setCutoff(int lastChar) {
		this.cutoff = lastChar;
	}

	/**
	 * Returns a string representation of the object. In general, the
	 * {@code toString} method returns a string that
	 * "textually represents" this object. The result should
	 * be a concise but informative representation that is easy for a
	 * person to read.
	 * It is recommended that all subclasses override this method.
	 * <p>
	 * The {@code toString} method for class {@code Object}
	 * returns a string consisting of the name of the class of which the
	 * object is an instance, the at-sign character `{@code @}', and
	 * the unsigned hexadecimal representation of the hash code of the
	 * object. In other words, this method returns a string equal to the
	 * value of:
	 * <blockquote>
	 * <pre>
	 * getClass().getName() + '@' + Integer.toHexString(hashCode())
	 * </pre></blockquote>
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "[Label] " + this.str + " ( "+x+", "+y+" )";
	}
}
