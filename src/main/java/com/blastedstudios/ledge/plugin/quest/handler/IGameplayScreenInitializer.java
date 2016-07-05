package com.blastedstudios.ledge.plugin.quest.handler;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;

@PluginImplementation
public interface IGameplayScreenInitializer extends Plugin{
	void setGameplayScreen(GameplayScreen screen);
}
