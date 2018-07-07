package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

public class LabeledButton extends MouseOverArea {
	private Label label;
	private Shape shape;
	private int margin;

	private Color textColor;
	private Color foregroundColor;
	private Color backgroundColor;

	private boolean labelInvalid;
	private boolean fontChanged;

	public LabeledButton(Object textSrc, GUIContext container, Image image) { this(container, textSrc, DrawingUtils.DEFAULT_MARGIN, container.getDefaultFont(), null, null, null, image, null); }
	public LabeledButton(Object textSrc, GUIContext container, Shape shape) { this(container, textSrc, DrawingUtils.DEFAULT_MARGIN, container.getDefaultFont(), null, null, null, null, shape); }
	public LabeledButton(Object textSrc, GUIContext container, Color textColor, Color foregroundColor, Color backgroundColor, Shape shape) {
		this(container, textSrc, DrawingUtils.DEFAULT_MARGIN, container.getDefaultFont(), textColor, foregroundColor, backgroundColor, null, shape);
	}
	public LabeledButton(GUIContext container, Object textSrc, int margin, Font font, Color textColor, Color foregroundColor, Color backgroundColor, Image image, Shape shape) {
		super(container, image, shape);
		this.margin = margin;
		this.shape = shape;

		this.textColor = textColor == null ? DrawingUtils.DEFAULT_TEXT_COLOR : textColor;
		this.foregroundColor = foregroundColor == null ? DrawingUtils.DEFAULT_FOREGROUND_COLOR : foregroundColor;
		this.backgroundColor = backgroundColor == null ? DrawingUtils.DEFAULT_BACKGROUND_COLOR : backgroundColor;


		this.label = null;
		this.labelInvalid = true;
		this.fontChanged = true;
		initLabel(container.getDefaultFont(), textSrc.toString());

		this.label.setTextColor(this.textColor);
		this.label.setForegroundColor(this.foregroundColor);
		this.label.setBackgroundColor(this.backgroundColor);
	}

	private void initLabel(Font font, String text) {
		if (!labelInvalid && !fontChanged) { return; }

		if ( text == null || text.isEmpty() ) {
			if (label != null) {
				text = getLabelText();
			} else {
				text = "NULL";
			}
		}

		int tw = font.getWidth(text);
		int th = font.getHeight(text);
		int bw = getWidth();
		int bh = getHeight();
		int tx = getX() + (bw / 2) - (tw / 2);
		int ty = getY() + (bh / 2) - (th / 2);

		label = new Label(container, text, tx, ty, tw, th, margin, true, textColor, Color.transparent, backgroundColor);
		labelInvalid = false;
		fontChanged = false;
	}

	@Override
	public void render(GUIContext container, Graphics g) {
		if (labelInvalid || (!g.getFont().equals(container.getDefaultFont()) && fontChanged) ) {
			initLabel(g.getFont(), null);
		}
		Color oldC = g.getColor();

		g.setColor(foregroundColor);
		g.fill(shape);
		label.render(container, g);

		g.setColor(oldC);
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
		return "[LabeledButton] "+this.getLabelText();
	}

	public Label getLabel() { return this.label; }
	public String getLabelText() { return this.label.getText(); }
	public Shape getShape() { return this.shape; }

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		this.labelInvalid = true;
	}

	public LabeledButton setLabelText(Object textSrc) {
		this.label.setText(textSrc);
		this.labelInvalid = true;
		return this;
	}
	public LabeledButton setTextColor(Color color) {
		this.textColor = color;
		this.label.setTextColor(color);
		return this;
	}
	public LabeledButton setForegroundColor(Color color) {
		this.foregroundColor = color;
		return this;
	}
	public LabeledButton setBackgroundColor(Color color) {
		this.backgroundColor = color;
		return this;
	}
}
