package com.blastedstudios.ledge.ui.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.ledge.ui.gameplay.inventory.GunButton;
import com.blastedstudios.ledge.ui.gameplay.inventory.GunButton.IButtonClicked;
import com.blastedstudios.ledge.ui.gameplay.inventory.GunInformationWindow;
import com.blastedstudios.ledge.ui.gameplay.inventory.GunInformationWindow.IWeaponInfoListener;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.weapon.Weapon;

public class VendorWindow extends AbstractWindow implements IButtonClicked{
	private final Stage stage;
	private final Skin skin;
	private final Being client;
	private final World world;
	private final NPC npc;
	private final ChangeListener closeListener;
	private GunInformationWindow gunInformationWindow;
	
	public VendorWindow(final Skin skin, NPC npc, Stage stage, Being client, 
			World world, final ChangeListener closeListener, AssetManager sharedAssets){
		super("", skin);
		this.stage = stage;
		this.skin = skin;
		this.client = client;
		this.world = world;
		this.closeListener = closeListener;
		this.npc = npc;
		Table table = new Table(skin);
		int counter = 0;
		for(final Weapon gun : npc.getVendorWeapons()){
			table.add(new GunButton(skin, sharedAssets, gun, this));
			if(counter % 2 == 1)
				table.row();
			counter++;
		}
		Button exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				closeListener.changed(new ChangeEvent(), event.getListenerActor().getParent().getParent());
			}
		});
		add(new Label("Vendor", skin));
		row();
		add(new ScrollPane(table));
		row();
		add(exitButton);
		pack();
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
	}
	
	@Override public boolean remove(){
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		return super.remove();
	}

	@Override public void gunButtonClicked(Weapon weapon) {
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		IWeaponInfoListener listener = new IWeaponInfoListener() {
			@Override public void sellWeapon(Weapon weapon) {/*cant sell*/}
			@Override public void deleteWeapon(Weapon weapon) {/*cant delete*/}
			@Override public void buyWeapon(Weapon weapon) {
				if(client.buy(weapon, world))
					npc.getVendorWeapons().remove(weapon);
				else
					Log.log("VendorWindow.buyWeapon", "Failed to buy weapon: " + weapon);
				closeListener.changed(new ChangeEvent(), VendorWindow.this);
			}
		};
		stage.addActor(gunInformationWindow = new GunInformationWindow(skin, weapon, listener, 
				false, false, client.getCash() >= weapon.getCost()));
		gunInformationWindow.setY(Gdx.graphics.getHeight() - gunInformationWindow.getHeight() - 32f);
	}
	
	@Override public boolean contains(float x, float y){
		return (gunInformationWindow != null && gunInformationWindow.contains(x, y)) || super.contains(x, y);
	}
}
