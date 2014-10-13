package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class HelpConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"help", "h"};
	}

	@Override public void execute(WorldManager world, GameplayScreen screen, String[] tokens) {
		for(IConsoleCommand command : PluginUtil.getPlugins(IConsoleCommand.class))
			Gdx.app.log("HelpConsole.execute", command.getHelp());
		Gdx.app.log("HelpConsole.execute", "Direct debug commands are enabled with the debug.commands property."
				+ " They are activated by holding LEFT_CONTROL and pressing a key. They are:");
		Gdx.app.log("HelpConsole.execute", "F6: toggles debug.draw property");
		Gdx.app.log("HelpConsole.execute", "F9: generate a drop");
		Gdx.app.log("HelpConsole.execute", "F10: spawn NPC");
		Gdx.app.log("HelpConsole.execute", "F12: beat current level");
	}

	@Override
	public String getHelp() {
		return "Help command prints information on available console commands. Usage:\nhelp";
	}
}