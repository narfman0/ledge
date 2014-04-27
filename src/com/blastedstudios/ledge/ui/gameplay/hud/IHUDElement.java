package com.blastedstudios.ledge.ui.gameplay.hud;

import net.xeoh.plugins.base.Plugin;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.ledge.world.being.Player;

public interface IHUDElement extends Plugin{
	public void render(final SpriteBatch spriteBatch);
	public IHUDElement initialize(Skin skin, BitmapFont font, Player player);
}
