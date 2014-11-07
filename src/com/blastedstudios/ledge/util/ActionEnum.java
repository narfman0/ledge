package com.blastedstudios.ledge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.blastedstudios.gdxworld.util.Properties;

public enum ActionEnum {
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
	CONSOLE(Properties.getInt("input.console", Keys.PERIOD), Properties.getInt("input.console2", Keys.MINUS)),
	UNDEFINED(-1,-1);
	
	public int key1, key2;
	
	private ActionEnum(int key1, int key2){
		this.key1 = key1;
		this.key2 = key2;
	}
	
	public static ActionEnum fromKey(int key){
		if(key == -1)
			return UNDEFINED;
		for(ActionEnum actionType : values())
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
