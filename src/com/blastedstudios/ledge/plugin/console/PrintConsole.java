package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class PrintConsole implements IConsoleCommand {
	@Override public String[] getMatches() {
		return new String[]{"print"};
	}

	@Override public void execute(WorldManager world, String[] tokens) {
		if(tokens[1].equalsIgnoreCase("position") || tokens[1].equalsIgnoreCase("location"))
			Gdx.app.log("PrintConsole.execute","Player location: " + world.getPlayer().getPosition());
	}
}
