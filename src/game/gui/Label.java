package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
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
		this.drawBorder = true;
		if (fitText) {
			this.fitWidth();
		}
		this.cutoff = findCutoff();
	}

	public void fitWidth(){
		setWidth(getWidthOfText("_"+str+"_"));
	}

	private int findCutoff() {
		int cutoff;
		int tw = getTextWidth();
		for (cutoff = str.length()-1; tw >= width && cutoff >= 0; cutoff--) { tw = getWidthOfText(str.substring(0, cutoff +1)); }

		if (cutoff < 0) {
			cutoff = 0;
		}

		return cutoff;
	}

	private int getWidthOfText(String text) {
		return this.font.getWidth(text);
	}
	private int getHeightOfText(String text) {
		return this.font.getHeight(text);
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
		} else if (width < getWidthOfText(str)) {
			setCutoff(findCutoff());
		}

		Color oldColor = g.getColor();
		float oldLineW = g.getLineWidth();
		Rectangle oldClip = g.getClip();

		g.setLineWidth(2.0f);
		g.setClip(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());

		g.setColor(backgroundColor);
		g.fillRoundRect(x, y, width, height, 6);

		this.drawBorder(container, g);

		int tw = getWidthOfText(str);
		int th = getHeightOfText(str);
		int tx = x + ((width - tw) / 2);
		int ty = y + ((height - th) / 2);
		g.getFont().drawString(tx, ty, str, textColor, 0, cutoff);

		g.setClip(oldClip);
		g.setLineWidth(oldLineW);
		g.setColor(oldColor);
	}

	public String getText() {
		return str;
	}
	public int getTextWidth() { return this.getWidthOfText(this.getText()); }
	public int getTextHeight() { return this.getHeightOfText(this.getText()); }
	public int getTextWidthWithMargin() {
		return getTextWidth() + this.margin;
	}
	public int getTextHeightWithMargin() {
		return this.getTextHeight() + this.margin;
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

	public void setFitToText(boolean fitText) {
		this.fitStr = fitText;
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
