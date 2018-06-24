package game.input;

public class InputHandler extends InputListenerAdapter {
	private static int MAX_OFFSET = 15;
	private static int MIN_OFFSET = -5;

	private int xOff = 0;
	private int yOff = 0;

	@Override
	public void keyReleased(int key, char c) {
		switch (c) {
			case 'w':
				yOff -= 2;
				if (yOff < MIN_OFFSET) { yOff = MIN_OFFSET; }
				break;
			case 's':
				yOff += 2;
				if (yOff > MAX_OFFSET) { yOff = MAX_OFFSET; }
				break;
			case 'a':
				xOff -= 2;
				if (xOff < MIN_OFFSET) { xOff = MIN_OFFSET; }
				break;
			case 'd':
				xOff += 2;
				if (xOff > MAX_OFFSET) { xOff = MAX_OFFSET; }
				break;
			default:
				break;
		}
	}

	public int getxOff() {
		return xOff;
	}

	public int getyOff() {
		return yOff;
	}
}
