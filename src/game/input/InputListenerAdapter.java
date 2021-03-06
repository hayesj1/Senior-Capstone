package game.input;

import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

/**
 * @deprecated This class was created for learning purposes only.
 */
@Deprecated
public class InputListenerAdapter implements InputListener {
	private static final int MAX_CONTROLLERS = 20;
	private static final int MAX_CONTROLLER_BUTTONS = 100;

	protected boolean[] controllerLeft = new boolean[20];
	protected boolean[] controllerRight = new boolean[20];
	protected boolean[] controllerUp = new boolean[20];
	protected boolean[] controllerDown = new boolean[20];
	protected boolean[][] controllerButton = new boolean[20][100];


	public void setInput(Input input) {
	}

	public void keyPressed(int key, char c) {
	}

	public void keyReleased(int key, char c) {
	}

	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	}

	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	}

	public void mouseClicked(int button, int x, int y, int clickCount) {
	}

	public void mousePressed(int button, int x, int y) {
	}

	public void controllerButtonPressed(int controller, int button) {
		this.controllerButton[controller][button] = true;
	}

	public void controllerButtonReleased(int controller, int button) {
		this.controllerButton[controller][button] = false;
	}

	public void controllerDownPressed(int controller) {
		this.controllerDown[controller] = true;
	}

	public void controllerDownReleased(int controller) {
		this.controllerDown[controller] = false;
	}

	public void controllerLeftPressed(int controller) {
		this.controllerLeft[controller] = true;
	}

	public void controllerLeftReleased(int controller) {
		this.controllerLeft[controller] = false;
	}

	public void controllerRightPressed(int controller) {
		this.controllerRight[controller] = true;
	}

	public void controllerRightReleased(int controller) {
		this.controllerRight[controller] = false;
	}

	public void controllerUpPressed(int controller) {
		this.controllerUp[controller] = true;
	}

	public void controllerUpReleased(int controller) {
		this.controllerUp[controller] = false;
	}

	public void mouseReleased(int button, int x, int y) {
	}

	public void mouseWheelMoved(int change) {
	}

	public boolean isAcceptingInput() {
		return true;
	}

	public void inputEnded() {
	}

	public void inputStarted() {
	}
}
