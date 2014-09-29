package com.blastedstudios.ledge.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.AssetManagerWrapper;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner.ITransitionListener;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.main.MainWindow.IMainWindowListener;
import com.blastedstudios.ledge.ui.main.NewCharacterWindow.INewCharacterWindowListener;

public class MainScreen extends AbstractScreen implements IMainWindowListener,
		INewCharacterWindowListener, ITransitionListener {
	public static final Color WINDOW_ALPHA_COLOR = new Color(1, 1, 1, .7f);
	public static final String SKIN_PATH = Properties.get("screen.skin","data/ui/uiskinGame.json");
	public static final FileHandle WORLD_FILE = Gdx.files.internal("data/world/" + Properties.get("world.path", "world.xml"));
	private final GDXWorld gdxWorld;
	private final GDXRenderer gdxRenderer;
	private final AssetManagerWrapper sharedAssets;
	private NewCharacterWindow newCharacterWindow;
	private MainWindow mainWindow;
	private ScreenLevelPanner activePanner, loadingPanner;

	public MainScreen(final GDXGame game, AssetManagerWrapper sharedAssets, GDXWorld gdxWorld){
		super(game, SKIN_PATH);
		this.sharedAssets = sharedAssets;
		this.gdxWorld = gdxWorld;
		gdxRenderer = new GDXRenderer(true, true);
		stage.addActor(mainWindow = new MainWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer, sharedAssets));
		loadingPanner = new ScreenLevelPanner(gdxWorld, gdxRenderer, this);
	}

	@Override public void render(float delta){
		super.render(delta);
		sharedAssets.update();
		updatePanners();
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
	
	private void updatePanners(){
		if(activePanner != null)
			activePanner.render();
		if(loadingPanner != null)
			if(loadingPanner.update()){
				if(activePanner != null)
					activePanner.dispose();
				activePanner = loadingPanner;
				loadingPanner = null;
			}
	}

	@Override public void transition() {
		loadingPanner = new ScreenLevelPanner(gdxWorld, gdxRenderer, this);
	}
}
