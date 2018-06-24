package game;

import java.io.File;

public class Bootstrap {

	public static void main(String[] args) {
//		String osName = System.getProperties().getProperty("os.name").toLowerCase();
//		String os = "";
//		if (osName.contains("windows")) {
//			os = "windows";
//		} else if (osName.contains("macos")) {
//			os = "macosx";
//		} else {
//			os = "linux";
//		}

		System.setProperty("java.library.path", "./libs");
		System.setProperty("org.lwjgl.librarypath", new File("./libs/natives/").getAbsolutePath());


		Capstone.main(args);
	}
}
