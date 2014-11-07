package com.blastedstudios.ledge.plugin.quest.handler;

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
	public CompletionEnum statusBeing(String beingName, float dmg, boolean kill) {
		Being target = null;
		if(beingName.endsWith("player"))
			target = world.getPlayer();
		else
			for(Being being : world.getAllBeings())
				if(being.getName().equals(beingName))
					target = being;
		if(target != null)
			if(kill)
				target.setHp(0f);
			else if (dmg > 0f)
				target.setHp(target.getHp() - dmg);
		Log.log("BeingStatusHandlerPlugin.statusBeing", "Status change for " + beingName);
		return CompletionEnum.COMPLETED;
	}
}
