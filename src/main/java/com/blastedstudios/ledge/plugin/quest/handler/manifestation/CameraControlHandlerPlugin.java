package com.blastedstudios.ledge.plugin.quest.handler.manifestation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.plugin.quest.handler.IWorldManagerInitializer;
import com.blastedstudios.ledge.plugin.quest.manifestation.cameracontrol.ICameraControlHandler;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class CameraControlHandlerPlugin implements ICameraControlHandler, IWorldManagerInitializer{
	private WorldManager world;
	
	@Override public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum playerTrack(boolean playerTrack) {
		world.setPlayerTrack(playerTrack);
		return CompletionEnum.COMPLETED;
	}
}
