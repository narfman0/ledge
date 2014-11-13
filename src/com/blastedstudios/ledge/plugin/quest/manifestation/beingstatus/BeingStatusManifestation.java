package com.blastedstudios.ledge.plugin.quest.manifestation.beingstatus;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingStatusManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final BeingStatusManifestation DEFAULT = new BeingStatusManifestation();
	private String being = "", textureAtlas = "";
	private float dmg = 0f;
	private boolean kill;
	
	public BeingStatusManifestation(){}
	public BeingStatusManifestation(String being, float dmg, boolean kill, String textureAtlas){
		this.being = being;
		this.dmg = dmg;
		this.kill = kill;
		this.textureAtlas = textureAtlas;
	}
	
	@Override public CompletionEnum execute() {
		for(IBeingStatusHandler handler : PluginUtil.getPlugins(IBeingStatusHandler.class))
			handler.statusBeing(being, dmg, kill, textureAtlas);
		return CompletionEnum.COMPLETED;
	}

	@Override public AbstractQuestManifestation clone() {
		return new BeingStatusManifestation(being, dmg, kill, textureAtlas);
	}

	@Override public String toString() {
		return "[BeingStatusManifestation being:" + being + "]";
	}
	
	public String getBeing() {
		return being;
	}
	
	public void setBeing(String being) {
		this.being = being;
	}
	
	public float getDmg() {
		return dmg;
	}
	
	public void setDmg(float dmg) {
		this.dmg = dmg;
	}
	
	public boolean isKill() {
		return kill;
	}
	
	public void setKill(boolean kill) {
		this.kill = kill;
	}
	
	public String getSprite() {
		if(textureAtlas == null)
			textureAtlas = "";
		return textureAtlas;
	}
	
	public void setSprite(String textureAtlas) {
		this.textureAtlas = textureAtlas;
	}
}
