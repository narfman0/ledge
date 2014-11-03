package com.blastedstudios.ledge.plugin.quest.manifestation.beingstatus;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class BeingStatusManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static final BeingStatusManifestation DEFAULT = new BeingStatusManifestation();
	private String being = "";
	private float dmg = 0f;
	private boolean kill;
	
	public BeingStatusManifestation(){}
	public BeingStatusManifestation(String being, float dmg, boolean kill){
		this.being = being;
		this.dmg = dmg;
		this.kill = kill;
	}
	
	@Override public CompletionEnum execute() {
		for(IBeingStatusHandler handler : PluginUtil.getPlugins(IBeingStatusHandler.class))
			handler.statusBeing(being, dmg, kill);
		return CompletionEnum.COMPLETED;
	}

	@Override public AbstractQuestManifestation clone() {
		return new BeingStatusManifestation(being, dmg, kill);
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
}
