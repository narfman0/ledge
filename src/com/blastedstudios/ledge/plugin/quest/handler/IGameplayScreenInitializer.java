package com.blastedstudios.ledge.plugin.quest.handler;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface IGameplayScreenInitializer extends Plugin{
	void setGameplayScreen(GameplayScreen screen);
}
