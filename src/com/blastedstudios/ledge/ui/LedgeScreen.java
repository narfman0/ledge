package com.blastedstudios.ledge.ui;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.main.MainScreen;

public abstract class LedgeScreen extends AbstractScreen {
	protected final HashMap<ActionType, AbstractInputHandler> inputHandlers = new HashMap<>();
	
	public LedgeScreen(GDXGame game) {
		super(game, MainScreen.SKIN_PATH);
	}
	
	public void register(ActionType actionType, AbstractInputHandler inputHandler){
		inputHandlers.put(actionType, inputHandler);
	}

	@Override public boolean keyDown(int key) {
		AbstractInputHandler handler = inputHandlers.get(ActionType.fromKey(key));
		if(handler != null)
			handler.down();
		return false;
	}

	@Override public boolean keyUp(int key) {
		AbstractInputHandler handler = inputHandlers.get(ActionType.fromKey(key));
		if(handler != null)
			handler.up();
		return false;
	}
	
	public enum ActionType {
		LEFT(Properties.getInt("input.left", Keys.A), Properties.getInt("input.left2", Keys.LEFT)),
		RIGHT(Properties.getInt("input.right", Keys.D), Properties.getInt("input.right2", Keys.RIGHT)),
		DOWN(Properties.getInt("input.down", Keys.S), Properties.getInt("input.down2", Keys.DOWN)),
		UP(Properties.getInt("input.up", Keys.W), Properties.getInt("input.up2", Keys.UP)),
		ACTION(Properties.getInt("input.action", Keys.E), Properties.getInt("input.action2", Keys.ENTER)),
		INVENTORY(Properties.getInt("input.inventory", Keys.I), Properties.getInt("input.inventory2", Keys.O)),
		RELOAD(Properties.getInt("input.reload", Keys.R), Properties.getInt("input.reload2", Keys.DEL)),
		CROUCH(Properties.getInt("input.crouch", Keys.CONTROL_LEFT), Properties.getInt("input.crouch2", Keys.C)),
		BACK(Properties.getInt("input.back", Keys.ESCAPE), Properties.getInt("input.back2", Keys.BACK)),
		MODIFIER(Properties.getInt("input.modifier", Keys.SHIFT_LEFT), Properties.getInt("input.modifier2", Keys.SHIFT_RIGHT)),
		UNDEFINED(-1,-1);
		
		public int key1, key2;
		
		private ActionType(int key1, int key2){
			this.key1 = key1;
			this.key2 = key2;
		}
		
		public static ActionType fromKey(int key){
			if(key == -1)
				return UNDEFINED;
			for(ActionType actionType : values())
				if(actionType.matches(key))
					return actionType;
			return UNDEFINED;
		}
		
		public boolean matches(int key){
			return key1 == key || key2 == key;
		}
		
		public boolean isPressed(){
			return Gdx.input.isKeyPressed(key1) || Gdx.input.isKeyPressed(key2);
		}
	}
	
	public abstract class AbstractInputHandler {
		public void down(){}
		public void up(){}
	}
}
