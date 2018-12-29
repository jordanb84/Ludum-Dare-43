package com.ld.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ld.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 804;
		config.height = 628;
		config.title = "Loading!";
		config.resizable = false;
		config.vSyncEnabled = true;
		new LwjglApplication(new Game(), config);
	}
}
