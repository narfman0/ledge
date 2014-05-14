package com.blastedstudios.ledge.ui.gameplay.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.ledge.ui.gameplay.inventory.GunButton.IButtonClicked;
import com.blastedstudios.ledge.ui.gameplay.inventory.GunInformationWindow.IDeleteWeaponListener;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Weapon;

/**
 * The player has inventory as follows:
 * There shall be non weapon armor: chest, head, and accessory
 * There shall be 4 settable gun slots, otherwise guns go in main inventory
 * There shall be 4*4 inventory slots available
 */
public class InventoryWindow extends AbstractWindow implements IButtonClicked, IDeleteWeaponListener {
	private static final int GUNS_PER_ROW = 4;
	private final Sprite noGun;
	private final Table gunsTable, inventoryTable;
	final List<Weapon> guns, inventory;
	private final GDXRenderer renderer;
	private GunInformationWindow gunInformationWindow;
	private final Skin skin;
	private final Stage stage;
	private Weapon lastClicked = null;

	public InventoryWindow(final Skin skin, final Being being, final ChangeListener listener,
			final GDXRenderer renderer, final Stage stage){
		super("", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		this.renderer = renderer;
		this.skin = skin;
		this.stage = stage;
		noGun = GunButton.createGunSprite(renderer, "nogun");
		gunsTable = new Table();
		inventoryTable = new Table();
		guns = new ArrayList<>(being.getGuns());
		inventory = new ArrayList<>(being.getInventory());
		final Button acceptButton = new TextButton("Accept", skin);
		acceptButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				List<Weapon> guns = new LinkedList<Weapon>();
				for(Actor actor : gunsTable.getChildren())
					if(actor instanceof GunButton)
						guns.add(((GunButton)actor).gun);
				List<Weapon> inventory = new LinkedList<Weapon>();
				for(Actor actor : inventoryTable.getChildren())
					if(actor instanceof GunButton)
						inventory.add(((GunButton)actor).gun);
				being.setGuns(guns);
				being.setInventory(inventory);
				listener.changed(new ChangeEvent(), event.getListenerActor().getParent().getParent());
			}
		});
		final Button exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.changed(new ChangeEvent(), event.getListenerActor().getParent().getParent());
			}
		});
		rebuildGunTables();
		add(new Label("Inventory", skin)).colspan(2);
		row();
		add(new Label("To swap, click a gun in the active column,\nthen shift click a gun in the inventory column", skin)).colspan(2);
		row();
		Table activeGuns = new Table();
		activeGuns.add(new Label("Active", skin));
		activeGuns.row();
		activeGuns.add(gunsTable);
		Table inventoryGuns = new Table();
		inventoryGuns.add(new Label("Inventory", skin));
		inventoryGuns.row();
		inventoryGuns.add(inventoryTable);
		add(activeGuns);
		add(inventoryGuns);
		row();
		Table controls = new Table();
		controls.add(acceptButton);
		controls.add(exitButton);
		add(controls).colspan(2);
		pack();
		setX(Gdx.graphics.getWidth() - getWidth());
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
	}
	
	private void rebuildGunTables(){
		addGunTables(skin);
		addInventoryTables(skin);
	}

	private void addGunTables(final Skin skin){
		gunsTable.clear();
		for(int i=0; i<GUNS_PER_ROW; i++){
			if(guns.size() > i)
				gunsTable.add(new GunButton(skin, renderer, guns.get(i), this));
			else
				gunsTable.add(new ImageButton(new SpriteDrawable(noGun)));
			gunsTable.row();
		}
	}

	private void addInventoryTables(final Skin skin){
		inventoryTable.clear();
		for(int y=0; y<GUNS_PER_ROW; y++){
			for(int x=0; x < GUNS_PER_ROW; x++){
				if(inventory.size() > x+y*GUNS_PER_ROW){
					final Weapon gun = inventory.get(x+y*GUNS_PER_ROW);
					inventoryTable.add(new GunButton(skin, renderer, gun, this));
				}else
					inventoryTable.add(new ImageButton(new SpriteDrawable(noGun)));
			}
			inventoryTable.row();
		}
	}
	
	private void swap(Weapon first, Weapon second){
		List<Weapon> firstList = guns.contains(first) ? guns : inventory,
			secondList = guns.contains(second) ? guns : inventory;
		int firstIndex = firstList.indexOf(first),
			secondIndex = secondList.indexOf(second);
		if(firstList == secondList)
			Collections.swap(firstList, firstIndex, secondIndex);
		else{
			firstList.remove(first);
			secondList.remove(second);
			firstList.add(firstIndex, second);
			secondList.add(secondIndex, first);
		}
		Gdx.app.log("InventoryWindow.swap", "Weapon swap, first: " + first + " second: " + second);
	}

	@Override public void gunButtonClicked(Weapon weapon) {
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		if(lastClicked != null && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
			swap(lastClicked, weapon);
			lastClicked = null;
			rebuildGunTables();
		}else
			stage.addActor(gunInformationWindow = new GunInformationWindow(skin, lastClicked = weapon, this));
	}
	
	@Override public boolean remove(){
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		return super.remove();
	}
	
	public boolean contains(float x, float y){
		return (gunInformationWindow != null && gunInformationWindow.contains(x, y)) || super.contains(x, y);
	}

	@Override public void deleteWeapon(Weapon weapon) {
		boolean removed = guns.remove(weapon);
		inventory.remove(weapon);
		if(removed && !inventory.isEmpty()){
			Weapon newWeapon = inventory.get(0); 
			guns.add(newWeapon); 
			inventory.remove(newWeapon);
		}
		rebuildGunTables();
	}
}
