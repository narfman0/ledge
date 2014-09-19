package com.blastedstudios.ledge.ui.loading;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.AssetManagerWrapper;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.world.being.Player;

public class LoadingScreen extends AbstractScreen{
	private final Label loadingLabel;
	private final AssetManagerWrapper assetManager;
	private final Player player;
	private final GDXLevel level;
	private final GDXWorld world;
	private final FileHandle selectedFile;
	private final GDXRenderer gdxRenderer;
	private final AssetManagerWrapper sharedAssets;
	
	public LoadingScreen(GDXGame game, Player player, GDXLevel level, GDXWorld world,
			FileHandle selectedFile, final GDXRenderer gdxRenderer, AssetManagerWrapper sharedAssets){
		super(game, MainScreen.SKIN_PATH);
		this.player = player;
		this.level = level;
		this.world = world;
		this.selectedFile = selectedFile;
		this.gdxRenderer = gdxRenderer;
		this.sharedAssets = sharedAssets;
		assetManager = new AssetManagerWrapper();
		for(String asset : level.createAssetList())
			assetManager.loadTexture(asset);
		loadingLabel = new Label("0", skin);
		Table table = new Table(skin);
		table.add("Loading... ");
		table.add(loadingLabel);
		table.add("% complete");
		stage.addActor(table);
	}

	@Override public void render(float delta){
		assetManager.update();
		loadingLabel.setText(assetManager.getProgress()+"");
		if(assetManager.getQueuedAssets() == 0){
			game.popScreen();
			game.pushScreen(new GameplayScreen(game, player, level, world, selectedFile, 
					gdxRenderer, sharedAssets, assetManager));
		}
	}
}
