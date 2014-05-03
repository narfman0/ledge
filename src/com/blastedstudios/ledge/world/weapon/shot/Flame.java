package com.blastedstudios.ledge.world.weapon.shot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Gun;

public class Flame extends GunShot {
	public final ParticleEffect flame;
	private long timeToRemove = -1l;
	private BodyType bodyType = null;
	
	public Flame(Being origin, Vector2 dir, Gun gun){
		super(origin, dir, gun);
		flame = new ParticleEffect();
		flame.load(Gdx.files.internal("data/particles/flame.p"), Gdx.files.internal("data/particles"));
		flame.start();
	}
	
	@Override public void render(float dt, SpriteBatch spriteBatch, GDXRenderer gdxRenderer, 
			Body body, WorldManager worldManager){
		super.render(dt, spriteBatch, gdxRenderer, body, worldManager);
		flame.setPosition(body.getPosition().x, body.getPosition().y);
		flame.draw(spriteBatch, dt);
		if(isTimeToRemoveSet() && System.currentTimeMillis() >= timeToRemove){
			worldManager.transferParticles(flame);
			body.setUserData(WorldManager.REMOVE_USER_DATA);
			setCanRemove(true);
		}
		if(bodyType != null && bodyType != body.getType())
			body.setType(bodyType);
	}
	
	@Override public void handleContact(Body body, WorldManager worldManager, WorldManifold manifold){
		if(!isTimeToRemoveSet()){
			timeToRemove = System.currentTimeMillis() + Properties.getInt("flame.contact.duration", 1500);
			bodyType = BodyType.StaticBody;
		}
	}
	
	public boolean isTimeToRemoveSet(){
		return timeToRemove != -1l;
	}
}
