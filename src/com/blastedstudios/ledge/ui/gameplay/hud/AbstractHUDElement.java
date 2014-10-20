package com.blastedstudios.ledge.ui.gameplay.hud;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.Player;

public abstract class AbstractHUDElement implements IHUDElement{
	protected static final int INSET = Properties.getInt("hud.inset", 4);
	protected BitmapFont font;
	protected Player player;
	protected Skin skin;
	
	public AbstractHUDElement initialize(Skin skin, BitmapFont font, Player player){
		this.skin = skin;
		this.font = font;
		this.player = player;
		return this;
	}
	
	protected void drawString(SpriteBatch spriteBatch, String string, float x, 
			float y, XAlign xAlign, YAlign yAlign){
		switch(xAlign){
		case LEFT:
			x -= font.getBounds(string).width;
			break;
		case MIDDLE:
			x -= (font.getBounds(string).width / 2);
			break;
		case RIGHT:
			break;
		}
		switch(yAlign){
		case UP:
			y += font.getBounds(string).height;
			break;
		case DOWN:
			break;
		}
		font.draw(spriteBatch, string, x, y);
	}

	public abstract void render(final SpriteBatch spriteBatch);
	public void npcAdded(NPC npc, boolean boss){}
	protected enum XAlign{LEFT,MIDDLE,RIGHT}
	protected enum YAlign{UP,DOWN}
}
