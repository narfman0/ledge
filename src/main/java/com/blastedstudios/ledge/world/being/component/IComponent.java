package com.blastedstudios.ledge.world.being.component;

import net.xeoh.plugins.base.Plugin;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.IBeingListener;

public interface IComponent extends IBeingListener,Plugin,Cloneable{
	IComponent initialize(Being being);
	void render(float dt, Batch batch, AssetManager sharedAssets,
			GDXRenderer gdxRenderer, boolean facingLeft, boolean paused);
	boolean keyDown(int key, WorldManager worldManager);
	boolean keyUp(int key, WorldManager worldManager);
}
