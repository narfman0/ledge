package com.blastedstudios.ledge.util;

import com.blastedstudios.ledge.world.WorldManager;

import net.xeoh.plugins.base.Plugin;

public interface IConsoleCommand extends Plugin {
	public String[] getMatches();
	public void execute(WorldManager world, String[] tokens);
}
