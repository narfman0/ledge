package com.blastedstudios.ledge.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.AssetManagerWrapper;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.main.MainWindow.IMainWindowListener;
import com.blastedstudios.ledge.ui.main.NewCharacterWindow.INewCharacterWindowListener;

public class MainScreen extends AbstractScreen implements IMainWindowListener, INewCharacterWindowListener{
	public static final Color WINDOW_ALPHA_COLOR = new Color(1, 1, 1, .7f);
	public static final String SKIN_PATH = Properties.get("screen.skin","data/ui/uiskinGame.json");
	private static final FileHandle WORLD_FILE = Gdx.files.internal("data/world/" + Properties.get("world.path", "world.xml"));
	private final GDXWorld gdxWorld = GDXWorld.load(WORLD_FILE);
	private final GDXRenderer gdxRenderer;
	private final AssetManagerWrapper sharedAssets = new AssetManagerWrapper();
	private NewCharacterWindow newCharacterWindow;
	private MainWindow mainWindow;
	private ScreenLevelPanner panner;

	public MainScreen(final GDXGame game){
		super(game, SKIN_PATH);
		loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/sounds"), Sound.class);
		loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/ammo"), Texture.class);
		loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/weapons"), Texture.class);
		sharedAssets.load("data/textures/" + gdxWorld.getWorldProperties().get("background"), Texture.class);
		sharedAssets.load("data/textures/blood.png", Texture.class);
		sharedAssets.load("data/textures/money.png", Texture.class);
		gdxRenderer = new GDXRenderer(true, true);
		stage.addActor(mainWindow = new MainWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer, sharedAssets));
		panner = new ScreenLevelPanner(gdxWorld, gdxRenderer);
	}

	@Override public void render(float delta){
		super.render(delta);
		sharedAssets.update();
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
		stage.addActor(newCharacterWindow = new NewCharacterWindow(skin, game, this, gdxWorld, WORLD_FILE,
				gdxRenderer, sharedAssets));
	}

	@Override public void backButtonClicked() {
		newCharacterWindow.remove();
		stage.addActor(mainWindow = new MainWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer, sharedAssets));
	}
	
	public static <T> void loadAssetsRecursive(AssetManagerWrapper assets, FileHandle path, Class<T> type){
		for(FileHandle file : path.list()){
			if(file.isDirectory())
				loadAssetsRecursive(assets, file, type);
			else{
				try{
					assets.loadAsset(file.path(), type);
					Gdx.app.debug("MainScreen.loadAssetsRecursive", "Success loading asset path: " +
							path.path() + " as " + type.getCanonicalName());
				}catch(Exception e){
					Gdx.app.debug("MainScreen.loadAssetsRecursive", "Failed to load asset path: " +
							path.path() + " as " + type.getCanonicalName());
				}
			}
		}
	}
}
