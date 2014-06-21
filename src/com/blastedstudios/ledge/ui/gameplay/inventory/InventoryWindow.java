package com.blastedstudios.ledge.ui.gameplay.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.ledge.ui.gameplay.inventory.GunButton.IButtonClicked;
import com.blastedstudios.ledge.ui.main.MainScreen;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Weapon;

/**
 * The player has inventory as follows:
 * There shall be non weapon armor: chest, head, and accessory
 * There shall be 4 settable gun slots, otherwise guns go in main inventory
 * There shall be 4*4 inventory slots available
 */
public class InventoryWindow extends AbstractWindow implements IButtonClicked {
	private final Skin skin;
	private final Stage stage;
	private final InventoryTable inventoryTable;
	private final boolean canSell;
	private GunInformationWindow gunInformationWindow;

	public InventoryWindow(final Skin skin, final Being being, final ChangeListener listener,
			final GDXRenderer renderer, final Stage stage, boolean canSell){
		super("", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		this.skin = skin;
		this.stage = stage;
		this.canSell = canSell;
		inventoryTable = new InventoryTable(skin, being, listener, renderer, this);
		final Button acceptButton = new TextButton("Accept", skin);
		acceptButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				inventoryTable.accept();
				listener.changed(new ChangeEvent(), event.getListenerActor().getParent().getParent());
			}
		});
		final Button exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.changed(new ChangeEvent(), event.getListenerActor().getParent().getParent());
			}
		});
		add(inventoryTable);
		row();
		Table controls = new Table();
		controls.add(acceptButton);
		controls.add(exitButton);
		add(controls);
		pack();
		setX(Gdx.graphics.getWidth() - getWidth());
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
	}
	
	@Override public boolean remove(){
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		return super.remove();
	}

	@Override public void gunButtonClicked(Weapon weapon) {
		if(gunInformationWindow != null)
			gunInformationWindow.remove();
		stage.addActor(gunInformationWindow = new GunInformationWindow(skin, weapon, inventoryTable, true, canSell, false));
	}
	
	@Override public boolean contains(float x, float y){
		return (gunInformationWindow != null && gunInformationWindow.contains(x, y)) || super.contains(x, y);
	}
}
