package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class ButtonGrid extends BaseComponent {
	private int rows;
	private int cols;
	private int spacing;
	private int cellW;
	private int cellH;

	private boolean invalid;
	private boolean populateAcross;

	private LinkedList<LabeledButton> buttons;

	public ButtonGrid(GUIContext container, int rows, int cols, int spacing, LabeledButton... buttons) {
		this(container, rows, cols, spacing, DrawingUtils.DEFAULT_MARGIN, null, null, buttons);
	}
	public ButtonGrid(GUIContext container, int rows, int cols, int spacing, int margin, Color foregroundColor, Color backgroundColor, LabeledButton... buttons) {
		this(container, 0, 0, rows, cols, spacing, margin, true, foregroundColor, backgroundColor, buttons);
	}
	public ButtonGrid(GUIContext container, int x, int y, int rows, int cols, int spacing, int margin, boolean populateAcross, Color foregroundColor, Color backgroundColor, LabeledButton... buttons) {
		super(container, x, y, 0, 0, margin, foregroundColor, backgroundColor);
		this.rows = rows <= 0 ? 1 : rows;
		this.cols = cols <= 0 ? 1 : cols;
		this.spacing = spacing < 0 ? 0 : spacing;
		this.cellW = -1;
		this.cellH = -1;

		this.invalid = true;
		this.populateAcross = populateAcross;
		this.buttons = new LinkedList<>();

		this.buttons.addAll(Arrays.asList(buttons));
		while (this.rows * this.cols < this.buttons.size()) {
			if(this.populateAcross) { this.rows++; } else { this.cols++; }
		}
		computeButtonPositions();
	}

	/**
	 * Computes and moves the buttons to the proper locations. Runs in O(n) time.
	 */
	private void computeButtonPositions() {
		if (!this.invalid) { return; }
		int cw = this.cellW;
		int ch = this.cellH;
		for (LabeledButton button : this.buttons) {
			if (button == null) { continue; }
			if (button.getWidth() > cw) { cw = button.getWidth(); }
			if (button.getHeight() > ch) { ch = button.getHeight(); }
		}

		if (cw <= 0) { cw = 120; }
		if (ch <= 0) { ch = 40; }

		this.width = ((cols-1)*spacing) + (cols * cw);
		this.height = ((rows-1)*spacing) + (rows * ch);
		this.cellW = cw;
		this.cellH = ch;

		int I = (populateAcross ? rows : cols);
		int J = (populateAcross ? cols : rows);
		ListIterator<LabeledButton> it = this.buttons.listIterator();
		for (int i = 0; it.hasNext() && i < I; i++) {
			for (int j = 0; it.hasNext() && j < J; j++) {
				int cx = getX() + (cw + spacing) * (populateAcross ? j: i);
				int cy = getY() + (ch + spacing) * (populateAcross ? i : j);
				it.next().setLocation(cx, cy);
			}
		}
	}

	/**
	 *
	 * @param container the GUIContext
	 * @param g the Graphics object to draw on
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
		if (!this.shown) {
			return;
		} else if (this.invalid) {
			computeButtonPositions();
		}

		Color oldC = g.getColor();

		g.setColor(backgroundColor);
		g.fillRect(getX(), getY(), getWidth(), getHeight());

		for (LabeledButton button : this.buttons) {
			button.render(container, g);
			if (!enabled) {
				DrawingUtils.drawDisabledOverlay(container, g, button.getX(), button.getY(), button.getWidth(), button.getHeight());
			}
		}

		g.setColor(oldC);
	}

	/**
	 * Moves the entire grid to <code>(x,y)</code>.
	 * This invalidates the grid, thus button positions will be recomputed
	 * @param x the x-coordinate of the top-left pixel
	 * @param y the y-coordinate of the top-left pixel
	 * @see #computeButtonPositions()
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		this.invalid = true;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		buttons.forEach(button -> button.setAcceptingInput(enabled));
	}
}
