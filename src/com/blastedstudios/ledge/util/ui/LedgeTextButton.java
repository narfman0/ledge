package com.blastedstudios.ledge.util.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LedgeTextButton extends TextButton {
	public LedgeTextButton(String name, Skin skin, EventListener... listeners){
		super(name, skin);
		try{
			setColor(UIHelper.getColor(skin, "textbutton-background", "button-background", "background", "secondary"));
		}catch(Exception e){}
		for(EventListener listener : listeners)
			addListener(listener);
	}
}
