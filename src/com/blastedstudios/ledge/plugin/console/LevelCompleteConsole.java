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
		if(tokens[1].equalsIgnoreCase("complete")){
			if(tokens.length == 2){
				if(screen != null){
					screen.levelComplete(true, "");
					Gdx.app.log("LevelCompleteConsole.execute", "Current level completed");
				}
			}else if(tokens.length == 3){
				String levelName = tokens[2];
				world.getPlayer().setLevelCompleted(levelName, true);
				Gdx.app.log("LevelCompleteConsole.execute", "Level marked completed: " + levelName);
			}
		}
	}

	@Override public String getHelp() {
		return "Level complete command either succeeds current level with no arguments or completes <levelname>. Usage:\n"
				+ "level complete, completes currently active level\n"
				+ "level complete <levelname>, marks the level with name <levelname> complete";
	}
}
