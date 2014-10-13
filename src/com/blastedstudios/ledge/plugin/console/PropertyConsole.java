package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class PropertyConsole implements IConsoleCommand {
	@Override public String[] getMatches() {
		return new String[]{"property","prop"};
	}

	@Override public void execute(final WorldManager world, final GameplayScreen screen, String[] tokens) {
		if(tokens.length == 2)
			Gdx.app.log("PropertyConsole.execute", "Property " + tokens[1] + 
					" has value: " + Properties.get(tokens[1]));
		else if(tokens.length == 3)
			Properties.set(tokens[1], tokens[2]);
	}
}
