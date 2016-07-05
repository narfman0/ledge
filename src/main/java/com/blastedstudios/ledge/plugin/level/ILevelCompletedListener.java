package com.blastedstudios.ledge.plugin.level;

import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.ledge.world.WorldManager;

import net.xeoh.plugins.base.Plugin;

public interface ILevelCompletedListener extends Plugin{
	 void levelComplete(final boolean success, final String nextLevelName, WorldManager world, GDXLevel level);
}
