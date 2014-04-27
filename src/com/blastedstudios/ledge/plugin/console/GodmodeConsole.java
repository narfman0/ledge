package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class GodmodeConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(WorldManager world, String[] tokens) {
		if(tokens.length == 3 && tokens[1].equalsIgnoreCase("godmode")){
			boolean enabled = Boolean.parseBoolean(tokens[2]);
			Properties.set("character.godmode", enabled+"");
			Gdx.app.log("GodmodeConsole.execute", "Godmode set to: " + enabled);
		}
	}
}