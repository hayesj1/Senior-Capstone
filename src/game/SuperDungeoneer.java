package game;

import game.battle.Battle;
import game.character.*;
import game.character.moves.Move;
import game.character.moves.MoveSet;
import game.dungeon.Dungeon;
import game.dungeon.DungeonComponent;
import game.dungeon.Tile;
import game.gui.*;
import game.input.BattleCommandDelegate;
import game.input.DemoInputHandler;
import game.input.MovementCommandDelegate;
import game.util.DrawingUtils;
import org.newdawn.slick.*;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.openal.SoundStore;

import java.util.ArrayList;
import java.util.Random;

public class SuperDungeoneer implements Game {
	private static SuperDungeoneer instance = new SuperDungeoneer("Super Dungeoneer");

	public static final int COMPONENT_SPACING = 3;
	public static final Color SELECTION_COLOR = Color.orange;


	private int countDown = 1500;
	private boolean showingSplash = true;
	private Image splash;

	private String title;
	private DemoInputHandler handler = null;

	private MouseListener onFlyMouseListner = new MouseListener() {
		private boolean isAcceptingInput = false;
		@Override
		public void mousePressed(int button, int x, int y) { }

		@Override
		public void mouseReleased(int button, int x, int y) {
			if (button == Input.MOUSE_LEFT_BUTTON) {
				if (!needsTarget) {
					if (captureButton.getShape().contains(x, y)) {
						setMoveSlot(0);
					}
				}

				if (needsTarget) {
					for (int i = 0; i < targetSelectorButtons.length; i++) {
						if (targetSelectorButtons[i].getShape().contains(x, y)) {
							setSelectedTargetSlot(Integer.valueOf(targetSelectorButtons[i].getLabelText().substring(0, 1)));
							break;
						}
					}
				}
			}
		}

		@Override
		public void mouseClicked(int button, int x, int y, int clickCount) { }

		@Override
		public void mouseWheelMoved(int change) { }

		@Override
		public void mouseMoved(int oldx, int oldy, int newx, int newy) { }

		@Override
		public void mouseDragged(int oldx, int oldy, int newx, int newy) { }

		@Override
		public void setInput(Input input) { }

		@Override
		public boolean isAcceptingInput() {
			return isAcceptingInput;
		}

		@Override
		public void inputEnded() {
			isAcceptingInput = false;
		}

		@Override
		public void inputStarted() {
			isAcceptingInput = true;
		}
	};

//	private MoveSet testMoveSet;
//	private Move[] testMoves;
//	private Species testSpecies;
//	private Move punch;
//	private Move kick;
//	private Move slap;
//	private Move elbow;
//	private Move bite;
//	private Move slam;

	private MoveSet allMoveSet;
	private MoveSet playerMoveSet;
	private MoveSet monsterMoveSet;
	private Move[] allMoves;
	private Move[] playerMoves;
	private Move[] monsterMoves;
	private Move jab;
	private Move comboPunch;
	private Move slash;
	private Move crunch;
	private Move bladeBeam;
	private Move rocketPunch;
	private Move riskyBlow;
	private Move dragonfist;
	private Move rage;
	private Move counter;
	private Move upperCut;
	private Move killerStab;
	private Move fireFist;

	private int spriteSize;
	private SpriteSheet humanSheet;
	private SpriteSheet orcSheet;

	private Species playerSpecies;
	private Species orcSpecies;

	private PlayerActor player;
	private PlayableActor buddy;
	private CapturableActor mon2;
	private CapturableActor mon3;
	private ArrayList<PlayableActor> actors;
	private ArrayList<CapturableActor> foes;
	private ArrayList<Tile> foeTiles;

	private Battle demoBattle;
	private BattleCommandDelegate battleCommandDelegate;
	private MovementCommandDelegate movementCommandDelegate;

	private DungeonComponent dungeon;

	private Panel battleControlPanel;
	private TextHistory feedbackText;
	private LabeledButton[] moveButtons;
	private ButtonGrid moveGrid;
	private Label moveSelectorLabel;
	private LabeledButton captureButton;
	private LabeledButton[] targetSelectorButtons;
	private ButtonGrid targetSelectionGrid;
	private Label targetSelectorLabel;

