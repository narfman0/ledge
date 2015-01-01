package com.blastedstudios.ledge.ui.main;

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
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.overworld.OverworldScreen;
import com.blastedstudios.ledge.util.SaveHelper;
import com.blastedstudios.ledge.util.ui.LedgeTextButton;
import com.blastedstudios.ledge.util.ui.LedgeWindow;
import com.blastedstudios.ledge.world.being.Player;

class MainWindow extends LedgeWindow{
	public MainWindow(final Skin skin, final GDXGame game, final IMainWindowListener listener, 
			final GDXWorld gdxWorld, final FileHandle worldFile, final GDXRenderer gdxRenderer,
			final AssetManager sharedAssets) {
		super("", skin);
		final Button newButton = new LedgeTextButton("Create New", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.newCharacterButtonClicked();
			}
		});
		final Button exitButton = new LedgeTextButton("Exit", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		add(new Label("Ledge", skin));
		row();
		add(newButton).fillX();
		row();
		for(final Player being : SaveHelper.load()){
			final Button savedCharacterButton = new LedgeTextButton(being.getName(), skin, new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					OverworldScreen screen = new OverworldScreen(game, being, 
							gdxWorld, worldFile, gdxRenderer, sharedAssets); 
					GDXGameFade.fadeInPushScreen(game, screen);
				}
			});
			add(savedCharacterButton).fillX();
			row();
		}
		add(exitButton).fillX();
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}
	
	interface IMainWindowListener{
		void newCharacterButtonClicked();
	}
}