package game.dungeon;

import game.character.Actor;
import game.character.BattlableActor;
import game.character.CapturableActor;
import game.character.PlayerActor;
import game.gui.ActorComponent;
import game.gui.BaseComponent;
import game.util.DrawingUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import java.util.ArrayList;

/** Component responsible for rendering the dungeon and its contents */
public class DungeonComponent extends BaseComponent {
	private static final char PASSAGE_CHAR = Tile.PASSAGE_CHAR;
	private static final char WALL_CHAR    = Tile.WALL_CHAR;
	private static final char BLOCKED_CHAR = Tile.BLOCKED_CHAR;

	private Dungeon dungeon;
	private int spriteLength;

	private ActorComponent playerComponent;
	private ArrayList<ActorComponent> foeComponents;

	private boolean initialized = false;
	private boolean generated = false;
	private int lastPlayerX;
	private int lastPlayerY;

	public DungeonComponent(GUIContext container, int x, int y, int width, int height, int margin) {
		this(container, x, y, width, height, margin, DrawingUtils.DEFAULT_FOREGROUND_COLOR, DrawingUtils.DEFAULT_BACKGROUND_COLOR);
	}
	public DungeonComponent(GUIContext container, int x, int y, int width, int height, int margin, Color foregroundColor, Color backgroundColor) {
		super(container, x, y, width, height, margin, foregroundColor, backgroundColor);

		this.dungeon = null;
		this.spriteLength = 1;
		this.foeComponents = new ArrayList<>();
	}

	private void init(GameContainer container, int spriteWidth) {
		if (initialized) { return; }

		this.spriteLength = spriteWidth;
		int dungW = (width-this.spriteLength) / this.spriteLength;
		int dungH = (height-this.spriteLength) / this.spriteLength;
		this.dungeon = new Dungeon(dungW, dungH);

		initialized = true;
	}

	public void generateDungeon(GameContainer container, int spriteWidth) {
		if (generated) { return; }
		else if (!initialized) { init(container, spriteWidth); }

		dungeon.generateDungeon();

		generated = true;
	}

	/**
	 * Calls this at the end of your {@link #render(GUIContext, Graphics)} implementation to draw a disabled overlay if
	 * this component is disabled. Or just conditionally call {@link #drawDisabledOverlay(GUIContext, Graphics)} yourself
	 *
	 * @param container the GUIContext
	 * @param g         the Graphics object to draw on
	 */
	@Override
	public void render(GUIContext container, Graphics g) {
		Color oldColor = g.getColor();
		Rectangle oldClip = g.getClip();

		if (this.backgroundColor != null) {
			g.setColor(this.backgroundColor);
			g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}

		for (int i = 0; i < dungeon.getHeight(); i++) {
			for (int j = 0; j < dungeon.getWidth(); j++) {
				Tile t = dungeon.getTileAt(j, i);
				int x = getX() + j*spriteLength;
				int y = getY() + i*spriteLength;
				Color clr;
				switch(t.getState()) {
					case Tile.WALL:
						clr = backgroundColor;
						break;
					case Tile.ACTOR:
						clr = foregroundColor;
						final Actor a = (Actor) dungeon.getTileAt(j, i).getOccupant();
						if (a != null) {
							if (a instanceof CapturableActor) {
								if (foeComponents.stream().noneMatch(comp -> comp.getActor() == a)) {
									if (!((BattlableActor) a).isIncapacitated()) {
										foeComponents.add(new ActorComponent(container, a, x, y, spriteLength));
									}
								}
							} else {
								if (playerComponent == null) {
									addPlayer((PlayerActor) a);
								} else {
									if (playerComponent.getX() != x || playerComponent.getY() != y) {
										System.err.println("Duplicate Player Found!");
									}
								}
							}
						} else {
							System.err.println("Null Occupant Found at Tile with State: "+dungeon.getTileAt(j, i).getState());
							//dungeon.getTileAt(j, i).setStateAndOccupant(Tile.PASSAGE, null);
						}
						break;
					case Tile.PASSAGE:
						clr = foregroundColor;
						break;
					default:
						clr = DrawingUtils.TEXT_COLOR;
						break;
				}
				g.setColor(clr);
				g.fillRect(x, y, spriteLength, spriteLength);
			}
		}

		playerComponent.render(container, g);
		foeComponents.forEach(comp -> comp.render(container, g));

		g.setClip(oldClip);
		g.setColor(oldColor);
	}

	/** Places the player in a random, valid position starting from the top left. */
	public void addPlayer(PlayerActor player) {
		if (!generated) { return; }

		for (int i = 2; i < dungeon.getHeight() - 2; i++) {
			for (int j = 2; j < dungeon.getWidth() - 2; j++) {
				Tile t = this.dungeon.getTileAt(j, i);
				//System.out.print(t);
				if (t.isPassage()) {
					//System.out.println(" | True");
					t.setStateAndOccupant(Tile.ACTOR, player);
					this.playerComponent = new ActorComponent(container, player, getX() + j*spriteLength, getY() + i*spriteLength, spriteLength);
					this.lastPlayerX = j;
					this.lastPlayerY = i;
					return;
				}

				//System.out.println("");
			}
		}
	}

	public void removeFoeComponent(Actor a) {
		if (foeComponents.removeIf(actorComponent -> actorComponent.getActor().equals(a))) {
			System.out.println("Removed foe(s)!");
		}
	}

	/** @return the player's [ x, y ] position in tiles */
	public int[] getPlayerPos() { return new int[] { lastPlayerX, lastPlayerY }; }

	/** offsets the player by [ x, y ] pixels */
	public void offsetPlayerPos(int[] pos) {
		//System.out.println(lastPlayerX+","+lastPlayerY);
		this.playerComponent.setLocation(this.playerComponent.getX() + pos[0], this.playerComponent.getY() + pos[1]);
		this.lastPlayerX = (playerComponent.getX() / spriteLength);
		this.lastPlayerY = (playerComponent.getY() / spriteLength);
	}

	public Dungeon getDungeon() { return dungeon; }

	/** Test code for the dungeon generator */
	public static void main(String[] args){
		Dungeon dungeon = new Dungeon(40,20);
		dungeon.generateDungeon();
		System.out.println(dungeon);
	}
}
