package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class JetpackConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(final WorldManager world, final GameplayScreen screen, String[] tokens) {
		if(tokens[1].equalsIgnoreCase("jetpack"))
			world.getPlayer().getStats().setJetpackMax(Float.parseFloat(tokens[2]));
	}

	@Override public String getHelp() {
		return "Jetpack console command changes max power for player. Usage:\n"
				+ "player jetpack <max>, where max is an integer e.g. 100";
	}
}
