package com.blastedstudios.ledge.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.panner.PannerManager;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.LedgeScreen;
import com.blastedstudios.ledge.ui.main.MainWindow.IMainWindowListener;
import com.blastedstudios.ledge.ui.main.NewCharacterWindow.INewCharacterWindowListener;
import com.blastedstudios.ledge.util.ActionEnum;

public class MainScreen extends LedgeScreen implements IMainWindowListener, INewCharacterWindowListener {
	public static final Color WINDOW_ALPHA_COLOR = new Color(1, 1, 1, .7f);
	public static final String SKIN_PATH = Properties.get("screen.skin","data/ui/uiskinGame.json");
	public static final FileHandle WORLD_FILE = Gdx.files.internal("data/world/" + Properties.get("world.path", "world.xml"));
	private final GDXWorld gdxWorld;
	private final GDXRenderer gdxRenderer;
	private final AssetManager sharedAssets;
	private final PannerManager panner;
	private NewCharacterWindow newCharacterWindow;
	private MainWindow mainWindow;

	public MainScreen(final GDXGame game, AssetManager sharedAssets, GDXWorld gdxWorld){
		super(game);
		this.sharedAssets = sharedAssets;
		this.gdxWorld = gdxWorld;
		gdxRenderer = new GDXRenderer(true, true);
		stage.addActor(mainWindow = new MainWindow(skin, game, this, gdxWorld, WORLD_FILE, gdxRenderer, sharedAssets));
		panner = new PannerManager(gdxWorld, gdxRenderer);
		register(ActionEnum.BACK, new AbstractInputHandler() {
			public void down(){
				Gdx.app.exit();
			}
		});
	}

	@Override public void render(float delta){
		super.render(delta);
		sharedAssets.update();
		update(delta);
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

	public void update(float delta) {
		panner.updatePanners(delta);
	}

	public boolean ready() {
		return panner.ready();
	}
}
