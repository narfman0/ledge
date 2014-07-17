package com.blastedstudios.ledge.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.overworld.OverworldScreen;
import com.blastedstudios.ledge.util.SaveHelper;
import com.blastedstudios.ledge.world.being.Player;

class MainWindow extends Window{
	public MainWindow(final Skin skin, final GDXGame game, final IMainWindowListener listener, 
			final GDXWorld gdxWorld, final FileHandle worldFile, final GDXRenderer gdxRenderer) {
		super("", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		final Button newCharacterButton = new TextButton("Create New", skin);
		final Button exitButton = new TextButton("Exit", skin);
		newCharacterButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.newCharacterButtonClicked();
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		add(new Label("Ledge", skin));
		row();
		add(newCharacterButton).fillX();
		row();
		for(final Player being : SaveHelper.load()){
			final Button savedCharacterButton = new TextButton(being.getName(), skin);
			savedCharacterButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					game.pushScreen(new OverworldScreen(game, being, gdxWorld, worldFile, gdxRenderer));
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