package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

public class LabeledButton extends MouseOverArea implements IBaseComponent {
	private Label label;
	private Shape shape;
	private int margin;

	private Color textColor;
	private Color foregroundColor;
	private Color backgroundColor;

	private boolean enabled;
	private boolean shown;

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
		super.setNormalColor(this.foregroundColor);
		super.setMouseDownColor(this.foregroundColor.darker());
		super.setMouseOverColor(this.foregroundColor.darker());

		this.enabled = true;
		this.shown = true;

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

		int tw = font.getWidth("___"+text);
		int th = font.getHeight("___"+text);
		int bw = getWidth();
		int bh = getHeight();
		if (tw > bw) {
			setWidth(tw);
			bw = getWidth();
		}
		if (th > bh) {
			setHeight(th);
			bh = getHeight();
		}
		int tx = getX() + ((bw - tw) / 2);
		int ty = getY() + ((bh / 2) - (th / 2));
		label = new Label(container, text, tx, ty, tw, th, margin, true, textColor, Color.transparent, backgroundColor);
		label.shouldDrawBorder(false);
//		System.out.println(toString()+" : tx, ty : "+tx+", "+ty);
//		System.out.println(toString()+" : x, y :"+getX()+", "+getY());
		labelInvalid = false;
		fontChanged = false;
	}

	@Override
	public void render(GUIContext container, Graphics g) {
		if (!shown) { return; }

		if (labelInvalid) {
			initLabel(g.getFont(), getLabelText());
		}
		Color oldC = g.getColor();
		Rectangle oldClip = g.getClip();
		g.setClip(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());

		g.setColor(foregroundColor);
		g.fill(this.shape);
		super.render(container, g);
		label.render(container, g);

		if (!enabled) {
			//DrawingUtils.drawDisabledOverlay(container, g, getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());
		}

		g.setClip(oldClip);
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

	public Shape getShape() { return this.shape; }
	public Label getLabel() { return this.label; }
	public String getLabelText() { return this.label.getText(); }
	public void setLabelText(Object textSrc) {
		this.label.setText(textSrc);
		this.labelInvalid = true;
	}


	@Override
	public int getMargin() {
		return this.margin;
	}

	@Override
	public int getX() {
		return (int) super.getX();
	}

	@Override
	public int getY() {
		return (int) super.getY();
	}

	@Override
	public int getXWithMargin() {
		return this.getX() - this.margin;
	}
	@Override
	public int getYWithMargin() {
		return this.getY() - this.margin;
	}
	@Override
	public int getWidthWithMargin() {
		return this.getWidth() + this.margin;
	}
	@Override
	public int getHeightWithMargin() {
		return this.getHeight() + this.margin;
	}
	@Override
	public void setMargin(int margin) {
		this.margin = margin;
	}
	@Override
	public void setX(int x) {
		super.setX(x);
		this.shape.setX(x);
		this.labelInvalid = true;
	}
	@Override
	public void setY(int y) {
		super.setY(y);
		this.shape.setY(y);
		this.labelInvalid = true;
	}
	@Override
	public void setWidth(int width) { this.shape.transform(Transform.createScaleTransform(width * 1.0f / this.getWidth(), 1.0f)); }
	@Override
	public void setHeight(int height) { this.shape.transform(Transform.createScaleTransform(1.0f, height * 1.0f / this.getHeight())); }
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		this.labelInvalid = true;
		if (this.shape != null) {
			this.shape.setX(x);
			this.shape.setY(y);
		}
	}

	@Override
	public Color getForegroundColor() {
		return this.foregroundColor;
	}
	@Override
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
	@Override
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		this.label.setForegroundColor(foregroundColor);
		super.setNormalColor(foregroundColor);
		super.setMouseDownColor(foregroundColor);
		super.setMouseOverColor(foregroundColor.darker());
	}
	@Override
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.label.setBackgroundColor(backgroundColor);
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	@Override
	public boolean isShown() {
		return this.shown;
	}
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		this.label.setEnabled(enabled);
		if (enabled) {
			this.label.setTextColor(textColor);
			super.setNormalColor(foregroundColor);
		} else {
			this.label.setTextColor(textColor.brighter(1.0f));
			super.setNormalColor(foregroundColor.darker(0.75f));

		}
	}
	@Override
	public void setShown(boolean shown) {
		this.shown = shown;
		this.label.setShown(shown);
	}
}
