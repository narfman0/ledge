package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;

@PluginImplementation
public class InputConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"input"};
	}

	@Override public void execute(String[] tokens) {
		boolean enabled = Boolean.parseBoolean(tokens[1]);
		world.setInputEnable(enabled);
		Gdx.app.log("InputConsole.execute", "Input: " + enabled);
	}

	@Override public String getHelp() {
		return "Input command, making input enabled or disabled. "
				+ "Usage:\ninput <true/false>, where true enables input";
	}
}