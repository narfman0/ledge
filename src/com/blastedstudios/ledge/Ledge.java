package com.blastedstudios.ledge;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.loading.MainLoadingScreen;

public class Ledge extends GDXGame {
	@Override public void create () {
		Properties.load(FileUtil.find("data/ledge.properties").read());
		pushScreen(new MainLoadingScreen(this));
	}
	
	public static void main (String[] argv) {
		new LwjglApplication(new Ledge(), GDXWorldEditor.generateConfiguration("Ledge"));
	}
}
