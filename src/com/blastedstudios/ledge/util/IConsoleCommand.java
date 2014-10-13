package com.blastedstudios.ledge.util;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.world.WorldManager;

import net.xeoh.plugins.base.Plugin;

public interface IConsoleCommand extends Plugin {
	public String[] getMatches();
	public void execute(final WorldManager world, final GameplayScreen screen, String[] tokens);
}
