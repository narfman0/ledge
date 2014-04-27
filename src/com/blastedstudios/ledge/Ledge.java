package com.blastedstudios.ledge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.main.MainScreen;

public class Ledge extends GDXGame {
	@Override public void create () {
		Properties.load(FileUtil.find(Gdx.files.internal("data"), "ledge.properties").read());
		pushScreen(new MainScreen(this));
	}
	
	public static void main (String[] argv) {
		new LwjglApplication(new Ledge(), "Ledge", 1680, 1000, true);
	}
}
