package game;

import game.battle.Battle;
import game.character.*;
import game.character.moves.Move;
import game.character.moves.MoveSet;
import game.gui.ButtonGrid;
import game.gui.LabeledButton;
import game.gui.Panel;
import game.input.BattleCommandDelegate;
import game.input.DemoInputHandler;
import org.newdawn.slick.*;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

public class Capstone implements Game {
	private static Capstone instance = new Capstone("Capstone");
	private static final int BUTTON_SPACING = 5;

	public static final Color SELECTION_COLOR = Color.orange;


	private String title;
	private Image im = null;
	private Animation anim = null;
	private Audio bgm = null;
	private InputProvider provider = null;
	private DemoInputHandler handler = null;
	private MouseListener onFlyMouseListner = new MouseListener() {
		private boolean isAcceptingInput = false;
		@Override
		public void mousePressed(int button, int x, int y) { }

		@Override
		public void mouseReleased(int button, int x, int y) {
			if (button == Input.MOUSE_LEFT_BUTTON) {
				if (needsTarget) {
					for (int i = 0; i < targetSelectorButtons.length; i++) {
						if (targetSelectorButtons[i].getBounds().contains(x, y)) {
							setSelectedTargetSlot(Integer.valueOf(targetSelectorButtons[i].getLabel().substring(0, 1)));
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


	private MoveSet testMoveSet;
	private Move[] testMoves;
	private Species testSpecies;
	private Move punch;
	private Move kick;
	private Move slap;
	private Move elbow;
	private Move bite;
	private Move slam;

	private MoveSet allMoveSet;
	private MoveSet playerMoveSet;
	private MoveSet monsterMoveSet;
	private Move[] allMoves;
	private Move[] playerMoves;
	private Move[] monsterMoves;
	private Species playerSpecies;
	private Species monsterSpecies;
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

	private Player player;
	private Monster mon1;
	private Monster mon2;
	private Monster mon3;
	private PlayableCharacter[] actors;

	private Battle demoBattle;
	private BattleCommandDelegate battleCommandDelegate;
	private SpriteSheet orcSheet;

	private Panel battleControlPanel;
	private LabeledButton[] moveButtons;
	private ButtonGrid moveGrid;
	private LabeledButton[] targetSelectorButtons;
	private ButtonGrid targetSelectionGrid;

	private boolean needsTarget = false;
	private boolean selectedTarget = false;
	private int targetSlot = -1;
	private int moveSlot = -1;

	/**
	 * Create a new basic game
	 *
	 * @param title The title for the game
	 */
	public Capstone(String title) {
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
		initPlayer(container);
		initDungeon(container);
		initGUI(container);
		initSound(container);

		// ##### DEMO CODE ##### //
		actors = new PlayableCharacter[] { player, mon1, mon2, mon3 };

		demoBattle.start(new IBattlable[] { player, mon1 }, new IBattlable[] { mon2, mon3 });
		battleCommandDelegate.init(demoBattle);
	}

	private void initInput(GameContainer container) {
		Input input = container.getInput();
		input.addMouseListener(onFlyMouseListner);

		battleCommandDelegate = new BattleCommandDelegate();
		provider = new InputProvider(input);
		handler = new DemoInputHandler(container);
		handler.addCommandDelegate(battleCommandDelegate);
		handler.registerCommands(provider);
		provider.addListener(handler);

	}

	private void initMoves(GameContainer container) {
		// ##### TESTING CODE ##### //
		punch = new Move("Punch", "", 5, 80, 30);
		kick = new Move("Kick", "", 4, 90, 30);
		slap = new Move("Slap", "", 3, 100, 20);
		elbow = new Move("Elbow", "", 4, 85, 30);
		bite = new Move("Bite", "", 5, 90, 20);
		slam = new Move("Slam", "", 6, 80, 10);

		testMoveSet = new MoveSet();
		testMoves = new Move[] { punch, kick, slap, elbow, bite, slam };
		int[] levels = new int[] {-1, 2, 4, 7, 11, 16 };
		testMoveSet.init(testMoves, levels);

		// ##### PRODUCTION CODE ##### //

		// ##### DEMO CODE ##### //
		jab = new Move("Jab", "", 2,100, 35);
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

		int[] playerMovesLevels = new int[]  { 0, 3, 7, 12, 14, 20 }; //Josh please fill in the proper levels or remove this comment if they're fine
		int[] monsterMovesLevels = new int[] { 0, 3, 6, 8, 13, 20, 25, 30 }; //Josh please fill in the proper levels or remove this comment if they're fine

		allMoveSet.init(allMoves, allMovesLevels);
		playerMoveSet.init(playerMoves, playerMovesLevels);
		monsterMoveSet.init(monsterMoves, monsterMovesLevels);
	}

	private void initSpecies(GameContainer container) {
		initSprites(container);

		// ##### TESTING CODE ##### //
		testSpecies = new Species("Test Species", testMoveSet, orcSheet);

		// ##### PRODUCTION CODE ##### //


		// ##### DEMO CODE ##### //
		playerSpecies = new Species("Human", playerMoveSet, orcSheet);
		monsterSpecies = new Species("???", monsterMoveSet, orcSheet); // Josh please replace ??? with a good species name
	}

	private void initSprites(GameContainer container) {
		try {
			orcSheet = new SpriteSheet("assets/sprite/orc/orc_regular_hair.png", 20, 20, 0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void initPlayer(GameContainer container) {
		// ##### TESTING CODE ##### //
		//player = new Player(testSpecies, new Stats(30, 5, 5, 4), testMoves, "Player1");
		mon1 = new Monster(testSpecies, new Stats(20, 4, 6, 3), testMoves); // Josh please fix these stats or agree with them and remove this comment

		// ##### PRODUCTION CODE ##### //
		Move[] playerStartMoves = new Move[] { playerMoves[0] };
		Stats playerStats = new Stats(30, 5, 5, 4); // Josh please fix these values or agree with them and remove this comment
		player = new Player(playerSpecies, playerStats, playerStartMoves, "Player1");

		// ##### DEMO CODE ##### //
		player.addToTeam(mon1);
	}

	private void initDungeon(GameContainer container) {
		initMonsters();
		initBoss();

		demoBattle = new Battle(battleCommandDelegate);
	}

	private void initMonsters() {
		mon2 = new Monster(testSpecies, new Stats(25, 3, 4, 2), testMoves);
		mon3 = new Monster(testSpecies, new Stats(15, 5, 5, 5), testMoves);
	}

	private void initBoss() {

	}

	private void initGUI(GameContainer container) {
		int bcpX = 0, bcpW = container.getWidth(), bcpY = (container.getHeight() / 5) * 4, bcpH = container.getHeight() / 5;
		battleControlPanel = new Panel(container, bcpX, bcpY, bcpW, bcpH);
		battleControlPanel.setBackgroundColor(Color.lightGray);

		moveButtons = new LabeledButton[IBattlable.MAX_MOVES];
		for (int i = 0; i < IBattlable.MAX_MOVES; i++) {
			moveButtons[i] = new LabeledButton(playerMoves[i], container, container.getDefaultFont(), null, new RoundedRectangle(0,0,200,container.getDefaultFont().getLineHeight()+10, 8));
		}
		moveGrid = new ButtonGrid(container, 1, 6, BUTTON_SPACING, moveButtons);
		moveGrid.setBackgroundColor(Color.transparent);

		targetSelectorButtons = new LabeledButton[Battle.MAX_TEAM_SIZE];
		for (int i = 0; i < Battle.MAX_TEAM_SIZE; i++) {
			targetSelectorButtons[i] = new LabeledButton("", container, container.getDefaultFont(), null, new RoundedRectangle(0, 0, 150, container.getDefaultFont().getLineHeight()+10, 8));
		}
		targetSelectionGrid = new ButtonGrid(container, 1, Battle.MAX_TEAM_SIZE, BUTTON_SPACING, targetSelectorButtons);
		targetSelectionGrid.setBackgroundColor(Color.transparent);

		battleControlPanel.addChild(moveGrid, battleControlPanel.getWidth() - moveGrid.getWidth(), 0);
		battleControlPanel.addChild(targetSelectionGrid, 0, 0);
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

		// ##### DEMO CODE ##### //
		if (demoBattle.isOver()) {
			handler.removeCommandDelegate(battleCommandDelegate);
		} else {
			demoBattle.advanceTurn(moveSlot);

			IBattlable activeActor = demoBattle.getActiveActor();
			for (int i = 0; i < IBattlable.MAX_MOVES; i++) {
				boolean hasMoreMoves = i < activeActor.getMoveCount();
				Object textSrc = hasMoreMoves ? activeActor.getLearnedMoves()[i] : "";
				moveButtons[i].setText(textSrc);
				moveButtons[i].setAcceptingInput(hasMoreMoves);
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
		//g.drawString("Welcome to our Capstone!",980,20);
		//g.drawImage(im, 10+input.getxOff(), 120+input.getyOff());
		//g.drawAnimation(anim, 10, 30);

		// ##### PRODUCTION CODE ##### //
		g.clear();

		g.setBackground(Color.darkGray);
		g.setAntiAlias(true);
		g.setLineWidth(2.0f);

		if (demoBattle.isOver()) {
			g.drawString(demoBattle.playerVictory() ? "You WIN!" : "You LOSE!", 240, 40);
		} else {
			g.drawString("Keep on battling!", 240,40);
		}

		drawHUD(container, g);
		drawBattle(container, g);
		Animation deathAnim = mon2.getDeathAnimation();
		if (deathAnim != null) {
			deathAnim.setLooping(false);
			g.drawAnimation(deathAnim, 20, 200);
			//deathAnim.setDuration(0, 2000);
		}

		g.flush();
	}

	private void drawHUD(GameContainer container, Graphics g) throws SlickException {
		if (demoBattle.isStarted()) {
			if (demoBattle.isOver()) {
				battleControlPanel.setEnabled(false);
			}
			drawBattleHUD(container, g);
			drawMoves(container, g);
		}
	}

	private void drawBattleHUD(GameContainer container, Graphics g) throws SlickException {
		battleControlPanel.render(container, g);
		if (!demoBattle.isOver()) {
			g.setColor(Color.lightGray);
			int x = 20, y = 20;
			for (PlayableCharacter actor : actors) {
				float w = ( ( actor.HP() * 1.0f ) / actor.getStats().maxHP() );
				//g.setColor(Color.lightGray);
				g.fillRoundRect(x - 2, y - 2, 204, 24, 8);
				g.fill(new RoundedRectangle(x, y, w * 200, 20, 8), new GradientFill(x, y, Color.red, x + 200, y + 20, Color.green));
				y += 35;
			}

			IBattlable active = demoBattle.getActiveActor();
			x = 240;
			y = 60;
			for (PlayableCharacter actor : actors) {
				Color color = ( actor == active ) ? SELECTION_COLOR : Color.lightGray;
				g.getFont().drawString(x, y, actor.getName() + " HP: " + actor.HP(), color);
				y += 20;
			}
		}
	}

	private void drawMoves(GameContainer container, Graphics g) throws SlickException {
		//moveGrid.setLocation(20, container.getHeight()-(moveGrid.getHeight()+20));
		//moveGrid.render(container, g);
		if (moveSlot > 0) {
			int x = moveButtons[moveSlot-1].getX() - 2;
			int y = moveButtons[moveSlot-1].getY() - 2;
			int w = moveButtons[moveSlot-1].getWidth() + 3;
			int h = moveButtons[moveSlot-1].getHeight() + 3;
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
			drawTargetSelector(container, g);
	}

	private void drawTargetSelector(GameContainer container, Graphics g) throws SlickException {
		if (!needsTarget) {
			//targetSelectionGrid.setLocation(container.getWidth(), container.getHeight());
			targetSelectionGrid.setEnabled(false);
			//targetSelectionGrid.setShown(false);
		} else {
			//int x = container.getWidth() / 2 - targetSelectionGrid.getWidth();
			//int y = moveGrid.getY() - targetSelectionGrid.getHeight() - BUTTON_SPACING;
			//targetSelectionGrid.setLocation(x ,y);
			targetSelectionGrid.setEnabled(true);
			targetSelectionGrid.setShown(true);

		}

		targetSelectionGrid.render(container, g);
	}

	public void selectTarget(IBattlable[] targets) {
		if (needsTarget || selectedTarget) { return; }

		for (int i = 1; i <= targets.length; i++) {
			targetSelectorButtons[i-1].setText(i+": "+targets[i-1].getName());
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

	public static Capstone getInstance() {
		return instance;
	}

	public LabeledButton[] getMoveButtons(){
		return this.moveButtons;
	}

	public LabeledButton[] getTargetSelectorButtons() {
		return this.targetSelectorButtons;
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

	public void setSelectedTargetSlot(int targetSlot) {
		this.targetSlot = targetSlot;
		this.selectedTarget = targetSlot > 0 && targetSlot < 3;
		this.needsTarget = !this.selectedTarget;
		this.targetSelectionGrid.setEnabled(this.needsTarget);
	}

	public void setMoveSlot(int moveSlot) {
		this.moveSlot = moveSlot;
	}
}
