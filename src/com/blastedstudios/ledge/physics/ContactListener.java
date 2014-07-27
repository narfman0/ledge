package com.blastedstudios.ledge.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Melee;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private final WorldManager worldManager;
	
	public ContactListener(WorldManager worldManager){
		this.worldManager = worldManager;
	}
	
	@Override public void preSolve(Contact contact, Manifold arg1) {
		GunShot gunshot = (GunShot) (contact.getFixtureA().getBody().getUserData() instanceof GunShot ? contact.getFixtureA().getBody().getUserData() :
			contact.getFixtureB().getBody().getUserData() instanceof GunShot ? contact.getFixtureB().getBody().getUserData() : null);
		Fixture hit = contact.getFixtureA().getBody().getUserData() instanceof Being ? contact.getFixtureA() :
			contact.getFixtureB().getBody().getUserData() instanceof Being ? contact.getFixtureB() : null;
		if(gunshot != null && !gunshot.collideWithOrigin() && gunshot.getBeing().isOwned(hit))
			contact.setEnabled(false);
	}
	
	@Override public void beginContact(Contact contact) {
		Body gunshotBody = contact.getFixtureA().getBody().getUserData() instanceof GunShot ? contact.getFixtureA().getBody() :
			contact.getFixtureB().getBody().getUserData() instanceof GunShot ? contact.getFixtureB().getBody() : null;
		Fixture hit = contact.getFixtureA().getBody().getUserData() instanceof Being ? contact.getFixtureA() :
			contact.getFixtureB().getBody().getUserData() instanceof Being ? contact.getFixtureB() : null;
		Body meleeBody = contact.getFixtureA().getBody().getUserData() instanceof Melee ? contact.getFixtureA().getBody() :
			contact.getFixtureB().getBody().getUserData() instanceof Melee ? contact.getFixtureB().getBody() : null;
			
		if(gunshotBody != null){//handle projectile contact
			GunShot gunshot = (GunShot) gunshotBody.getUserData();
			if(gunshot.collideWithOrigin() || !gunshot.getBeing().isOwned(hit)){//skip if self
				if(hit != null){//handle getting shot
					Being target = (Being) hit.getBody().getUserData();
					worldManager.processHit(gunshot.getGun().getDamage(), target, gunshot.getBeing(), 
							hit, contact.getWorldManifold().getNormal(), contact.getWorldManifold().getPoints()[0]);
				}
				gunshot.beginContact(gunshotBody, hit, worldManager, contact.getWorldManifold());
			}
		}else if(meleeBody != null && hit != null){//handle melee attack
			Being target = (Being) hit.getBody().getUserData();
			Melee melee = (Melee) meleeBody.getUserData();
			melee.beginContact(worldManager, target, hit, contact, calculateMomentumImpulse(contact));
		}else if(hit != null){//handle physics object collision dmg
			Being target = (Being) hit.getBody().getUserData();
			float i = calculateMomentumImpulse(contact);
			if(i > Properties.getFloat("contact.impulse.threshold", 20f) && !target.isDead())
				worldManager.processHit(i, target, null, hit, contact.getWorldManifold().getNormal(), 
						contact.getWorldManifold().getPoints()[0]);
		}
	}
	
	private static float calculateMomentumImpulse(Contact contact){
		Vector2 velocity = contact.getFixtureA().getBody().getLinearVelocity().sub(
				contact.getFixtureB().getBody().getLinearVelocity());
//		float mass = Math.max(contact.getFixtureA().getBody().getMass(), contact.getFixtureB().getBody().getMass());
		Vector2 relativeMomentumProjected = velocity.scl(contact.getWorldManifold().getNormal());
		return relativeMomentumProjected.len2()/5f;
	}
	
	@Override public void postSolve(Contact contact, ContactImpulse oldManifold) {}
	@Override public void endContact(Contact contact) {}
}
