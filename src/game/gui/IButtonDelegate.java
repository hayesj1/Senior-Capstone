package game.gui;

import game.input.ICommandDelegate;
import org.newdawn.slick.gui.ComponentListener;

public interface IButtonDelegate extends ComponentListener, ICommandDelegate {

	void action(LabeledButton button);
}
