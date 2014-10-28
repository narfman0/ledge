package com.blastedstudios.ledge.plugin.quest.manifestation.beingremove;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingRemoveManifestationTable extends ManifestationTable{
	private final BeingRemoveManifestation manifestation;
	private final TextField beingField;
	
	public BeingRemoveManifestationTable(Skin skin, BeingRemoveManifestation manifestation){
		super(skin);
		this.manifestation = manifestation;
		beingField = new TextField(manifestation.getBeing(), skin);
		beingField.setMessageText("<being name(s) or regex>");
		add("Being(s): ");
		add(beingField);
	}

	@Override public AbstractQuestManifestation apply() {
		manifestation.setBeing(beingField.getText());
		return manifestation;
	}
}
