package game.gui;

import game.character.BattlableActor;
import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.GUIContext;
/** Component responsible for rendering an actor's stat HUD*/
public class ActorStatPane extends BaseComponent {
	private CompoundComponent emptyHUD;
	private CompoundComponent HUD;
	private BattlableActor actor;
	private boolean invalid;

	private Label HPLabel;

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
		Rectangle oldClip = g.getClip();
		g.setClip(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());

		if (backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect(getXWithMargin(), getYWithMargin(), getWidthWithMargin(), getHeightWithMargin());
		}

		//g.setLineWidth(2.0f);
		this.drawBorder(container, g);

		if (actor != null) {
			hpGradientBar.setWidth(( actor.HP()*1.0f / actor.getStats().maxHP() ) * ( hpBar.getWidth() - 4 ));
			HPLabel.setText("HP: "+actor.HP()+"/"+actor.getStats().maxHP());
			HUD.render(container, g);
		} else {
			emptyHUD.render(container, g);
		}

//		//Debug Code
//		g.setColor(Color.red);
//		int w = width/3 - margin, h = height/4 - margin;
//		for (int i = 0; i < 5; i++) {
//			g.drawLine(x + i*w + i*margin, y, x +i*w + i*margin, y + height);
//			g.drawLine(x, y + i*h + i*margin, x + width, y + i*h + i*margin);
//		}

		g.setClip(oldClip);
		g.setLineWidth(oldLW);
		g.setColor(oldC);
	}

	/**
	 * Init the stats section of the GUI
	 */
	private void addStats(GUIContext container) {
		if (!invalid) { return; }

		int w = width / 3, h = height / 4;
		int widthMinusMargins = w - 2*margin, heightMinusMargins = h - 2*margin;
		int topLeftX = x + margin, topLeftY = y + margin;
		int hpBarX = topLeftX + w, hpBarY = topLeftY;
		int hpBarWidth = widthMinusMargins, hpBarHeight = heightMinusMargins;
		hpBar = new RoundedRectangle(hpBarX, hpBarY, hpBarWidth, hpBarHeight, 6);
		ShapeComponent tmp = new ShapeComponent(container, hpBar, true, margin, foregroundColor, backgroundColor);
		emptyHUD.addChild(tmp, hpBarX, hpBarY);

		if (actor != null) {
			int HP = actor.HP();
			int maxHP = actor.getStats().maxHP();
			int inset = Math.max(margin / 2, 2);
			float hpRatio = ( ( HP * 1.0f ) / maxHP );
			int hpGBarX = hpBarX + inset, hpGBarY = hpBarY + inset;
			float hpGBarWidth = hpBarWidth - 2*inset, hpGBarHeight = hpBarHeight - 2*inset;
			hpGradientBar = new RoundedRectangle(hpGBarX, hpGBarY, hpRatio * hpGBarWidth, hpGBarHeight, 6);
			GradientFill hpGradient = DrawingUtils.getHPGradient(hpGBarX, hpGBarY, hpGBarX + hpGBarWidth, hpGBarY + hpGBarHeight);

			HUD.addChild(tmp, hpBarX, hpBarY);
			tmp = new ShapeComponent(container, hpGradientBar, true, margin, foregroundColor, Color.transparent);
			tmp.setFill(hpGradient);
			HUD.addChild(tmp, hpGBarX, hpGBarY);

			String name = actor.getName();
			Label l = new Label(container, name, hpBarX, hpBarY + heightMinusMargins, widthMinusMargins, heightMinusMargins, margin, true, DrawingUtils.TEXT_COLOR, foregroundColor, backgroundColor);
			HUD.addChild(l, margin, margin);
		}

		int statW = widthMinusMargins;
		int statH = heightMinusMargins;
		String[] stats = new String[] { "ATT: ", "DEF: ", "SPD: " };
		int statX = width - widthMinusMargins;
		int statY = margin;
		for (int i = 0; i < stats.length; i++) {
			Label l;
			int labelW;
			int labelH;
			int statYCentered;
			if (i == 0) {
				l = new Label(container, "HP: --/--", statX, statY, statW, statH, margin, true, DrawingUtils.TEXT_COLOR, Color.transparent, Color.transparent);
				labelW = l.getTextWidthWithMargin();
				labelH = l.getTextHeightWithMargin();
				if (labelW > widthMinusMargins) {
					l.setFitToText(false);
					l.setWidth(widthMinusMargins);
					statW = labelW = l.getTextWidthWithMargin();
				}
				if (labelH > heightMinusMargins) {
					l.setHeight(heightMinusMargins);
					statH = labelH = l.getTextHeightWithMargin();
				}

				statYCentered = statY + ( heightMinusMargins - statH ) / 2;

				emptyHUD.addChild(l, statX, statYCentered);
				statY += statH + margin;
			}

			l = new Label(container, stats[i]+"--", statX, statY, statW, statH, margin, true, DrawingUtils.TEXT_COLOR, Color.transparent, Color.transparent);
			labelW = l.getTextWidthWithMargin();
			labelH = l.getTextHeightWithMargin();
			if (labelW > widthMinusMargins) {
				l.setFitToText(false);
				l.setWidth(widthMinusMargins);
				statW = labelW = l.getTextWidthWithMargin();
			}
			if (labelH > heightMinusMargins) {
				l.setHeight(heightMinusMargins);
				statH = labelH = l.getTextHeightWithMargin();
			}

			statYCentered = statY + ( heightMinusMargins - statH ) / 2;
			emptyHUD.addChild(l, statX, statYCentered);

			if (actor != null) {
				int statVal = 0;
				switch (i) {
					case 0:
						statVal = actor.getStats().attack();
						int HP = actor.HP();
						int maxHP = actor.getStats().maxHP();
						statY -= statH + margin;

						l = new Label(container, "HP: "+HP+"/"+maxHP, statX, statY, statW, statH, margin, true, DrawingUtils.TEXT_COLOR, Color.transparent, Color.transparent);
						HPLabel = l;
						labelW = l.getTextWidthWithMargin();
						labelH = l.getTextHeightWithMargin();
						if (labelW > widthMinusMargins) {
							l.setFitToText(false);
							l.setWidth(widthMinusMargins);
							statW = labelW = l.getTextWidthWithMargin();
						}
						if (labelH > heightMinusMargins) {
							l.setHeight(heightMinusMargins);
							statH = labelH = l.getTextHeightWithMargin();
						}

						statYCentered = statY + ( heightMinusMargins - statH ) / 2;
						HUD.addChild(l, statX, statYCentered);

						statY += statH + margin;
						break;
					case 1:
						statVal = actor.getStats().defense();
						break;
					case 2:
						statVal = actor.getStats().speed();
						break;
				}

				String padding = statVal < 10 ? " " : "";
				l = new Label(container, stats[i]+padding+statVal, statX, statY, statW, statH, margin, true, DrawingUtils.TEXT_COLOR, Color.transparent, Color.transparent);
				labelW = l.getTextWidthWithMargin();
				labelH = l.getTextHeightWithMargin();
				if (labelW > widthMinusMargins) {
					l.setFitToText(false);
					l.setWidth(widthMinusMargins);
					statW = labelW = l.getTextWidthWithMargin();
				}
				if (labelH > heightMinusMargins) {
					l.setHeight(heightMinusMargins);
					statH = labelH = l.getTextHeightWithMargin();
				}

				statYCentered = statY + ( heightMinusMargins - statH ) / 2;
				HUD.addChild(l, statX, statYCentered);
			}

			statY += statH + margin;
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
