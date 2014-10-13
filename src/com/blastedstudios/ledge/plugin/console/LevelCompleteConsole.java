package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

@PluginImplementation
public class LevelCompleteConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"level"};
	}

	@Override public void execute(final WorldManager world, final GameplayScreen screen, String[] tokens) {
		if(screen == null)
			return;
		if(tokens[1].equalsIgnoreCase("complete")){
			if(tokens.length == 2){
				screen.levelComplete(true, "");
				Gdx.app.log("LevelCompleteConsole.execute", "Current level completed");
			}else if(tokens.length == 3){
				String levelName = tokens[2];
				world.getPlayer().setLevelCompleted(levelName, true);
				Gdx.app.log("LevelCompleteConsole.execute", "Level marked completed: " + levelName);
			}
		}
	}
}
