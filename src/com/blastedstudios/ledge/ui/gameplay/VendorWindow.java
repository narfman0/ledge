package com.blastedstudios.ledge.ui.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.weapon.Weapon;

public class VendorWindow extends AbstractWindow {
	public VendorWindow(final Skin skin, NPC npc){
		super("Vendor", skin);
		Table table = new Table(skin);
		for(final Weapon gun : npc.getVendorWeapons()){
			table.add(new GunTable(skin, gun));
			Button buyButton = new TextButton("Buy", skin);
			table.add(buyButton);
			buyButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					Gdx.app.log("WeaponLockerWindow.<init>", "TODO implement gun buying");
				}
			});
			table.row();
		}
		Button exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				event.getListenerActor().getParent().remove();
			}
		});
		add(new ScrollPane(table));
		row();
		add(exitButton);
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
	}
}
