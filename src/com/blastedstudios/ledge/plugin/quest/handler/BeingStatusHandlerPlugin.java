package com.blastedstudios.ledge.plugin.quest.handler;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
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
		for(Being being : world.getAllBeings())
			if(being.getName().equals(beingName))
				if(kill)
					being.setHp(0f);
				else if (dmg > 0f)
					being.setHp(being.getHp() - dmg);
		Gdx.app.log("BeingStatusHandlerPlugin.statusBeing", "Status change for " + beingName);
		return CompletionEnum.COMPLETED;
	}
}
