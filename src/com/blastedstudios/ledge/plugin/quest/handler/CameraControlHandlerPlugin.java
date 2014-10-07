package com.blastedstudios.ledge.plugin.quest.handler;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.plugin.quest.manifestation.cameracontrol.ICameraControlHandler;
import com.blastedstudios.ledge.world.WorldManager;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class CameraControlHandlerPlugin implements ICameraControlHandler{
	private WorldManager world;
	
	public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum playerTrack(boolean playerTrack) {
		world.setPlayerTrack(playerTrack);
		return CompletionEnum.COMPLETED;
	}
}