	private Panel helpPanel;
	private TextHistory helpText;
	private Panel partyPanel;
	private ActorStatPane[] partyMemberPanes;

	private ArrayList<Animation> anims;
	private boolean needsTarget = false;
	private boolean selectedTarget = false;
	private int targetSlot = -1;
	private int moveSlot = -1;

	/**
	 * Create a new basic game
	 *
	 * @param title The title for the game
	 */
	private SuperDungeoneer(String title) {
		this.title = title;
	}

	/**
	 * Initialise the game. This can be used to load static resources. It's called
	 * before the game loop starts
	 *
	 * @param container The container holding the game
	 * @throws SlickException Throw to indicate an internal error
	 */
	@Override
	public void init(GameContainer container) throws SlickException {
		// ##### TESTING CODE ##### //
		//container.getInput().addPrimaryListener(input);
		//im = new Image("assets/image/doggo.png");
		//SpriteSheet sprites = new SpriteSheet("assets/sprite/Ultravore.gif", 20, 20, 1);
		//anim = new Animation(sprites, 0, 0, 8, 0, true, 1000, true);

		// ##### PRODUCTION CODE ##### //
		initInput(container);
		initMoves(container);
		initSpecies(container);
		initGUI(container);
		initSound(container);

		try {
			this.splash = new Image("assets/image/splash.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		anims.forEach(Animation::start);

		// ##### DEMO CODE ##### //
		//demoBattle.start(new PlayableActor[] { player, buddy }, foes.toArray(new CapturableActor[0]));
		//1battleCommandDelegate.init(demoBattle);
		movementCommandDelegate.init(dungeon.getDungeon());
	}
	private void initInput(GameContainer container) {
		Input input = container.getInput();
		input.addMouseListener(onFlyMouseListner);

		battleCommandDelegate = new BattleCommandDelegate();
		movementCommandDelegate = new MovementCommandDelegate();
		InputProvider provider = new InputProvider(input);
		handler = new DemoInputHandler(container);
		handler.addCommandDelegate(battleCommandDelegate);
		handler.addCommandDelegate(movementCommandDelegate);
		handler.registerCommands(provider);
		provider.addListener(handler);

	}
	private void initMoves(GameContainer container) {
		// ##### TESTING CODE ##### //
//		punch = new Move("Punch", "", 5, 80, 30);
//		kick = new Move("Kick", "", 4, 90, 30);
//		slap = new Move("Slap", "", 3, 100, 20);
//		elbow = new Move("Elbow", "", 4, 85, 30);
//		bite = new Move("Bite", "", 5, 90, 20);
//		slam = new Move("Slam", "", 6, 80, 10);
//
//		testMoveSet = new MoveSet();
//		testMoves = new Move[] { punch, kick, slap, elbow, bite, slam };
//		int[] levels = new int[] {-1, 2, 4, 7, 11, 16 };
//		testMoveSet.init(testMoves, levels);

		// ##### PRODUCTION CODE ##### //

		// ##### DEMO CODE ##### //
		jab = new Move("Jab", "", 2,90, 35);
		comboPunch = new Move("Combo Punch", "", 2,75, 15);
		slash = new Move("Slash", "", 3,90, 16);
		crunch = new Move("Crunch", "", 5,80, 16);
		bladeBeam = new Move("Blade Beam", "", 7,80, 6);
		rocketPunch = new Move("Rocket Punch", "", 8,78, 8);
		riskyBlow = new Move("Risky Blow", "", 16,50, 1);
		dragonfist = new Move("Dragon Fist", "", 13,70, 3);
		rage = new Move("Rage", "", 4,87, 25);
		counter = new Move("Counter", "", 7,100, 10);
		upperCut = new Move("Upper Cut", "", 8,76, 14);
		killerStab = new Move("Killer Stab", "", 10,74, 7);
		fireFist = new Move("Fire Fist", "", 9,75, 8);

		allMoveSet = new MoveSet();
		playerMoveSet = new MoveSet();
		monsterMoveSet = new MoveSet();

		allMoves = new Move[] { jab, comboPunch, slash, crunch, bladeBeam, rocketPunch, riskyBlow, dragonfist, rage, counter, upperCut, killerStab, fireFist };
		playerMoves = new Move[]  { jab, comboPunch, riskyBlow, rage, counter, upperCut };
		monsterMoves = new Move[] { jab, slash, crunch, bladeBeam, rocketPunch, dragonfist, killerStab, fireFist };

		int[] allMovesLevels = new int[allMoves.length];
		for (int i = 0; i < allMovesLevels.length; i++) {
			allMovesLevels[i] = -1;
		}

		int[] playerMovesLevels = new int[]  { 0, 3, 7, 12, 14, 20 };
		int[] monsterMovesLevels = new int[] { 0, 3, 6, 8, 13, 20, 25, 30 };

		allMoveSet.init(allMoves, allMovesLevels);
		playerMoveSet.init(playerMoves, playerMovesLevels);
		monsterMoveSet.init(monsterMoves, monsterMovesLevels);
	}
	private void initSpecies(GameContainer container) {
		initSprites(container);

		// ##### TESTING CODE ##### //
		//testSpecies = new Species("Generic Monster", testMoveSet, orcSheet);

		// ##### PRODUCTION CODE ##### //


		// ##### DEMO CODE ##### //
		playerSpecies = new Species("Human", playerMoveSet, humanSheet);
		orcSpecies = new Species("Orc", monsterMoveSet, orcSheet);
	}
	private void initSprites(GameContainer container) {
		try {
			spriteSize = 20;
			humanSheet = new SpriteSheet("assets/sprite/Human/human_regular_hair.png", spriteSize, spriteSize, 0);
			orcSheet = new SpriteSheet("assets/sprite/Orc/orc_regular_bald.png", spriteSize, spriteSize, 0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	private void initDungeon(GameContainer container) {
		// ##### TESTING CODE ##### //
		//initMonsters();
		initPlayer(container);

		actors = new ArrayList<>(PlayerActor.MAX_PARTY_SIZE+1);
		actors.add(player);
		actors.add(buddy);
		foes = new ArrayList<>();
		foeTiles = new ArrayList<>();

		demoBattle = new Battle(battleCommandDelegate);

		// ##### PRODUCTION CODE ##### //
		int dcX = DrawingUtils.DEFAULT_MARGIN, dcY = DrawingUtils.DEFAULT_MARGIN;
		int dcW = container.getWidth() - (container.getWidth() / 5) + dcX, dcH = container.getHeight() - (container.getHeight() / 5) + dcY;
		dungeon = new DungeonComponent(container, dcX, dcY, dcW, dcH, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER2_BACKGROUND_COLOR);
		dungeon.generateDungeon(container, spriteSize);
		dungeon.addPlayer(player);
	}
	private void initMonsters() {
		// ##### TESTING CODE ##### //
		//mon2 = new CapturableActor(testSpecies, new Stats(25, 3, 4, 2), testMoves);
		//mon3 = new CapturableActor(testSpecies, new Stats(15, 5, 5, 5), testMoves);

		// ##### PRODUCTION CODE ##### //


		// ##### DEMO CODE ##### //
		Move[] monsterStartMoves = new Move[] { monsterMoves[0] };
		mon2 = new CapturableActor(orcSpecies, new Stats(25, 3, 4, 2), monsterStartMoves);
		mon3 = new CapturableActor(orcSpecies, new Stats(15, 5, 5, 5), monsterStartMoves);

		mon2.setName("Orc 1");
		mon3.setName("Orc 2");
	}
	private void initPlayer(GameContainer container) {
		// ##### TESTING CODE ##### //
		//player = new Player(testSpecies, new Stats(30, 5, 5, 4), testMoves, "Player1");
		//buddy = new PlayableActor(testSpecies, new Stats(20, 4, 6, 3), testMoves);

		// ##### PRODUCTION CODE ##### //
		Move[] playerStartMoves = new Move[] { playerMoves[0] };
		Stats playerStats = new Stats(30, 5, 5, 4);
		player = new PlayerActor(playerSpecies, "You", playerStartMoves, playerStats);

		// ##### DEMO CODE ##### //
		Move[] monsterStartMoves = new Move[] { monsterMoves[0] };
		buddy = new PlayableActor(orcSpecies, "Olaf", new Stats(20, 4, 6, 3), monsterStartMoves);
		player.addToTeam(buddy);
	}
	private void initGUI(GameContainer container) {
		initDungeon(container);

		int bcpX = DrawingUtils.DEFAULT_MARGIN, bcpY = dungeon.getHeight() + DrawingUtils.DEFAULT_MARGIN;
		int bcpW = container.getWidth() - DrawingUtils.DEFAULT_MARGIN, bcpH = container.getHeight() / 5 - DrawingUtils.DEFAULT_MARGIN;
		battleControlPanel = new Panel(container, bcpX, bcpY, bcpW, bcpH, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER1_BACKGROUND_COLOR);

		int hpX = dungeon.getWidth() + DrawingUtils.DEFAULT_MARGIN, hpW = (container.getWidth() / 5);
		int hpY = DrawingUtils.DEFAULT_MARGIN, hpH = battleControlPanel.getY() / 9;
		helpPanel = new Panel(container, hpX, hpY, hpW, hpH, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER1_BACKGROUND_COLOR);

		int ppX = hpX, ppW = hpW;
		int ppY = helpPanel.getY() + helpPanel.getHeightWithMargin(), ppH = battleControlPanel.getY() - (hpY + hpH);
		partyPanel = new Panel(container, ppX, ppY, ppW, ppH, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER1_BACKGROUND_COLOR);

		int nActors = PlayerActor.MAX_PARTY_SIZE+1;
		int pmpX = 0, pmpW = partyPanel.getWidthWithMargin() - DrawingUtils.DEFAULT_MARGIN;
		int pmpY = 0, pmpH = partyPanel.getHeightWithMargin() / nActors;
		partyMemberPanes = new ActorStatPane[nActors];
		for (int i = 0; i < nActors; i++) {
			partyMemberPanes[i] = new ActorStatPane(container, (i == 0 ? player : player.getParty()[i-1]), pmpX, pmpY, pmpW, pmpH - DrawingUtils.DEFAULT_MARGIN, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER2_BACKGROUND_COLOR);
			pmpY += pmpH;
		}

		moveButtons = new LabeledButton[IBattlable.MAX_MOVES];
		for (int i = 0; i < IBattlable.MAX_MOVES; i++) {
			Object textSrc = i >= player.getMoveCount() ? " --- " : player.getLearnedMoves()[i];
			moveButtons[i] = new LabeledButton(textSrc, container, DrawingUtils.TEXT_COLOR, DrawingUtils.BUTTON_COLOR, DrawingUtils.TIER3_BACKGROUND_COLOR, new RoundedRectangle(0,0,container.getDefaultFont().getWidth("________________"), container.getDefaultFont().getLineHeight(), 6));
			if (i >= player.getMoveCount()) {
				moveButtons[i].setEnabled(false);
			}
		}
		moveGrid = new ButtonGrid(container, 1, 6, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER2_BACKGROUND_COLOR, moveButtons);

		String label = "Move Selector";
		moveSelectorLabel = new Label(container, label, 0, 0, container.getDefaultFont().getWidth("__"+label), moveGrid.getHeight(), DrawingUtils.DEFAULT_MARGIN, false, DrawingUtils.TEXT_COLOR, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER3_BACKGROUND_COLOR);

		label = "Capture!";
		captureButton = new LabeledButton(label, container, DrawingUtils.TEXT_COLOR, DrawingUtils.BUTTON_COLOR, DrawingUtils.TIER3_BACKGROUND_COLOR, new RoundedRectangle(0, 0, container.getDefaultFont().getWidth("___"+label)+DrawingUtils.DEFAULT_MARGIN, container.getDefaultFont().getLineHeight(), 6));
		targetSelectorButtons = new LabeledButton[Battle.MAX_TEAM_SIZE];
		int maxWidth = container.getDefaultFont().getWidth("No Targets");
		for (BattlableActor actor : actors) {
			int width = container.getDefaultFont().getWidth("__________" + actor.getName());
			if (width > maxWidth) {
				maxWidth = width;
			}
		}
		for (int i = 0; i < Battle.MAX_TEAM_SIZE; i++) {
			targetSelectorButtons[i] = new LabeledButton("No Targets", container, DrawingUtils.TEXT_COLOR, DrawingUtils.BUTTON_COLOR, DrawingUtils.TIER3_BACKGROUND_COLOR, new RoundedRectangle(0, 0, maxWidth+DrawingUtils.DEFAULT_MARGIN, container.getDefaultFont().getLineHeight()+DrawingUtils.DEFAULT_MARGIN, 6));
			targetSelectorButtons[i].setEnabled(false);
		}
		targetSelectionGrid = new ButtonGrid(container, 1, Battle.MAX_TEAM_SIZE, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER2_BACKGROUND_COLOR, targetSelectorButtons);
		label = "Target Selector";
		targetSelectorLabel = new Label(container, label, 0, 0, container.getDefaultFont().getWidth("__"+label), targetSelectionGrid.getHeight(), DrawingUtils.DEFAULT_MARGIN, false, DrawingUtils.TEXT_COLOR, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER3_BACKGROUND_COLOR);

		feedbackText = new TextHistory(container, 0, 0, battleControlPanel.getWidth(), battleControlPanel.getHeight() - moveGrid.getHeightWithMargin() - 2*container.getDefaultFont().getLineHeight(), DrawingUtils.DEFAULT_MARGIN, DrawingUtils.TEXT_COLOR, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER2_BACKGROUND_COLOR);
		//feedbackText.shouldDrawBorder(false);
		feedbackText.addLine("Welcome to Super Dungeoneer!");
		feedbackText.addLine("Press ESCAPE to quit at anytime!");
		feedbackText.addLine("Use WASD or the ARROW KEYS to move!");
		feedbackText.addLine("If you're standing next to a monster, press SPACEBAR to begin a battle!");
		feedbackText.addLine("If you're in a battle use the mouse or press 1-6 to choose a move, then choose a target if there is more than one foe left!");
		feedbackText.addLine("If the battle goes bad, press R to run away!");


		battleControlPanel.addChild(moveGrid, battleControlPanel.getWidth() - moveGrid.getWidthWithMargin(), DrawingUtils.DEFAULT_MARGIN);
		int mslX = battleControlPanel.getWidth()-moveGrid.getWidthWithMargin()-moveSelectorLabel.getWidthWithMargin();
		battleControlPanel.addChild(moveSelectorLabel, mslX, DrawingUtils.DEFAULT_MARGIN);
		battleControlPanel.addChild(captureButton, mslX - captureButton.getWidthWithMargin(), DrawingUtils.DEFAULT_MARGIN);
		battleControlPanel.addChild(targetSelectionGrid, DrawingUtils.DEFAULT_MARGIN, DrawingUtils.DEFAULT_MARGIN);
		battleControlPanel.addChild(targetSelectorLabel, targetSelectionGrid.getWidthWithMargin(), DrawingUtils.DEFAULT_MARGIN);
		battleControlPanel.addChild(feedbackText, 0, battleControlPanel.getHeight() - feedbackText.getHeight());

		helpText = new TextHistory(container, 0, 0, hpW, hpH - (hpH / 5), DrawingUtils.DEFAULT_MARGIN, DrawingUtils.TEXT_COLOR, DrawingUtils.FOREGROUND_COLOR, DrawingUtils.TIER2_BACKGROUND_COLOR);
		helpText.addLine("Use the mouse to select a target \nafter selecting a move!");
		helpText.addLine("");
		helpText.addLine("1-6 -- Select Move (or use the mouse)");
		helpText.addLine("Esc -- Quit Game");

		helpPanel.addChild(helpText, 0, 0);

		for (int i = partyMemberPanes.length -1; i >= 0; i--) {
			pmpY -= pmpH;
			partyPanel.addChild(partyMemberPanes[i], pmpX, pmpY);
		}
	}
	private void initSound(GameContainer container) {
		SoundStore.get().init();
		container.setMusicOn(true);
		container.setMusicVolume(1.0f);

		/*try {
			bgm = SoundStore.get().getOgg("assets/sound/bgm/FromHere.ogg");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}*/
	}

	/**
	 * Update the game logic here. No rendering should take place in this method
	 * though it won't do any harm.
	 *
	 * @param container The container holing this game
	 * @param delta     The amount of time thats passed since last update in milliseconds
	 * @throws SlickException Throw to indicate an internal error
	 */
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// ##### TESTING CODE ##### //
		//int bufid = -1;
		//if (!bgm.isPlaying()) {
			//bufid = bgm.playAsMusic(1.0f, 0.05f, false);
		//}

		// ##### PRODUCTION CODE ##### //
		if (showingSplash) {
			showingSplash = countDown-- > 0;
			return;
		}
		anims.forEach(anim -> anim.update(delta));
		anims.forEach(anim -> anim.setSpeed(0.05f));

		// ##### DEMO CODE ##### //
		if (demoBattle.isStarted()) {
			battleControlPanel.setEnabled(true);
			if (demoBattle.isOver()) {
				if (demoBattle.playerVictory()) {
					foes.removeAll(demoBattle.getFoes());
					foeTiles.removeIf(tile -> foes.stream().noneMatch(capturableActor -> tile.getOccupant().equals(capturableActor)));
					foeTiles.forEach(tile -> {
						if (demoBattle.getFoes().stream().anyMatch(capturableActor -> tile.getOccupant().equals(capturableActor))) {
							dungeon.removeFoeComponent((Actor) tile.getOccupant());
							tile.setStateAndOccupant(Tile.PASSAGE, null);

						}
					});
				} else {
					System.out.println("YOU LOST!!");
				}
				demoBattle.clearState();
			} else {
				IBattlable activeActor = demoBattle.getActiveActor();
				demoBattle.advanceTurn(moveSlot);

				if (activeActor != (demoBattle.getActiveActor())) {
					moveSlot = -1;
					activeActor = demoBattle.getActiveActor();
					if (activeActor != null) {
						for (int i = 0; i < IBattlable.MAX_MOVES; i++) {
							boolean hasMoreMoves = i < activeActor.getMoveCount();
							Object textSrc = hasMoreMoves ? activeActor.getLearnedMoves()[i] : "";
							moveButtons[i].setLabelText(textSrc);
							moveButtons[i].setAcceptingInput(hasMoreMoves);
						}
					}
				}
				updateTargetSelector(container);

				for (BattlableActor a : demoBattle.getFoes()) {
					if (a instanceof ICapturable) {
						ICapturable actor = (ICapturable) a;
						if (actor.wasJustCaptured()) {
							int nMembers = player.getPartySize();
							partyMemberPanes[nMembers].setActor(a);
						}
					}

				}
			}
		} else {
			moveGrid.setEnabled(false);
			targetSelectionGrid.setEnabled(false);
			player.setHP(player.getStats().maxHP());
			for (PlayableActor buddy : player.getParty()) {
				if (buddy == null || buddy.isKOed()) { continue; }
				buddy.setHP(buddy.getStats().maxHP());
			}
		}
	}

	/**
	 * Render the game's screen here.
	 *
	 * @param container The container holing this game
	 * @param g         The graphics context that can be used to render. However, normal rendering
	 *                  routines can also be used.
	 * @throws SlickException Throw to indicate a internal error
	 */
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		// ##### TESTING CODE ##### //
		//g.drawString("Welcome to our SuperDungeoneer!",980,20);
		//g.drawImage(im, 10+input.getxOff(), 120+input.getyOff());
		//g.drawAnimation(anim, 10, 30);

		// ##### PRODUCTION CODE ##### //
		g.clear();

		g.setBackground(DrawingUtils.BASE_COLOR);
		g.setAntiAlias(true);
		g.setLineWidth(4.0f);

		if (showingSplash) {
			splash.draw((container.getWidth() / 2) - (splash.getWidth() / 2), (container.getHeight() / 2) - (splash.getHeight() / 2));
			return;
		}

		drawHUD(container, g);
		drawBattle(container, g);
		/*Animation deathAnim = mon2.getDeathAnimation();
		if (deathAnim != null) {
			deathAnim.setLooping(false);
			//g.drawAnimation(deathAnim, 20, 200);
			//deathAnim.setDuration(0, 2000);
		}*/

		g.flush();
	}

	private void drawHUD(GameContainer container, Graphics g) throws SlickException {
		dungeon.render(container, g);
		helpPanel.render(container, g);
		partyPanel.render(container, g);
		drawBattleHUD(container, g);
		if (demoBattle.isStarted()) {
			drawMoves(container, g);
		}
	}

	private void drawBattleHUD(GameContainer container, Graphics g) throws SlickException {
		Color oldC = g.getColor();

		battleControlPanel.render(container, g);
		ArrayList<CapturableActor> foes = demoBattle.getFoes();
		for (int i = 0; i < foes.size(); i++) {
			if (foes.get(i) != null){
				if (foes.get(i).isIncapacitated()) {
					LabeledButton btn = targetSelectorButtons[i];
					//DrawingUtils.drawDisabledOverlay(container, g, btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight());
				}
			}
		}
		if (!demoBattle.isOver()) {
			g.setColor(DrawingUtils.FOREGROUND_COLOR);
			int foeX = COMPONENT_SPACING, foeY = COMPONENT_SPACING;
			for (int i = 0; i < foes.size(); i++) {
				float w = ( ( foes.get(i).HP() * 1.0f ) / foes.get(i).getStats().maxHP() );
				g.setColor(DrawingUtils.FOREGROUND_COLOR);
				g.fillRoundRect(foeX - 2, foeY - 2, 204, 24, 8);
				g.fill(new RoundedRectangle(foeX, foeY, w * 200, 20, 8), new GradientFill(foeX, foeY, Color.red, foeX + 200, foeX + 20, Color.green));
				foeY += 35;
			}

			foeX = 240;
			foeY = COMPONENT_SPACING;
			for (int i = 0; i < foes.size(); i++) {
				g.getFont().drawString(foeX, foeY, foes.get(i).getName() + " HP: " + foes.get(i).HP() + " / "+foes.get(i).getStats().maxHP(), Color.white);
				foeY += 15 + g.getFont().getLineHeight();
			}

			IBattlable active = demoBattle.getActiveActor();
			ActorStatPane activeMemberPanel = null;
			for (int i = 0; active != null && i < partyMemberPanes.length; i++) {
				if (active == partyMemberPanes[i].getActor()) {
					activeMemberPanel = partyMemberPanes[i];
					break;
				}
			}
			if (activeMemberPanel != null) {
				int x = activeMemberPanel.getXWithMargin(), y = activeMemberPanel.getYWithMargin();
				int w = activeMemberPanel.getWidthWithMargin(), h = activeMemberPanel.getHeightWithMargin();

				g.setColor(SELECTION_COLOR);
				g.drawRect(x, y, w, h);
			}
		}

		g.setColor(oldC);
	}

	private void drawMoves(GameContainer container, Graphics g) throws SlickException {
		if (moveSlot > 0) {
			int x = moveButtons[moveSlot-1].getX() - 2;
			int y = moveButtons[moveSlot-1].getY() - 2;
			int w = moveButtons[moveSlot-1].getWidth() + 4;
			int h = moveButtons[moveSlot-1].getHeight() + 4;
			Color oldC = g.getColor();
			float oldLW = g.getLineWidth();

			g.setLineWidth(1.0f);
			g.setColor(SELECTION_COLOR);
			g.drawRect(x, y, w, h);

			g.setColor(oldC);
			g.setLineWidth(oldLW);
		}
	}

	private void drawBattle(GameContainer container, Graphics g) throws SlickException {

	}

	private void drawTargetSelector(GameContainer container, Graphics g) throws SlickException {
		targetSelectionGrid.setEnabled(needsTarget && moveSlot > 0);
		//targetSelectionGrid.render(container, g);
	}


	private void updateTargetSelector(GameContainer container) throws SlickException {
		targetSelectionGrid.setEnabled(needsTarget);
	}

	public void selectTarget(IBattlable[] targets) {
		if (needsTarget || selectedTarget) { return; }

		for (int i = 1; i <= targets.length; i++) {
			targetSelectorButtons[i-1].setLabelText(i+": "+targets[i-1].getName());
		}

		needsTarget = true;
	}

	/**
	 * Notification that a game close has been requested
	 *
	 * @return True if the game should close
	 */
	@Override
	public boolean closeRequested() {
		return true;
	}

	/**
	 * Get the title of this game
	 *
	 * @return The title of the game
	 */
	@Override
	public String getTitle() {
		return this.title;
	}

	public static SuperDungeoneer getInstance() {
		return instance;
	}

	public PlayerActor getPlayer() { return this.player; }
	public int[] getPlayerPos() { return dungeon.getPlayerPos(); }
	public void movePlayer(int x, int y) {
		dungeon.offsetPlayerPos(new int[] { x*spriteSize, y*spriteSize });
	}

	public LabeledButton[] getMoveButtons(){
		return this.moveButtons;
	}

	public int getSelectedTargetSlot() {
		if (!this.selectedTarget) {
			throw new IllegalStateException("Target is invalid or not selected!");
		}

		int ret = this.targetSlot;
		this.targetSlot = -1;
		this.selectedTarget = false;
		return ret;
	}

	private void setSelectedTargetSlot(int targetSlot) {
		this.targetSlot = targetSlot;
		this.selectedTarget = targetSlot > 0 && targetSlot <= Battle.MAX_TEAM_SIZE;
		this.needsTarget = !this.selectedTarget;
		this.targetSelectionGrid.setEnabled(this.needsTarget);
	}

	public void setMoveSlot(int moveSlot) {
		this.moveSlot = moveSlot;
	}

	public void addFeedback(String text) {
		this.feedbackText.addLine(text);
	}

	public void addAnim(Animation anim) {
		if (anims == null) {
			anims = new ArrayList<Animation>();
		}
		anims.add(anim);
	}
	public void addActor(PlayableActor a) {
		actors.add(a);
	}
	public void addMonster(Tile t, CapturableActor a) {
		foes.add(a);
		foeTiles.add(t);
	}
	public CapturableActor getRandMonster(Random rand) {
		Species[] species = new Species[] { orcSpecies };
		int speciesIdx = rand.nextInt(species.length);
		return new CapturableActor(species[speciesIdx], new Stats(rand.nextInt(10)+10, rand.nextInt(10)+1, rand.nextInt(10)+1, rand.nextInt(5)+1), new Move[] { monsterMoves[0] } );
	}

	public boolean isBattling() { return demoBattle.isStarted() && !demoBattle.isOver(); }
	public void interacted() {
		int x = getPlayerPos()[0], y = getPlayerPos()[1];
		Dungeon d = dungeon.getDungeon();
		CapturableActor foe = null;
		Tile[] tiles = new Tile[] { d.getTileAt(x-1,y), d.getTileAt(x, y-1), d.getTileAt(x+1,y), d.getTileAt(x, y+1) };
		for (int i = 0; i < tiles.length; i++) {
			boolean found = foeTiles.contains(tiles[i]);
			if (found) {
				foe = (CapturableActor) tiles[i].getOccupant();
			}
		}
		/*
		try {
			if (d.getTileAt(x + 1, y).getState() == Tile.ACTOR && d.getTileAt(x + 1, y).getOccupant() != player) {
				foe = (CapturableActor) d.getTileAt(x + 1, y).getOccupant();
			}
		} catch (IllegalArgumentException iae) {}
		try {
			if (d.getTileAt(x, y - 1).getState() == Tile.ACTOR && d.getTileAt(x, y - 1).getOccupant() != player) {
				foe = (CapturableActor) d.getTileAt(x, y - 1).getOccupant();
			}
		} catch (IllegalArgumentException iae) {}
		try {
			if (d.getTileAt(x - 1, y).getState() == Tile.ACTOR && d.getTileAt(x - 1, y).getOccupant() != player) {
				foe = (CapturableActor) d.getTileAt(x - 1, y).getOccupant();
			}
		} catch (IllegalArgumentException iae) {}
		try {
			if (d.getTileAt(x, y + 1).getState() == Tile.ACTOR && d.getTileAt(x, y + 1).getOccupant() != player) {
				foe = (CapturableActor) d.getTileAt(x, y + 1).getOccupant();
			}
		} catch (IllegalArgumentException iae) {}
		*/

		if (foe != null) {
			try {
				demoBattle.start(new PlayableActor[] { player, player.getParty()[0] }, new CapturableActor[] { foe, (CapturableActor) foe.clone() });
				battleCommandDelegate.init(demoBattle);
				System.out.println("Battle Time!");
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
	public void runAway() {
		demoBattle.clearState();
	}
}
