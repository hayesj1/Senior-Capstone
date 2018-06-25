package game;

import game.battle.Battle;
import game.character.*;
import game.character.moves.Move;
import game.character.moves.MoveSet;
import game.input.BattleCommandDelegate;
import game.input.DemoInputHandler;
import game.input.InputHandler;
import org.newdawn.slick.*;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Capstone implements Game {

	private String title;
	private Image im = null;
	private Animation anim = null;
	private Audio bgm = null;
	private InputProvider provider = null;
	private DemoInputHandler handler = new DemoInputHandler();

	private InputHandler input = new InputHandler();
	private MoveSet testMoveSet;
	private Species testSpecies;
	private Move punch;
	private Move kick;
	private Move slap;
	private Move elbow;
	private Move bite;
	private Move slam;
	private Player player;
	private Move[] moves;
	private Monster mon3;
	private Monster mon2;
	private Monster mon1;
	private Battle demoBattle;
	private BattleCommandDelegate battleCommandDelegate;
	private SpriteSheet orcSheet;

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
		container.getInput().addPrimaryListener(input);
		SoundStore.get().init();
		container.setMusicOn(true);
		container.setMusicVolume(5.0f);
		try {
			bgm = SoundStore.get().getOgg("assets/sound/bgm/FromHere.ogg");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		im = new Image("assets/image/doggo.png");
		SpriteSheet sprites = new SpriteSheet("assets/sprite/Ultravore.gif", 20, 20, 1);
		anim = new Animation(sprites, 0, 0, 8, 0, true, 1000, true);

		// ##### PRODUCTION CODE ##### //
		initInput(container);
		initMoves(container);
		initSpecies(container);
		initPlayer(container);
		initDungeon(container);

		// ##### DEMO CODE ##### //
		demoBattle.start(new IBattlable[] { player, mon1 }, new IBattlable[] { mon2, mon3 });
		battleCommandDelegate.init(demoBattle);
	}

	private void initInput(GameContainer container) {
		provider = new InputProvider(container.getInput());
		provider.addListener(handler);
		handler.registerCommands(provider);
		battleCommandDelegate = new BattleCommandDelegate();
		handler.addCommandDelegate(battleCommandDelegate);
	}

	private void initMoves(GameContainer container) {
		punch = new Move("Punch", "", 5, 80, 30);
		kick = new Move("Kick", "", 4, 90, 30);
		slap = new Move("Slap", "", 3, 100, 20);
		elbow = new Move("Elbow", "", 4, 85, 30);
		bite = new Move("Bite", "", 5, 90, 20);
		slam = new Move("Slam", "", 6, 80, 10);
		testMoveSet = new MoveSet();
		testMoveSet.putMove(punch, -1);
		testMoveSet.putMove(kick, 2);
		testMoveSet.putMove(slap, 4);
		testMoveSet.putMove(elbow, 7);
		testMoveSet.putMove(bite,11);
		testMoveSet.putMove(slam,16);
	}

	private void initSpecies(GameContainer container) {
		initSprites(container);
		testSpecies = new Species("Test Uno", testMoveSet, orcSheet) {};
	}

	private void initSprites(GameContainer container) {
		try {
			orcSheet = new SpriteSheet("assets/sprite/orc/orc_regular_hair.png", 20, 20, 0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void initPlayer(GameContainer container) {
		moves = new Move[] { punch, kick, slap, elbow, bite, slam };
		player = new Player(testSpecies, new Stats(30, 5, 5, 4), moves, "Player1");
		mon1 = new Monster(testSpecies, new Stats(20, 4, 6, 3), moves);
		player.addMonsterToParty(mon1);
	}

	private void initDungeon(GameContainer container) {
		initMonsters();
		initBoss();

		demoBattle = new Battle(battleCommandDelegate);
	}

	private void initMonsters() {
		mon2 = new Monster(testSpecies, new Stats(25, 3, 4, 2), moves);
		mon3 = new Monster(testSpecies, new Stats(15, 5, 5, 5), moves);
	}

	private void initBoss() {

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
		int bufid = -1;
		if (!bgm.isPlaying()) {
			bufid = bgm.playAsMusic(1.0f, 0.05f, false);
		}

		// ##### PRODUCTION CODE ##### //

		// ##### DEMO CODE ##### //
		if (demoBattle.isOver()) {
			handler.removeCommandDelegate(battleCommandDelegate);
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
		g.setBackground(Color.darkGray);
		g.setAntiAlias(true);
		g.setLineWidth(2.0f);
		// ##### TESTING CODE ##### //
		g.drawString("Welcome to our Capstone!",980,20);
		//g.drawImage(im, 10+input.getxOff(), 120+input.getyOff());
		//g.drawAnimation(anim, 10, 30);
		if (demoBattle.isOver()) {
			g.drawString(demoBattle.playerVictory() ? "You WIN!" : "You LOSE!", 140, 40);
		} else {
			g.drawString("Keep on battling!", 140,40);
			g.drawString("Your Health: "+player.HP(),140,60);
			g.drawString("Your Buddies's Health: "+mon1.HP(), 140, 80);
			g.drawString("Enemy1's Health: "+mon2.HP(), 140, 100);
			g.drawString("Enemy2's Health: "+mon3.HP(), 140, 120);
		}

		// ##### PRODUCTION CODE ##### //
		drawHUD(container, g);
		Animation deathAnim = mon2.getDeathAnimation();
		if (deathAnim != null) {
			deathAnim.setLooping(false);
			g.drawAnimation(deathAnim, 20, 200);
			//deathAnim.setDuration(0, 2000);
		}
	}

	private void drawHUD(GameContainer container, Graphics g) {
		drawStats(container, g);
		drawMoves(container, g);
	}

	private void drawStats(GameContainer container, Graphics g) {
		PlayableCharacter[] actors = new PlayableCharacter[] { player, mon1, mon2, mon3 };
		g.setColor(Color.lightGray);
		int x = 20, y = 20;
		for (int i = 0; i < 4; i++) {
			float w = actors[i].HP() / actors[i].getStats().maxHP() * 100;
			g.setColor(Color.lightGray);
			g.fillRoundRect(x-2, y-2, 98, 18, 15);
			g.fill(new RoundedRectangle(x, y, w+5, 20, 15, 16), new GradientFill(x,y,Color.red, x+100,y+20,Color.green));
			y += 35;
		}
	}

	private void drawMoves(GameContainer container, Graphics g) {
		g.setColor(Color.white);
		moves = player.getLearnedMoves();
		int x = 20, y = 960 - 40;
		for (int i = 0, j = 1; i < moves.length; i++) {
			if (moves[i] == null) { continue; }
			String mv = (j++)+": "+moves[i].getName();
			g.drawString(mv, x, y);
			x += mv.length()*10;
		}

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

	public static void main(String[] args) {
		System.setProperty("java.library.path", "libs");
		//Extracted from Distributing Your LWJGL Application
		System.setProperty("org.lwjgl.librarypath", new File("libs/natives").getAbsolutePath());


		Capstone game = new Capstone("Capstone");
		System.out.println("Welcome to "+game.getTitle());

		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(game);
			appgc.setDisplayMode(1280, 960, false);
			appgc.setShowFPS(false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Capstone.class.getName()).log(Level.SEVERE, null, ex);
		}


	}
}

