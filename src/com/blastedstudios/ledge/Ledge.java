package com.blastedstudios.ledge;

import net.xeoh.plugins.base.util.uri.ClassURI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.main.MainScreen;

public class Ledge extends GDXGame {
	@Override public void create () {
		Properties.load(FileUtil.find(Gdx.files.internal("."), "ledge.properties").read());
		PluginUtil.initialize(ClassURI.CLASSPATH);
		pushScreen(new MainScreen(this));
	}
	
	public static void main (String[] argv) {
		new LwjglApplication(new Ledge(), GDXWorldEditor.generateConfiguration("Ledge"));
	}
}
