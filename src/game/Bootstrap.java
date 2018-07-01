package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bootstrap {

	public static void main(String[] args) {
		System.setProperty("java.library.path", "./libs");
		System.setProperty("org.lwjgl.librarypath", new File("./libs/natives").getAbsolutePath());

		Capstone game = Capstone.getInstance();
		System.out.println("Welcome to "+game.getTitle());

		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(game);
			appgc.setDisplayMode(appgc.getScreenWidth(), appgc.getScreenHeight(), true);
			appgc.setShowFPS(false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Capstone.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
