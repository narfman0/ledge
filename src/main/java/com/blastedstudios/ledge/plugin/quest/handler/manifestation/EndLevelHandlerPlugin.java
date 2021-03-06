package com.blastedstudios.ledge.plugin.quest.handler.manifestation;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.endlevel.IEndLevelHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.plugin.quest.handler.IGameplayScreenInitializer;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;

@PluginImplementation
public class EndLevelHandlerPlugin implements IEndLevelHandler, IGameplayScreenInitializer{
	private GameplayScreen screen;
	
	@Override public void setGameplayScreen(GameplayScreen screen){
		this.screen = screen;
	}

	@Override public CompletionEnum endLevel(boolean success, String nextLevel) {
		screen.levelComplete(success, nextLevel);
		return CompletionEnum.COMPLETED;
	}
}
