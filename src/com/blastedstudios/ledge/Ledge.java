package com.blastedstudios.ledge;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.ledge.ui.loading.MainLoadingScreen;
import com.blastedstudios.ledge.util.SaveHelper;

public class Ledge extends GDXGame {
	@Override public void create () {
		pushScreen(new MainLoadingScreen(this));
	}
	
	public static void main (String[] argv) {
		SaveHelper.loadProperties();
		new LwjglApplication(new Ledge(), GDXWorldEditor.generateConfiguration("Ledge"));
	}
}
