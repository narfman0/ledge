package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.DropManager;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class GunDropConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"gun"};
	}

	@Override public void execute(WorldManager world, String[] tokens) {
		if(tokens[1].equalsIgnoreCase("drop")){
			if(tokens.length == 2)
				Gdx.app.log("GunDropConsole.execute", "Gun drop probability: " + 
						Properties.getFloat(DropManager.GUN_DROP_PROPERTY, .05f));
			else if(tokens.length == 3){
				Properties.set(DropManager.GUN_DROP_PROPERTY, "" + Float.parseFloat(tokens[2]));
				Gdx.app.log("GunDropConsole.execute", "Gun drop probability set to: " + 
						Properties.getFloat(DropManager.GUN_DROP_PROPERTY));
			}
		}
	}
}
