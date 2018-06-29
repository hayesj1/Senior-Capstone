package game.gui;

import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class ButtonGrid extends CapstoneComponent {
	private int rows;
	private int cols;
	private int spacing;
	private int cellW;
	private int cellH;

	private int pos;
	private boolean invalid;
	private boolean populateAcross;

	private Color backgroundColor;
	private LinkedList<LabeledButton> buttons;

	public ButtonGrid(GUIContext container, int rows, int cols, int spacing, LabeledButton... buttons) {
		this(container, 0, 0, rows, cols, spacing, true, buttons);
	}
	public ButtonGrid(GUIContext container, int x, int y, int rows, int cols, int spacing, boolean populateAcross, LabeledButton... buttons) {
		super(container, x, y);
		this.rows = rows <= 0 ? 1 : rows;
		this.cols = cols <= 0 ? 1 : cols;
		this.spacing = spacing < 0 ? 0 : spacing;
		this.cellW = -1;
		this.cellH = -1;

		this.pos = 0;
		this.invalid = true;
		this.populateAcross = populateAcross;

		this.backgroundColor = Color.gray;
		this.buttons = new LinkedList<>();

		this.buttons.addAll(Arrays.asList(buttons));
		while (this.rows * this.cols < this.buttons.size()) {
			if(this.populateAcross) { this.rows++; } else { this.cols++; }
		}
		computeButtonPositions();
		this.pos = this.buttons.size();
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
				int cx = this.x + (cw + spacing) * (populateAcross ? j: i);
				int cy = this.y + (ch + spacing) * (populateAcross ? i : j);
				it.next().setLocation(cx, cy);
			}
		}
	}

	/**
	 *
	 * @param container the GUIContext
	 * @param g the Graphics object to draw on
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
		if (this.invalid) {
			computeButtonPositions();
		}
		for (LabeledButton button : this.buttons) {
			button.render(container, g);
			if (!enabled) {
				DrawingUtils.drawDisabledOverlay(container, g, button.getX(), button.getY(), button.getWidth(), button.getHeight());
			}
		}

		g.setColor(old);
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

	public void setBackgroundColor(Color color) { this.backgroundColor = color; }
}
