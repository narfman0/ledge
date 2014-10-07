package com.blastedstudios.ledge.plugin.quest.handler;

import com.blastedstudios.ledge.world.WorldManager;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface IWorldManagerInitializer extends Plugin{
	void setWorldManager(WorldManager world);
}
