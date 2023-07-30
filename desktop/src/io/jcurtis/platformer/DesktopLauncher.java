package io.jcurtis.platformer;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(MainGame.VIRTUAL_WIDTH * 4, MainGame.VIRTUAL_HEIGHT * 4);
		config.setForegroundFPS(60);
		config.setTitle("LibGDXPlatformer");
		new Lwjgl3Application(MainGame.INSTANCE, config);
	}
}
