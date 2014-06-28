package com.blastedstudios.ledge.plugin.quest.handler;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.inputenable.IInputEnableHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.world.WorldManager;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class InputEnableHandlerPlugin implements IInputEnableHandler{
	private WorldManager world;
	
	public void setWorldManager(WorldManager world){
		this.world = world;
	}
	
	@Override public CompletionEnum inputEnable(boolean inputEnable) {
		world.setInputEnable(inputEnable);
		return CompletionEnum.COMPLETED;
	}
}
