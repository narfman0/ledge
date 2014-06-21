package com.blastedstudios.ledge.ui.gameplay.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.ledge.ui.gameplay.GunTable;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.world.weapon.Weapon;

public class GunInformationWindow extends AbstractWindow {
	public GunInformationWindow(final Skin skin, final Weapon weapon, final IWeaponInfoListener listener, boolean canSell){
		super("", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		Button closeButton = new TextButton("Close", skin);
		closeButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				event.getListenerActor().getParent().getParent().remove();
			}
		});
		Button sellButton = new TextButton("Sell", skin);
		sellButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				event.getListenerActor().getParent().getParent().remove();
				listener.sellWeapon(weapon);
			}
		});
		Button deleteButton = new TextButton("Delete", skin);
		deleteButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				event.getListenerActor().getParent().getParent().remove();
				listener.deleteWeapon(weapon);
			}
		});
		add(new Label(weapon.getName(), skin)).colspan(2);
		row();
		add(new GunTable(skin, weapon)).colspan(2);
		row();
		final Table controls = new Table();
		controls.add(closeButton);
		if(canSell)
			controls.add(sellButton);
		controls.add(deleteButton);
		add(controls).colspan(2);
		pack();
		setX(Gdx.graphics.getWidth()/2f - getWidth()/2f);
		setY(32f);	
	}
	
	public interface IWeaponInfoListener{
		void deleteWeapon(Weapon weapon);
		void sellWeapon(Weapon weapon);
	}
}
