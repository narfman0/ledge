package com.blastedstudios.ledge.ui.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.world.being.Being;

public class CharacterWindow extends AbstractWindow {
	public CharacterWindow(final Skin skin, Being being, final ChangeListener listener){
		super("", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		final Button exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.changed(new ChangeEvent(), event.getTarget().getParent());
			}
		});
		add(new Label(being.getName(), skin));
		row();
		add(new Label("Attack: " + being.getAttack(), skin));
		row();
		add(new Label("Defense: " + being.getDefense(), skin));
		row();
		add(new Label("Cash: " + being.getCash(), skin));
		row();
		add(new Label("Level: " + being.getLevel(), skin));
		row();
		add(new Label("XP: " + being.getXp() + "/" + Being.xpToLevel(being.getLevel()+1), skin));
		row();
		add(new Label("HP: " + (int)being.getHp() + "/" + (int)being.getMaxHp(), skin));
		row();
		add(exitButton).fillX();
		pack();
		setX(0);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
	}
}
