package com.blastedstudios.ledge.ui.gameplay.hud;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HPComponent {
	private final Sprite barOutsideSprite, barInsideSprite;
	
	public HPComponent(Skin skin){
		barOutsideSprite = skin.getSprite("hud-bar-outside");
		barOutsideSprite.setColor(skin.getColor("yellow"));
		barInsideSprite = skin.getSprite("hud-bar-inside");
		barInsideSprite.setColor(skin.getColor("yellow"));
	}
	
	public void render(SpriteBatch spriteBatch, float percentHP, Vector2 position){
		int width = barInsideSprite.getRegionWidth();
		barOutsideSprite.setPosition(position.x - barOutsideSprite.getWidth()/2f, 
				position.y - barOutsideSprite.getHeight()/2f);
		barInsideSprite.setPosition(position.x - barInsideSprite.getWidth()/2f - ((1f-percentHP)*width/2), 
				position.y - barInsideSprite.getHeight()/2f);
		barOutsideSprite.draw(spriteBatch);
		barInsideSprite.setRegionWidth((int)(width * percentHP));
		barInsideSprite.setScale(percentHP, 1f);
		barInsideSprite.draw(spriteBatch);
		barInsideSprite.setRegionWidth(width);
	}
}
