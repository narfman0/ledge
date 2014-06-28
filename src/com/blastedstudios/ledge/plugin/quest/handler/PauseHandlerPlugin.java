package com.blastedstudios.ledge.plugin.quest.handler;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.pause.IPauseHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class PauseHandlerPlugin implements IPauseHandler {
	private WorldManager world;
	
	public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum pause(boolean pause) {
		world.pause(pause);
		return CompletionEnum.COMPLETED;
	}
}