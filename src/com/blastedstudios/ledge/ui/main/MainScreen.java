package com.blastedstudios.ledge.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner.ITransitionListener;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.LedgeScreen;
import com.blastedstudios.ledge.ui.main.MainWindow.IMainWindowListener;
import com.blastedstudios.ledge.ui.main.NewCharacterWindow.INewCharacterWindowListener;
import com.blastedstudios.ledge.util.ActionEnum;

public class MainScreen extends LedgeScreen implements IMainWindowListener,
		INewCharacterWindowListener, ITransitionListener {
	public static final Color WINDOW_ALPHA_COLOR = new Color(1, 1, 1, .7f);
	public static final String SKIN_PATH = Properties.get("screen.skin","data/ui/uiskinGame.json");
	public static final FileHandle WORLD_FILE = Gdx.files.internal("data/world/" + Properties.get("world.path", "world.xml"));
	private final GDXWorld gdxWorld;
	private final GDXRenderer gdxRenderer;
	private final AssetManager sharedAssets;
	private NewCharacterWindow newCharacterWindow;
	private MainWindow mainWindow;
	private ScreenLevelPanner activePanner, loadingPanner;

	public MainScreen(final GDXGame game, AssetManager sharedAssets, GDXWorld gdxWorld){
		super(game);
		this.sharedAssets = sharedAssets;
		this.gdxWorld = gdxWorld;
		gdxRenderer = new GDXRenderer(true, true);
		stage.addActor(mainWindow = new MainWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer, sharedAssets));
		loadingPanner = new ScreenLevelPanner(gdxWorld, gdxRenderer, this);
		register(ActionEnum.BACK, new AbstractInputHandler() {
			public void down(){
				Gdx.app.exit();
			}
		});
	}

	@Override public void render(float delta){
		super.render(delta);
		sharedAssets.update();
		updatePanners(delta);
		stage.draw();
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
	
	public void updatePanners(float dt){
		if(activePanner != null)
			activePanner.render(dt);
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

	public boolean ready() {
		return activePanner != null;
	}
}
