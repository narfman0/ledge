package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class TeleportConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"teleport","tp"};
	}

	@Override public void execute(final WorldManager world, final GameplayScreen screen, String[] tokens) {
		float x, y;
		if(tokens[1].contains(",")){
			x = Float.parseFloat(tokens[1].split(",")[0].trim());
			y = Float.parseFloat(tokens[1].split(",")[1].trim());
		}else{
			x = Float.parseFloat(tokens[1]);
			y = Float.parseFloat(tokens[2]);
		}
		world.getPlayer().setPosition(x, y, 0);
	}
}
