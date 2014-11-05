package com.blastedstudios.ledge.plugin.console;

import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

public abstract class AbstractConsole implements IConsoleCommand{
	protected WorldManager world;
	protected GameplayScreen screen;
	
	@Override public void initialize(final WorldManager world, final GameplayScreen screen){
		this.world = world;
		this.screen = screen;
	}
}
