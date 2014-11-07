package com.blastedstudios.ledge.ui.gameplay.console;

import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.util.Log.ILogHandler;
import com.blastedstudios.gdxworld.util.Properties;

@PluginImplementation
public class History implements ILogHandler{
	static final LinkedList<ConsoleOutputStruct> items = new LinkedList<>();

	@Override
	public void debug(String tag, String message) {
		add(tag + ": " + message, Color.GRAY);
	}

	@Override
	public void error(String tag, String message) {
		add(tag + ": " + message, Color.MAROON);
	}

	@Override
	public void log(String tag, String message) {
		add(tag + ": " + message, Color.DARK_GRAY);
	}
	
	public static void add(String msg, Color color){
		items.add(new ConsoleOutputStruct(msg, color));
		while(items.size() > Properties.getInt("history.size", 100))
			items.removeFirst();
	}
}
