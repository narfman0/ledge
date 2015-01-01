package com.blastedstudios.ledge.ui.overworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.loading.GameplayLoadingWindowExecutor;
import com.blastedstudios.ledge.ui.loading.LoadingWindow;
import com.blastedstudios.ledge.util.ui.LedgeTextButton;
import com.blastedstudios.ledge.util.ui.LedgeWindow;
import com.blastedstudios.ledge.world.DialogManager;
import com.blastedstudios.ledge.world.being.Player;

class LevelInformationWindow extends LedgeWindow{
	public final GDXLevel level;
	
	public LevelInformationWindow(final Skin skin, final GDXLevel level, 
			final GDXGame game, final Player player, final GDXWorld world, 
			final FileHandle selectedFile, final GDXRenderer gdxRenderer,
			final AssetManager sharedAssets, final OverworldScreen screen) {
		super("", skin);
		this.level = level;
		final Button startButton = new LedgeTextButton("Start", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				screen.getStage().addActor(new LoadingWindow(skin, 
						new GameplayLoadingWindowExecutor(game, player, level, world, selectedFile, gdxRenderer, sharedAssets)));
			}
		});
		add(new Label(level.getName(), skin));
		row();
		add(new Label("Description:\n" + DialogManager.splitRenderable(level.getProperties().get("Description"), 40), skin));
		row();
		String prereqs = level.getProperties().get("Prerequisites");
		if(prereqs != null && !prereqs.isEmpty()){
			add(new Label("Prerequisites: " + level.getProperties().get("Prerequisites"), skin));
			row();
		}
		add(startButton).fillX();
		pack();
		setX(Gdx.graphics.getWidth() - getWidth());
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}
}
