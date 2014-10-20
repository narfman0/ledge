package com.blastedstudios.ledge.ui.gameplay.hud;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.Player;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class BossHP extends AbstractHUDElement{
	private final LinkedList<NPC> npcs = new LinkedList<>();
	private Sprite barOutsideSprite, barInsideSprite;

	@Override public AbstractHUDElement initialize(Skin skin, BitmapFont font, Player player){
		super.initialize(skin, font, player);
		npcs.clear();
		barOutsideSprite = skin.getSprite("hud-bar-outside");
		barInsideSprite = skin.getSprite("hud-bar-inside");
		return this;
	}

	@Override public void render(SpriteBatch spriteBatch) {
		int i=0;
		for(Iterator<NPC> iter = npcs.iterator(); iter.hasNext();){
			NPC npc = iter.next();
			float percentHP = npc.getHp()/npc.getMaxHp();
			int width = barInsideSprite.getRegionWidth();
			barOutsideSprite.setPosition(Gdx.graphics.getWidth()/2f - barOutsideSprite.getWidth()/2f,
					Gdx.graphics.getHeight() - (i*64 + 48));
			barOutsideSprite.draw(spriteBatch);
			barOutsideSprite.setColor(skin.getColor("yellow"));
			barInsideSprite.setPosition(Gdx.graphics.getWidth()/2f - barOutsideSprite.getWidth()/2f,
					Gdx.graphics.getHeight() - (i*64 + 44));
			barInsideSprite.setColor(skin.getColor("yellow"));
			barInsideSprite.setRegionWidth((int)(width * percentHP));
			barInsideSprite.setScale(percentHP, 1f);
			barInsideSprite.draw(spriteBatch);
			barInsideSprite.setRegionWidth(width);
			if(npc.isDead())
				iter.remove();
			i++;
		}
	}
	
	@Override public void npcAdded(NPC npc, boolean boss){
		if(boss)
			npcs.add(npc);
	}
}
