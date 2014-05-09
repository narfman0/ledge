package com.blastedstudios.ledge.plugin.quest.manifestation.turretadd;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.ui.leveleditor.VertexTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class TurretAddManifestationTable extends ManifestationTable{
	private final TurretAddManifestation manifestation;
	private final TextField weaponField, directionField, directionLowField, directionHighField;
	private final VertexTable locationTable;

	public TurretAddManifestationTable(final Skin skin, final TurretAddManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		weaponField = new TextField(manifestation.getWeapon(), skin);
		weaponField.setMessageText("<weapon name>");
		directionField = new TextField(manifestation.getDirection()+"", skin);
		directionField.setMessageText("<direction>");
		directionLowField = new TextField(manifestation.getDirection()+"", skin);
		directionLowField.setMessageText("<direction low>");
		directionHighField = new TextField(manifestation.getDirection()+"", skin);
		directionHighField.setMessageText("<direction high>");
		locationTable = new VertexTable(manifestation.getLocation(), skin, null);

		add(new Label("Location: ", skin));
		add(locationTable);
		row();
		add(new Label("Weapon: ", skin));
		add(weaponField);
		row();
		add(new Label("Direction: ", skin));
		add(directionField);
		row();
		add(new Label("Direction Low: ", skin));
		add(directionLowField);
		row();
		add(new Label("Direction High: ", skin));
		add(directionHighField);
		row();
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setLocation(locationTable.getVertex());
		manifestation.setWeapon(weaponField.getText());
		manifestation.setDirection(Float.parseFloat(directionField.getText()));
		manifestation.setDirectionLow(Float.parseFloat(directionLowField.getText()));
		manifestation.setDirectionHigh(Float.parseFloat(directionHighField.getText()));
		return manifestation;
	}
}
