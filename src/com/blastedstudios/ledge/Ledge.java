package com.blastedstudios.ledge;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
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
		boolean fullscreen = Properties.getBool("graphics.fullscreen", false);
		Dimension dimension = getDimension(fullscreen);
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Ledge";
	    cfg.width = dimension.width;
	    cfg.height = dimension.height;
	    cfg.fullscreen = fullscreen;
	    cfg.useGL20 = Properties.getBool("graphics.usegl20", true);
	    cfg.vSyncEnabled = Properties.getBool("graphics.vsync", true);
		new LwjglApplication(new Ledge(), cfg);
	}
	
	private static Dimension getDimension(boolean fullscreen){
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = Properties.getInt("graphics.width", gd.getDisplayMode().getWidth());
		int height = Properties.getInt("graphics.height", gd.getDisplayMode().getHeight());
		if(!fullscreen){
			width = width < 1800 ? 800 : 1680;
			height = height < 1080 ? 600 : 1000;
		}
		return new Dimension(width, height);
	}
}
