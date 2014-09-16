package com.blastedstudios.ledge.plugin.quest.handler;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.endlevel.IEndLevelHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class EndLevelHandlerPlugin implements IEndLevelHandler{
	private GameplayScreen screen;
	
	public void setGameplayScreen(GameplayScreen screen){
		this.screen = screen;
	}

	@Override public CompletionEnum endLevel(boolean success, String nextLevel) {
		screen.levelComplete(success, nextLevel);
		return CompletionEnum.COMPLETED;
	}
}
