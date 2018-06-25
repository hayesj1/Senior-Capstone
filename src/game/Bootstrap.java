package game;

import java.io.File;

public class Bootstrap {

	public static void main(String[] args) {
		System.setProperty("java.library.path", "./libs");
		System.setProperty("org.lwjgl.librarypath", new File("./libs/natives").getAbsolutePath());


		Capstone.main(args);
	}
}
