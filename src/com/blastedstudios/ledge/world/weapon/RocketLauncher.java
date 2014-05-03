package com.blastedstudios.ledge.world.weapon;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.Being.BodyPart;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;
import com.blastedstudios.ledge.world.weapon.shot.Rocket;

public class RocketLauncher extends Gun {
	private static final long serialVersionUID = 1L;
	private float maxDistance, baseDamage, baseImpulse;

	@Override public String toString(){
		return "[RocketLauncher name:" + name + " dmg: " + getDamage() + "]";
	}

	@Override public void handleContact(Body body, GunShot gunshot, WorldManager worldManager, WorldManifold manifold){
		HashMap<Being, Float> nearbyBeings = new HashMap<>();
		//provide impulse on bodies
		for(Body worldBody : worldManager.getBodiesIterable()){
			Vector2 direction = worldBody.getWorldCenter().cpy().sub(body.getPosition());
			float distance = direction.len();
			if(distance < maxDistance){
				if(worldBody.getUserData() instanceof Being){
					float closestDistance = distance;
					if(nearbyBeings.containsKey(worldBody.getUserData()))
						closestDistance = Math.min(nearbyBeings.get(worldBody.getUserData()), distance);
					nearbyBeings.put((Being) worldBody.getUserData(), closestDistance);
				}
				//if a being, this is huge because each body gets in
				Vector2 impulse = direction.nor().scl(baseImpulse / (float)Math.max(1, distance));
				worldBody.applyLinearImpulse(impulse, worldBody.getPosition(), true);
			}
		}
		//handle damage on nearby players
		for(Entry<Being,Float> being : nearbyBeings.entrySet()){
			Gdx.app.log("RocketLauncher.handleContact","Calculate rocket baseDamage for " + being.getKey().getName());
			worldManager.processHit(calculateLinearDmg(being.getValue()), being.getKey(), gunshot.getBeing(), 
					being.getKey().getRagdoll().getBodyPart(BodyPart.torso).getFixtureList().get(0), manifold.getNormal());
		}
		//send off particles to particle manager
		Rocket rocket = (Rocket) gunshot;
		rocket.explosion.setPosition(body.getPosition().x, body.getPosition().y);
		rocket.explosion.start();
		worldManager.transferParticles(rocket.trail, rocket.explosion);

		body.setUserData(WorldManager.REMOVE_USER_DATA);
		worldManager.getGunshots().remove(body);
	}
	
	@Override public GunShot createGunShot(Being origin, Vector2 dir){
		return new Rocket(origin, dir, this);
	}
	
	private float calculateLinearDmg(float distance){
		return baseDamage*( (maxDistance-distance)/maxDistance );
	}
}
