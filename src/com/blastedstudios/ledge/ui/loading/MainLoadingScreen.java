package com.blastedstudios.ledge.ui.loading;

import net.xeoh.plugins.base.util.uri.ClassURI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.AssetManagerWrapper;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.main.MainScreen;

public class MainLoadingScreen extends AbstractScreen{
	private final AssetManagerWrapper sharedAssets = new AssetManagerWrapper();
	private GDXWorld gdxWorld;
	private int iterationsToLoad = 1;
	
	public MainLoadingScreen(final GDXGame game){
		super(game, MainScreen.SKIN_PATH);
		Table table = new Table(skin);
		table.add("Loading");
		table.pack();
		table.setX(Gdx.graphics.getWidth()/2 - table.getWidth()/2);
		table.setY(Gdx.graphics.getHeight()/2 - table.getHeight()/2);
		stage.addActor(table);

		sharedAssets.load("data/textures/blood.png", Texture.class);
		sharedAssets.load("data/textures/money.png", Texture.class);
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

	@Override public void render(float delta){
		super.render(delta);
		stage.draw();
		sharedAssets.update();
		if(sharedAssets.getProgress() == 1f){
			game.popScreen();
			GDXGameFade.fadeInPushScreen(game, new MainScreen(game, sharedAssets, gdxWorld));
		}
		if(iterationsToLoad-- == 0){
			PluginUtil.initialize(ClassURI.CLASSPATH);//this takes 5+ seconds
			gdxWorld = GDXWorld.load(MainScreen.WORLD_FILE);
			sharedAssets.load("data/textures/" + gdxWorld.getWorldProperties().get("background"), Texture.class);
			loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/sounds"), Sound.class);
			loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/ammo"), Texture.class);
			loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/weapons"), Texture.class);
		}
	}
}
