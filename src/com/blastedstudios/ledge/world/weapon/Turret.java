package com.blastedstudios.ledge.world.weapon;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;

public class Turret {
	private final Gun gun;
	private final Vector2 location, mountLocation;
	private final float directionLow, directionHigh;
	private final Random random;
	private final String baseResource;
	private float direction;
	private Sprite gunSprite = null, weaponBaseSprite = null;
	private long reloadTime;
	
	public Turret(Gun gun, String baseResource, Vector2 location, Vector2 mountLocation,
			float direction, float directionLow, float directionHigh, Random random){
		this.gun = gun;
		this.baseResource = baseResource;
		this.location = location;
		this.mountLocation = mountLocation;
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
			gunSprite.setScale(Properties.getFloat("ragdoll.custom.scale"));
			gunSprite.setPosition(location.x - gunSprite.getWidth()/2f, location.y - gunSprite.getHeight()/2f);
			weaponBaseSprite = new Sprite(gdxRenderer.getTexture(baseResource));
			weaponBaseSprite.setScale(Properties.getFloat("turret.scale", .02f));
			weaponBaseSprite.setPosition(location.x - weaponBaseSprite.getWidth()/2f, location.y - weaponBaseSprite.getHeight()/2f);
		}
		gunSprite.setRotation((float)Math.toDegrees(direction));
		gunSprite.draw(spriteBatch);
		weaponBaseSprite.draw(spriteBatch);
		if(reloadTime != 0l && System.currentTimeMillis() > reloadTime){
			gun.setCurrentRounds(gun.getRoundsPerClip());
			reloadTime = 0l;
		}
	}
	
	public void shoot(Being source, WorldManager worldManager){
		if(gun.canAttack())
			gun.shoot(source, random, new Vector2((float)Math.cos(direction), (float)Math.sin(direction)), worldManager, location);
		if(gun.getCurrentRounds() <= 0 && reloadTime == 0l)
			reloadTime = System.currentTimeMillis() + (long)gun.getReloadSpeed();
	}
	
	public Vector2 getLocation(){
		return location;
	}

	public float getDirection() {
		return direction;
	}

	public Vector2 getMountLocation() {
		return mountLocation;
	}
}
