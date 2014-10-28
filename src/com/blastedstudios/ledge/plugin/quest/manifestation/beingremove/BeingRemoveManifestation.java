package com.blastedstudios.ledge.plugin.quest.manifestation.beingremove;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingRemoveManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final BeingRemoveManifestation DEFAULT = new BeingRemoveManifestation();
	private String being = "";
	
	public BeingRemoveManifestation(){}
	public BeingRemoveManifestation(String being){
		this.being = being;
	}
	
	@Override public CompletionEnum execute() {
		for(IBeingRemoveHandler handler : PluginUtil.getPlugins(IBeingRemoveHandler.class))
			handler.removeBeing(being);
		return CompletionEnum.COMPLETED;
	}

	@Override public AbstractQuestManifestation clone() {
		return new BeingRemoveManifestation(being);
	}

	@Override public String toString() {
		return "[BeingRemoveManifestation being:" + being + "]";
	}
	
	public String getBeing() {
		return being;
	}
	
	public void setBeing(String being) {
		this.being = being;
	}
}
