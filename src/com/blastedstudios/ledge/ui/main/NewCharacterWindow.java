package com.blastedstudios.ledge.ui.main;

import java.util.ArrayList;
import java.util.EnumSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.overworld.OverworldScreen;
import com.blastedstudios.ledge.util.ui.LedgeTextButton;
import com.blastedstudios.ledge.util.ui.LedgeWindow;
import com.blastedstudios.ledge.util.ui.UIHelper;
import com.blastedstudios.ledge.world.Stats;
import com.blastedstudios.ledge.world.being.FactionEnum;
import com.blastedstudios.ledge.world.being.NPCData;
import com.blastedstudios.ledge.world.being.Player;
import com.blastedstudios.ledge.world.weapon.Weapon;
import com.blastedstudios.ledge.world.weapon.WeaponFactory;

class NewCharacterWindow extends LedgeWindow{
	public NewCharacterWindow(final Skin skin, final GDXGame game, 
			final INewCharacterWindowListener listener, final GDXWorld gdxWorld, 
			final FileHandle worldFile, final GDXRenderer gdxRenderer,
			final AssetManager sharedAssets) {
		super("", skin);
		final TextField nameField = new TextField("", skin);
		try{
			nameField.setColor(UIHelper.getColor(skin, "new-name-field", "textfield", "secondary"));
		}catch(Exception e){}
		nameField.setMessageText("<name>");
		nameField.setMaxLength(12);
		final Button createButton = new LedgeTextButton("Create", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				NPCData npcData = NPCData.parse("player");
				Player player = new Player(nameField.getText(), 
						WeaponFactory.getGuns(npcData.get("Weapons")), new ArrayList<Weapon>(), 
						Stats.parseNPCData(npcData),
						0,0,1,0, FactionEnum.FRIEND, EnumSet.of(FactionEnum.FRIEND), 
						"player");
				OverworldScreen screen = new OverworldScreen(game, player, 
						gdxWorld, worldFile, gdxRenderer, sharedAssets);
				GDXGameFade.fadeInPushScreen(game, screen);
			}
		});
		final Button backButton = new LedgeTextButton("Back", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.backButtonClicked();
			}
		});
		add(new Label("New Character", skin));
		row();
		add(nameField).fillX();
		row();
		add(createButton).fillX();
		row();
		add(backButton).fillX();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
		pack();
	}
	
	interface INewCharacterWindowListener{
		void backButtonClicked();
	}
}