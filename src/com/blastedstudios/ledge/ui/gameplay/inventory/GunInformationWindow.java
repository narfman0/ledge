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
	public GunInformationWindow(final Skin skin, final Weapon weapon, final IWeaponInfoListener listener, 
			boolean canDelete, boolean canSell, boolean canBuy){
		super("", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		Button closeButton = new TextButton("Close", skin);
		closeButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				event.getListenerActor().getParent().getParent().remove();
			}
		});
		Button buyButton = new TextButton("Buy", skin);
		buyButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				event.getListenerActor().getParent().getParent().remove();
				listener.buyWeapon(weapon);
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
		add(new Label(weapon.getName(), skin));
		row();
		add(new GunTable(skin, weapon));
		row();
		final Table controls = new Table();
		if(canBuy)
			controls.add(buyButton);
		if(canSell)
			controls.add(sellButton);
		if(canDelete)
			controls.add(deleteButton);
		controls.add(closeButton);
		add(controls);
		pack();
		setX(Gdx.graphics.getWidth()/2f - getWidth()/2f);
		setY(32f);	
	}
	
	public interface IWeaponInfoListener{
		void deleteWeapon(Weapon weapon);
		void sellWeapon(Weapon weapon);
		void buyWeapon(Weapon weapon);
	}
}
