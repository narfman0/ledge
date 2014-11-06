package com.blastedstudios.ledge.ui;

import java.util.HashMap;

import com.badlogic.gdx.Input.Keys;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.main.MainScreen;

public abstract class LedgeScreen extends AbstractScreen {
	private final HashMap<ActionType, AbstractInputHandler> inputHandlers = new HashMap<>();
	
	public LedgeScreen(GDXGame game) {
		super(game, MainScreen.SKIN_PATH);
	}
	
	public void register(ActionType actionType, AbstractInputHandler inputHandler){
		inputHandlers.put(actionType, inputHandler);
	}

	@Override public boolean keyDown(int key) {
		AbstractInputHandler handler = inputHandlers.get(keyToAction(key));
		if(handler != null)
			handler.down();
		return false;
	}

	@Override public boolean keyUp(int key) {
		AbstractInputHandler handler = inputHandlers.get(keyToAction(key));
		if(handler != null)
			handler.up();
		return false;
	}
	
	private static ActionType keyToAction(int key){
		if(Properties.getInt("input.up", Keys.W) == key)
			return ActionType.UP;
		else if(Properties.getInt("input.down", Keys.S) == key)
			return ActionType.DOWN;
		else if(Properties.getInt("input.left", Keys.A) == key)
			return ActionType.LEFT;
		else if(Properties.getInt("input.right", Keys.D) == key)
			return ActionType.RIGHT;
		else if(Properties.getInt("input.action", Keys.E) == key ||
				Properties.getInt("input.action2", Keys.ENTER) == key)
			return ActionType.ACTION;
		else if(Properties.getInt("input.crouch", Keys.CONTROL_LEFT) == key ||
				Properties.getInt("input.crouch2", Keys.C) == key)
			return ActionType.CROUCH;
		else if(Properties.getInt("input.back", Keys.ESCAPE) == key)
			return ActionType.BACK;
		else if(Properties.getInt("input.reload", Keys.R) == key)
			return ActionType.RELOAD;
		else if(Properties.getInt("input.inventory", Keys.I) == key)
			return ActionType.INVENTORY;
		return ActionType.UNDEFINED;
	}
	
	public enum ActionType {
		LEFT, RIGHT, DOWN, UP, ACTION, MODIFIER, INVENTORY, RELOAD, FIRE, CROUCH, BACK, UNDEFINED
	}
	
	public abstract class AbstractInputHandler {
		public void down(){}
		public void up(){}
	}
}
