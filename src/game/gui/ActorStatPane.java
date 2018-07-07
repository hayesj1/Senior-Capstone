package game.gui;

import game.SuperDungeoneer;
import game.character.BattlableActor;
import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.GUIContext;

public class ActorStatPane extends BaseComponent {
	private CompoundComponent emptyHUD;
	private CompoundComponent HUD;
	private BattlableActor actor;
	private boolean invalid;

	private RoundedRectangle hpBar;
	private RoundedRectangle hpGradientBar;

	public ActorStatPane(GUIContext container, int x, int y, int width, int height) { this(container, null, x, y, width, height, DrawingUtils.DEFAULT_MARGIN, null, null); }
	public ActorStatPane(GUIContext container, BattlableActor actor, int x, int y, int width, int height, int margin, Color foregroundColor, Color backgroundColor) {
		super(container, x, y, width, height, margin, foregroundColor, backgroundColor);

		this.emptyHUD = new CompoundComponent(container, x+margin, y+margin, width-margin, height-margin);
		this.HUD = new CompoundComponent(container, x+margin, y+margin, width-margin, height-margin);
		this.actor = actor;
		this.invalid = true;
	}

	/**
	 * Renders the background (if one is set) and the children of this panel
	 *
	 * @param container the GUIContext
	 * @param g         the Graphics object to draw on
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
		if (invalid) { addStats(container); }
		if (!shown) { return; }

		Color oldC = g.getColor();
		float oldLW = g.getLineWidth();

		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}

		g.setLineWidth(2.0f);
		g.setColor(foregroundColor);
		g.drawRect(getX()+margin, getY()+margin, getWidth()-margin, getHeight()-margin);

		if (actor != null) {
			hpGradientBar.setWidth(( actor.HP()*1.0f / actor.getStats().maxHP() ) * ( hpBar.getWidth() - 4 ));
			HUD.render(container, g);
		} else {
			emptyHUD.render(container, g);
		}

		g.setLineWidth(oldLW);
		g.setColor(oldC);
	}

	private void addStats(GUIContext container) {
		if (!invalid) { return; }

		int w = this.width / 3, h = this.height / 3;
		int hpx = this.x + SuperDungeoneer.COMPONENT_SPACING + w, hpy = this.y + SuperDungeoneer.COMPONENT_SPACING;
		hpBar = new RoundedRectangle(hpx, hpy, w, 20, 6);
		ShapeComponent tmp = new ShapeComponent(container, hpBar, true, DrawingUtils.DEFAULT_MARGIN, foregroundColor, backgroundColor);
		emptyHUD.addChild(tmp, SuperDungeoneer.COMPONENT_SPACING, SuperDungeoneer.COMPONENT_SPACING);

		if (actor != null) {
			int HP = actor.HP();
			int maxHP = actor.getStats().maxHP();
			float hpRatio = ( ( HP * 1.0f ) / maxHP );
			String name = actor.getName();

			hpGradientBar = new RoundedRectangle(hpx + 2, hpy + 2, hpRatio * ( w - 4 ), 16, 6);
			GradientFill hpGradient = DrawingUtils.getHPGradient(hpx + 2, hpy + 2, ( hpx + 2 ) + ( hpBar.getWidth() - 4 ), ( hpy + 2 ) + ( 16 - 4 ));
			tmp = new ShapeComponent(container, hpBar, true, DrawingUtils.DEFAULT_MARGIN, foregroundColor, backgroundColor);
			HUD.addChild(tmp, SuperDungeoneer.COMPONENT_SPACING, SuperDungeoneer.COMPONENT_SPACING);
			tmp = new ShapeComponent(container, hpGradientBar, true, DrawingUtils.DEFAULT_MARGIN, foregroundColor, null);
			tmp.setFill(hpGradient);
			HUD.addChild(tmp, SuperDungeoneer.COMPONENT_SPACING, SuperDungeoneer.COMPONENT_SPACING);

			Label l = new Label(container, name, hpx, hpy + h, true);
			HUD.addChild(l, w, h);
		}

		String[] stats = new String[] { "ATT: ", "DEF: ", "SPD: " };
		int x = width - w - SuperDungeoneer.COMPONENT_SPACING;
		int y = SuperDungeoneer.COMPONENT_SPACING;
		for (int i = 0; i < stats.length; i++) {
			Label l = new Label(container, stats[i]+"--", x, y, true);
			l.setTextColor(DrawingUtils.TEXT_COLOR);
			l.setForegroundColor(foregroundColor);
			l.setBackgroundColor(backgroundColor);

			int sw = l.getWidth();
			int sh = l.getHeight();
			if (sw > w) {
				l.setWidth(w);
				sw = l.getWidth();
			}
			if (sh > h) {
				l.setHeight(h);
				sh = l.getHeight();
			}

			int sx = x + ( w - sw ) / 2;
			int sy = y + ( h - sh ) / 2;
			l.setLocation(sx, sy);
			emptyHUD.addChild(l, sx, sy);

			if (actor != null) {
				int statVal = 0;
				switch (i) {
					case 0:
						statVal = actor.getStats().attack();
						break;
					case 1:
						statVal = actor.getStats().defense();
						break;
					case 2:
						statVal = actor.getStats().speed();
						break;
				}

				l = new Label(container, stats[i]+statVal, x, y, true);
				l.setTextColor(DrawingUtils.TEXT_COLOR);
				l.setForegroundColor(foregroundColor);
				l.setBackgroundColor(backgroundColor);

				sw = l.getWidth();
				sh = l.getHeight();
				if (sw > w) {
					l.setWidth(w);
					sw = l.getWidth();
				}
				if (sh > h) {
					l.setHeight(h);
					sh = l.getHeight();
				}

				sx = x + ( w - sw ) / 2;
				sy = y + ( h - sh ) / 2;
				l.setLocation(sx, sy);
				HUD.addChild(l, sx, sy);
			}

			y += sh + SuperDungeoneer.COMPONENT_SPACING;
		}

		invalid = false;
	}

	public BattlableActor getActor() {
		return actor;
	}
	public void setActor(BattlableActor actor) {
		this.actor = actor;
		this.invalid = true;
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (emptyHUD != null) {
			emptyHUD.setLocation(x+margin, y+margin);
		}

		if (HUD != null) {
			HUD.setLocation(x+margin, y+margin);
		}
	}
}
