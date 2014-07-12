package com.blastedstudios.ledge.ui.main;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.main.MainWindow.IMainWindowListener;
import com.blastedstudios.ledge.ui.main.NewCharacterWindow.INewCharacterWindowListener;

public class MainScreen extends AbstractScreen implements IMainWindowListener, INewCharacterWindowListener{
	public static final Color WINDOW_ALPHA_COLOR = new Color(1, 1, 1, .7f);
	private static final File WORLD_FILE = FileUtil.find(Gdx.files.internal("."), Properties.get("world.path", "world.xml")).file();
	private final GDXWorld gdxWorld = GDXWorld.load(WORLD_FILE);
	private final GDXRenderer gdxRenderer;
	private NewCharacterWindow newCharacterWindow;
	private MainWindow mainWindow;
	private ScreenLevelPanner panner;

	public MainScreen(final GDXGame game){
		super(game, Properties.get("screen.skin","uiskinGame.json"));
		gdxRenderer = new GDXRenderer(true, true);
		stage.addActor(mainWindow = new MainWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer));
		panner = new ScreenLevelPanner(gdxWorld, gdxRenderer);
	}

	@Override public void render(float delta){
		super.render(delta);
		panner.render();
		stage.draw();
		
	}

	@Override public boolean keyDown(int key) {
		switch(key){
		case Keys.ESCAPE:
			Gdx.app.exit();
			break;
		}
		return super.keyDown(key);
	}

	@Override public void newCharacterButtonClicked() {
		mainWindow.remove();
		stage.addActor(newCharacterWindow = new NewCharacterWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer));
	}

	@Override public void backButtonClicked() {
		newCharacterWindow.remove();
		stage.addActor(mainWindow = new MainWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer));
	}
}
