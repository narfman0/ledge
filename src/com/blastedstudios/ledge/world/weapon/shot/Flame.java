package com.blastedstudios.ledge.world.weapon.shot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Gun;

public class Flame extends GunShot {
	public final ParticleEffect flame;
	
	public Flame(Being origin, Vector2 dir, Gun gun){
		super(origin, dir, gun);
		flame = new ParticleEffect();
		flame.load(Gdx.files.internal("data/particles/flame.p"), Gdx.files.internal("data/particles"));
		flame.start();
	}
	
	@Override public void render(float dt, SpriteBatch spriteBatch, GDXRenderer gdxRenderer, Body body){
		super.render(dt, spriteBatch, gdxRenderer, body);
		flame.setPosition(body.getPosition().x, body.getPosition().y);
		flame.draw(spriteBatch, dt);
	}
}
