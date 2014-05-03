package com.blastedstudios.ledge.world.weapon.shot;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.Being.BodyPart;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.RocketLauncher;

public class Rocket extends GunShot {
	private final ParticleEffect trail, explosion;
	
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

	@Override public void handleContact(Body body, WorldManager worldManager, WorldManifold manifold){
		RocketLauncher launcher = (RocketLauncher) gun;//TODO: make generic
		HashMap<Being, Float> nearbyBeings = new HashMap<>();
		//provide impulse on bodies
		for(Body worldBody : worldManager.getBodiesIterable()){
			Vector2 direction = worldBody.getWorldCenter().cpy().sub(body.getPosition());
			float distance = direction.len();
			if(distance < launcher.getMaxDistance()){
				if(worldBody.getUserData() instanceof Being){
					float closestDistance = distance;
					if(nearbyBeings.containsKey(worldBody.getUserData()))
						closestDistance = Math.min(nearbyBeings.get(worldBody.getUserData()), distance);
					nearbyBeings.put((Being) worldBody.getUserData(), closestDistance);
				}
				//if a being, this is huge because each body gets in
				Vector2 impulse = direction.nor().scl(launcher.getBaseImpulse() / (float)Math.max(1, distance));
				worldBody.applyLinearImpulse(impulse, worldBody.getPosition(), true);
			}
		}
		//handle damage on nearby players
		for(Entry<Being,Float> being : nearbyBeings.entrySet()){
			Gdx.app.log("RocketLauncher.handleContact","Calculate rocket baseDamage for " + being.getKey().getName());
			worldManager.processHit(launcher.calculateLinearDmg(being.getValue()), being.getKey(), getBeing(), 
					being.getKey().getRagdoll().getBodyPart(BodyPart.torso).getFixtureList().get(0), manifold.getNormal());
		}
		//send off particles to particle manager
		explosion.setPosition(body.getPosition().x, body.getPosition().y);
		explosion.start();
		worldManager.transferParticles(trail, explosion);

		body.setUserData(WorldManager.REMOVE_USER_DATA);
		setCanRemove(true);
	}
}
