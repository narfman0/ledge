package com.blastedstudios.ledge.world.weapon;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;

public class Turret {
	private final Gun gun;
	private final Vector2 location;
	private final float directionLow, directionHigh;
	private float direction;
	private final Random random;
	private Sprite gunSprite = null;
	
	public Turret(Gun gun, Vector2 location, float direction, float directionLow, float directionHigh, Random random){
		this.gun = gun;
		this.location = location;
		this.direction = direction;
		this.directionLow = directionLow;
		this.directionHigh = directionHigh;
		this.random = random;
	}
	
	public void aim(float direction){
		this.direction = Math.max(directionLow, Math.min(directionHigh, direction));
	}
	
	public void render(float dt, SpriteBatch spriteBatch, GDXRenderer gdxRenderer, WorldManager worldManager) {
		if(gunSprite == null){
			gunSprite = new Sprite(gdxRenderer.getTexture(gun.getResource() + ".png"));
			gunSprite.setPosition(location.x, location.y);
		}
		gunSprite.setRotation((float)Math.toDegrees(direction));
		gunSprite.draw(spriteBatch);
	}
	
	public void shoot(Being source, WorldManager worldManager){
		gun.shoot(source, random, new Vector2((float)Math.cos(direction), (float)Math.sin(direction)), worldManager, location);
	}
	
	public Vector2 getLocation(){
		return location;
	}

	public float getDirection() {
		return direction;
	}
}
