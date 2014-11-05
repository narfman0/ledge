package com.blastedstudios.ledge.ui.gameplay;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;

public class ConsoleWindow extends AbstractWindow{
	private final TextField text;
	
	public ConsoleWindow(final Skin skin, final WorldManager world, 
			final GameplayScreen screen, final EventListener listener) {
		super("Console", skin);
		for(IConsoleCommand command : PluginUtil.getPlugins(IConsoleCommand.class))
			command.initialize(world, screen);
		text = new TextField("", skin);
		text.setMessageText("<enter command>");
		text.setWidth(200);
		TextButton button = new TextButton("Send", skin);
		button.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				execute();
				listener.handle(event);
			}
		});
		add(text);
		add(button);
		pack();
	}

	/**
	 * Interpret text and execute command therein
	 */
	public void execute() {
		try{
			String[] tokens = text.getText().split(" ");
			for(IConsoleCommand command : PluginUtil.getPlugins(IConsoleCommand.class))
				for(String match : command.getMatches())
					try{
						if(tokens[0].matches(match))
							command.execute(tokens);
					}catch(Exception e){
						Log.error("ConsoleWindow.execute", "Failed to execute command: " + text.getText());
						e.printStackTrace();
					}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
