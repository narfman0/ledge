package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class AddXPConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(WorldManager world, String[] tokens) {
		if(tokens.length == 3 && tokens[1].equalsIgnoreCase("addxp")){
			long amount = Integer.parseInt(tokens[2]);
			Gdx.app.log("AddXPConsole.execute", "Adding: " + amount + " xp to player");
			world.getPlayer().addXp(amount);
		}
	}
}