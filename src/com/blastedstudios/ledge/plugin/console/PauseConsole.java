package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class PauseConsole implements IConsoleCommand {
	@Override public String[] getMatches() {
		return new String[]{"pause"};
	}

	@Override public void execute(final WorldManager world, final GameplayScreen screen, String[] tokens) {
		world.pause(Boolean.parseBoolean(tokens[1]));
	}
}
