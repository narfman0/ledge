package com.blastedstudios.ledge.ui.loading;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.AssetManagerWrapper;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.util.GDXGameFade.IPopListener;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.world.being.Player;

public class GameplayLoadingScreen extends AbstractScreen{
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	private final AssetManagerWrapper assetManager;
	private final Player player;
	private final GDXLevel level;
	private final GDXWorld world;
	private final FileHandle selectedFile;
	private final GDXRenderer gdxRenderer;
	private final AssetManagerWrapper sharedAssets;
	private final Sprite backgroundSprite;
	private final Label loadingLabel;
	private List<String> createdAssetList = null;
	private boolean finished = false;
	
	public GameplayLoadingScreen(GDXGame game, Player player, GDXLevel level, GDXWorld world,
			FileHandle selectedFile, final GDXRenderer gdxRenderer, AssetManagerWrapper sharedAssets){
		super(game, MainScreen.SKIN_PATH);
		this.player = player;
		this.level = level;
		this.world = world;
		this.selectedFile = selectedFile;
		this.gdxRenderer = gdxRenderer;
		this.sharedAssets = sharedAssets;
		assetManager = new AssetManagerWrapper();
		loadingLabel = new Label("", skin);
		Table table = new Table(skin);
		table.add("Loading ");
		table.add(loadingLabel);
		table.pack();
		table.setX(Gdx.graphics.getWidth()/2 - table.getWidth()/2);
		table.setY(Gdx.graphics.getHeight()/2 - table.getHeight()/2);
		stage.addActor(table);
		createdAssetList = level.createAssetList();
		String backgroundPath = "data/textures/" + world.getWorldProperties().get("background");
		backgroundSprite = new Sprite(sharedAssets.get(backgroundPath, Texture.class));
		backgroundSprite.setPosition(backgroundSprite.getWidth()/-2f, backgroundSprite.getHeight()/-2f);
		camera.update();
	}

	@Override public void render(float delta){
		super.render(delta);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		backgroundSprite.draw(spriteBatch);
		spriteBatch.end();
		stage.draw();
		if(finished)
			return;
		//begin loading
		if(!createdAssetList.isEmpty()){
			long start = System.currentTimeMillis();
			while(System.currentTimeMillis() - start < 33){
				assetManager.loadTexture(createdAssetList.remove(0));
			}
		}
		//middle of loading
		if(createdAssetList.isEmpty()){
			assetManager.update();
			loadingLabel.setText(assetManager.getQueuedAssets()+"");
		}
		//done loading
		if(createdAssetList.isEmpty() && assetManager.getQueuedAssets() == 0){
			finished = true;
			GDXGameFade.fadeOutPopScreen(game, new IPopListener() {
				@Override public void screenPopped() {
					GameplayScreen screen = new GameplayScreen(game, player, level, world, selectedFile, 
							gdxRenderer, sharedAssets, assetManager);
					GDXGameFade.fadeInPushScreen(game, screen);
				}
			});
		}
	}
}
