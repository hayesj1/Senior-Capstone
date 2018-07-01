package game.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

public class LabeledButton extends MouseOverArea {
	public static final Color DEFAULT_TEXT_COLOR = Color.black;
	private String text;
	private Color textColor;
	private int textX;
	private int textY;
	private int cutoff;
	private boolean textNeedsValidation;
	private Shape bounds;

	public LabeledButton(Object textSrc, GUIContext container, Font font, Image image) { this(textSrc, container, font, image, null); }
	public LabeledButton(Object textSrc, GUIContext container, Font font, Shape shape) { this(textSrc, container, font, null, shape); }
	public LabeledButton(Object textSrc, GUIContext container, Font font, Image image, Shape shape) {
		super(container, image, shape);

		this.text = textSrc.toString();
		this.textColor = DEFAULT_TEXT_COLOR;
		this.textX = this.getX();
		this.textY = this.getY();
		this.textNeedsValidation = true;
		this.bounds = shape;

	}

	public LabeledButton setText(Object textSrc) {
		this.text = textSrc.toString();
		this.textNeedsValidation = true;
		return this;
	}

	public LabeledButton setTextColor(Color color) {
		this.textColor = color;
		return this;
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		this.textNeedsValidation = true;
	}

	private void validateTextLocation(Graphics g) {
		if (!textNeedsValidation) { return; }

		Font font = g.getFont();
		int tw = font.getWidth(this.text);
		int th = font.getHeight(this.text);
		int bw = this.getWidth();
		int bh = this.getHeight();

		for (this.cutoff = this.text.length()-1; tw >= bw; this.cutoff--, tw = font.getWidth(this.text.substring(0, this.cutoff+1)));

		this.textX = this.getX() + (bw / 2) - (tw / 2);
		this.textY = this.getY() + (bh / 2) - (th / 2);
		this.textNeedsValidation = false;
	}

	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);
		validateTextLocation(g);
		g.getFont().drawString(this.textX, this.textY, this.text, this.textColor, 0, cutoff);
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
		return "[LabeledButton] "+this.text;
	}

	public String getLabel() { return this.text; }
	public Shape getBounds() { return this.bounds; }
}
