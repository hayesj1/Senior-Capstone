package game.gui;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public abstract class CapstoneComponent extends AbstractComponent {
	protected boolean enabled;
	protected boolean shown;

	public CapstoneComponent(GUIContext container) {
		super(container);
		this.enabled = true;
		this.shown = true;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public boolean isShown() {
		return shown;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setShown(boolean shown) {
		this.shown = shown;
	}
}
