package com.blastedstudios.ledge.util;

import net.xeoh.plugins.base.Plugin;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.world.WorldManager;

public interface IConsoleCommand extends Plugin {
	void initialize(final WorldManager world, final GameplayScreen screen);
	String[] getMatches();
	void execute(String[] tokens);
	String getHelp();
}
