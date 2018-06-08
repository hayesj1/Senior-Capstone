package game;

import game.input.InputHandler;
import org.newdawn.slick.*;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Capstone implements Game {

	private String title;
	private Image im = null;
	private Animation anim = null;
	private Audio bgm = null;

	private InputHandler input = new InputHandler();
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
			bgm = SoundStore.get().getOgg("./FromHere.ogg");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		im = new Image("./doggo.png");
		SpriteSheet sprites = new SpriteSheet("./Ultravore.gif", 20, 20, 1);
		anim = new Animation(sprites, 0, 0, 8, 0, true, 1000, true);

		// ##### PRODUCTION CODE ##### //
		initSpecies();
		initMoves();
		initPlayer();
		initDungeon();
	}

	private void initSpecies() {

	}

	private void initMoves() {

	}

	private void initPlayer() {

	}

	private void initDungeon() {
		initMonsters();
		initBoss();
	}

	private void initMonsters() {

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
			bufid = bgm.playAsMusic(1.0f, 1.0f, false);
		}

		// ##### PRODUCTION CODE ##### //

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
		g.drawString("Welcome to our Capstone!",10,10);
		g.drawImage(im, 10+input.getxOff(), 120+input.getyOff());
		g.drawAnimation(anim, 10, 30);

		// ##### PRODUCTION CODE ##### //
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
		Capstone game = new Capstone("Capstone");
		System.out.println("Welcome to "+game.getTitle());

		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(game);
			appgc.setDisplayMode(640, 480, false);
			appgc.setShowFPS(false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Capstone.class.getName()).log(Level.SEVERE, null, ex);
		}


	}
}

