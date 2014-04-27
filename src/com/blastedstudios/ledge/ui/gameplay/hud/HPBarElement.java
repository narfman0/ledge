package com.blastedstudios.ledge.ui.gameplay.hud;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.ledge.world.being.Player;

@PluginImplementation
public class HPBarElement extends AbstractHUDElement{
	private Sprite barOutsideSprite, barInsideSprite;
	
	@Override public AbstractHUDElement initialize(Skin skin, BitmapFont font, Player player){
		super.initialize(skin, font, player);
		barOutsideSprite = skin.getSprite("hud-bar-outside");
		barInsideSprite = skin.getSprite("hud-bar-inside");
		return this;
	}
	
	@Override public void render(SpriteBatch spriteBatch) {
		float percentHP = player.getHp()/player.getMaxHp();
		int width = barInsideSprite.getRegionWidth();
		barOutsideSprite.setPosition(4, 4);
		barOutsideSprite.draw(spriteBatch);
		barOutsideSprite.setColor(skin.getColor("yellow"));
		barInsideSprite.setPosition(10, 9);
		barInsideSprite.setColor(skin.getColor("yellow"));
		barInsideSprite.setRegionWidth((int)(width * percentHP));
		barInsideSprite.draw(spriteBatch);
		barInsideSprite.setRegionWidth(width);

//		drawString(spriteBatch, "HP: " + (int)player.getHp() + "/" + (int)player.getMaxHp(), 
//				INSET, INSET, XAlign.RIGHT, YAlign.UP);
	}
}
