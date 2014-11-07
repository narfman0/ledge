package com.blastedstudios.ledge.ui.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.main.MainScreen;

public class BackWindow extends AbstractWindow {
	public BackWindow(final Skin skin, final GameplayScreen screen) {
		super("", skin);
		Button button = new TextButton(Properties.get("ui.back.button.text", "Exit to Map"), skin);
		button.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				screen.handleBack();
			}
		});
		add(button);
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
	}
}
