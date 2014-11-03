package com.blastedstudios.ledge.plugin.quest.manifestation.beingstatus;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingStatusManifestationTable extends ManifestationTable{
	private final BeingStatusManifestation manifestation;
	private final TextField beingField, dmgField;
	private final CheckBox killCheckbox;
	
	public BeingStatusManifestationTable(Skin skin, BeingStatusManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name(s) or regex>");
		dmgField = new TextField(manifestation.getDmg()+"", skin);
		dmgField.setMessageText("<apply damage>");
		killCheckbox = new CheckBox("Kill", skin);
		killCheckbox.setChecked(manifestation.isKill());
		add("Being(s): ");
		add(beingField);
		row();
		add("Damage: ");
		add(dmgField);
		row();
		add(killCheckbox);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeing(beingField.getText());
		manifestation.setDmg(Float.parseFloat(dmgField.getText()));
		manifestation.setKill(killCheckbox.isChecked());
		return manifestation;
	}
}
