package com.blastedstudios.ledge;

import java.io.File;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.loading.MainLoadingScreen;

public class Ledge extends GDXGame {
	@Override public void create () {
		pushScreen(new MainLoadingScreen(this));
	}
	
	private static void loadProperties(FileHandle handle){
		if(handle.exists()){
			Properties.load(handle.read());
		}else
			System.out.println("Ledge.loadProperties: File not found - " + handle.path());
	}
	
	public static void main (String[] argv) {
		loadProperties(new FileHandle("data/ledge.properties"));
		loadProperties(new FileHandle(new File(LwjglFiles.externalPath + "/.ledge/ledge.properties")));
		new LwjglApplication(new Ledge(), GDXWorldEditor.generateConfiguration("Ledge"));
	}
}
