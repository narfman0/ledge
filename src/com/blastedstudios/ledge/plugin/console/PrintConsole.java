package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;

@PluginImplementation
public class PrintConsole extends AbstractConsole {
	@Override public String[] getMatches() {
		return new String[]{"print"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens[1].equalsIgnoreCase("position") || tokens[1].equalsIgnoreCase("location"))
			Gdx.app.log("PrintConsole.execute","Player location: " + world.getPlayer().getPosition());
	}

	@Override public String getHelp() {
		return "Print command to print current values for position. Usage:\nprint position";
	}
}
