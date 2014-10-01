package com.blastedstudios.ledge.ui.loading;

import net.xeoh.plugins.base.util.uri.ClassURI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.AssetManagerWrapper;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.util.AssetUtil;

public class MainLoadingScreen extends AbstractScreen{
	private final AssetManagerWrapper sharedAssets = new AssetManagerWrapper();
	private GDXWorld gdxWorld;
	private int iterationsToLoad = 1;
	private MainScreen mainScreen = null;
	
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

	@Override public void render(float delta){
		super.render(delta);
		stage.draw();
		sharedAssets.update();
		if(mainScreen == null && sharedAssets.getProgress() == 1f)
			mainScreen = new MainScreen(game, sharedAssets, gdxWorld);
		if(mainScreen != null){
			mainScreen.updatePanners();
			if(mainScreen.ready()){
				game.popScreen();
				GDXGameFade.fadeInPushScreen(game, mainScreen);
			}
		}
		if(iterationsToLoad-- == 0){
			PluginUtil.initialize(ClassURI.CLASSPATH);//this takes 5+ seconds
			gdxWorld = GDXWorld.load(MainScreen.WORLD_FILE);
			sharedAssets.load("data/textures/" + gdxWorld.getWorldProperties().get("background"), Texture.class);
			AssetUtil.loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/sounds"), Sound.class);
			AssetUtil.loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/ammo"), Texture.class);
			AssetUtil.loadAssetsRecursive(sharedAssets, Gdx.files.internal("data/textures/weapons"), Texture.class);
		}
	}
}
