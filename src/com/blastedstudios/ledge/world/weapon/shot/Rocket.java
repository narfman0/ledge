package com.blastedstudios.ledge.world.weapon.shot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Gun;

public class Rocket extends GunShot {
	public final ParticleEffect trail, explosion;
	
	public Rocket(Being origin, Vector2 dir, Gun gun){
		super(origin, dir, gun);
		trail = new ParticleEffect();
		trail.load(Gdx.files.internal("data/particles/rocketTrail.p"), Gdx.files.internal("data/particles"));
		trail.start();
		explosion = new ParticleEffect();
		explosion.load(Gdx.files.internal("data/particles/rocketExplosion.p"), Gdx.files.internal("data/particles"));
	}
	
	@Override public void render(float dt, SpriteBatch spriteBatch, GDXRenderer gdxRenderer, 
			Body body, WorldManager worldManager){
		super.render(dt, spriteBatch, gdxRenderer, body, worldManager);
		trail.setPosition(body.getPosition().x, body.getPosition().y);
		trail.draw(spriteBatch, dt);
		explosion.setPosition(body.getPosition().x, body.getPosition().y);
	}
}
