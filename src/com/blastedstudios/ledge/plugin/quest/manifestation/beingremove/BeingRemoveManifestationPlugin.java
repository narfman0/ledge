package com.blastedstudios.ledge.plugin.quest.manifestation.beingremove;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class BeingRemoveManifestationPlugin implements IQuestComponentManifestation {
	@Override public String getBoxText() {
		return "Being Remove";
	}

	@Override public ICloneable getDefault() {
		return BeingRemoveManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new BeingRemoveManifestationTable(skin, (BeingRemoveManifestation) object);
	}
}
