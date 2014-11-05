package com.blastedstudios.ledge.plugin.quest.handler;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.plugin.quest.manifestation.beingremove.IBeingRemoveHandler;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class BeingRemoveHandlerPlugin implements IBeingRemoveHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum removeBeing(String being) {
		int total = 0;
		if(being.contains(","))
			for(String beingName : being.split(","))
				total += world.removeNPC(beingName);
		else
			total = world.removeNPC(being);
		Log.log("BeingRemoveHandlerPlugin.removeBeing", "Removed " + total + " beings matching " + being);
		return CompletionEnum.COMPLETED;
	}
}
