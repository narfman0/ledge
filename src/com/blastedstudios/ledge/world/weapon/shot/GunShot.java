package com.blastedstudios.ledge.world.weapon.shot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Gun;

public class GunShot {
	protected Being origin;
	protected Vector2 dir;
	protected Gun gun;
	private boolean canRemove;
	
	public GunShot(){}
	
	public GunShot(Being origin, Vector2 dir, Gun gun){
		this.origin = origin;
		this.dir = dir;
		this.gun = gun;
	}
	
	public Being getBeing() {
		return origin;
	}
	
	public void setOrigin(Being being) {
		this.origin = being;
	}
	
	public Vector2 getDir() {
		return dir;
	}
	
	public void setDir(Vector2 dir) {
		this.dir = dir;
	}

	public Gun getGun() {
		return gun;
	}

	public void setGun(Gun gun) {
		this.gun = gun;
	}
	
	public void render(float dt, SpriteBatch spriteBatch, GDXRenderer gdxRenderer, 
			Body body, WorldManager worldManager){
		WorldManager.drawTexture(spriteBatch, gdxRenderer, body, 
				gun.getAmmoType().textureName, Properties.getFloat("gun.render.scale", .02f));
	}

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}
	
	public void postSolve(Body gunshotBody, Fixture hit, WorldManager worldManager, WorldManifold manifold){
		if(!origin.getRagdoll().isOwned(hit)){
			gunshotBody.setUserData(WorldManager.REMOVE_USER_DATA);
			canRemove = true;
		}
	}
	
	public boolean collideWithOrigin(){
		return false;
	}
}
