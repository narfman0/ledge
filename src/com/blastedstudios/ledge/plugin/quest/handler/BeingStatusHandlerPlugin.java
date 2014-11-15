package com.blastedstudios.ledge.plugin.quest.handler;

import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.plugin.quest.manifestation.beingstatus.IBeingStatusHandler;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;

@PluginImplementation
public class BeingStatusHandlerPlugin implements IBeingStatusHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override
	public CompletionEnum statusBeing(String beingName, float dmg, boolean kill, String textureAtlas, boolean remove) {
		LinkedList<Being> targets = new LinkedList<>();
		if("player".matches(beingName))
			targets.add(world.getPlayer());
		for(Being being : world.getAllBeings())
			if(being.getName().matches(beingName))
				targets.add(being);
		for(Being target : targets){
			if (dmg > 0f)
				target.setHp(target.getHp() - dmg);
			if(kill)
				target.setHp(0f);
			if(textureAtlas != null && !textureAtlas.isEmpty())
				target.setResource(textureAtlas);
			if(remove)
				world.dispose(target);
		}
		Log.log("BeingStatusHandlerPlugin.statusBeing", "Status change for " + beingName);
		return CompletionEnum.COMPLETED;
	}
}
