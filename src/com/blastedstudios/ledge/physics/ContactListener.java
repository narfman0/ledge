package com.blastedstudios.ledge.physics;

import com.badlogic.gdx.Gdx;
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
	private WorldManager worldManager;
	
	public ContactListener(WorldManager worldManager){
		this.worldManager = worldManager;
	}

	@Override public void postSolve(Contact contact, ContactImpulse oldManifold) {
		Body gunshotBody = contact.getFixtureA().getBody().getUserData() instanceof GunShot ? contact.getFixtureA().getBody() :
			contact.getFixtureB().getBody().getUserData() instanceof GunShot ? contact.getFixtureB().getBody() : null;
		Fixture hit = contact.getFixtureA().getBody().getUserData() instanceof Being ? contact.getFixtureA() :
			contact.getFixtureB().getBody().getUserData() instanceof Being ? contact.getFixtureB() : null;
		Body meleeBody = contact.getFixtureA().getBody().getUserData() instanceof Melee ? contact.getFixtureA().getBody() :
			contact.getFixtureB().getBody().getUserData() instanceof Melee ? contact.getFixtureB().getBody() : null;
		if(gunshotBody != null){//handle projectile contact
			GunShot gunshot = (GunShot) gunshotBody.getUserData();
			if(hit != null){//handle getting shot
				Being target = (Being) hit.getBody().getUserData();
				worldManager.processHit(gunshot.getGun().getDamage(), target, gunshot.getBeing(), 
						hit, contact.getWorldManifold().getNormal());
			}
			gunshot.getGun().handleContact(gunshotBody, gunshot, worldManager, contact.getWorldManifold());
		}else if(meleeBody != null && hit != null){//handle melee attack
			Being target = (Being) hit.getBody().getUserData();
			Melee melee = (Melee) meleeBody.getUserData();
			float impulse = 0;
			for(float normalImpulse : oldManifold.getNormalImpulses())
				impulse += normalImpulse;
			if(impulse > Properties.getFloat("melee.contact.impulse.threshold", .02f)){
				Gdx.app.log("ContactListener.postSolve","HandleMelee - scale damage with impulse impulse: " + impulse);
				worldManager.processHit(melee.getDamage() * (float)Melee.impulseToDamageScalar(impulse, Gdx.graphics.getRawDeltaTime()), 
						target, melee.getOwner(), hit, contact.getWorldManifold().getNormal());
			}
		}else if(hit != null){//handle physics object collision dmg
			Being target = (Being) hit.getBody().getUserData();
			for(float i : oldManifold.getNormalImpulses())
				if(i > Properties.getFloat("melee.contact.impulse.threshold", 20f) && !target.isDead()){
					Gdx.app.log("ContactListener.postSolve","HandleEnvironment - scale damage with impulse: " + i);
					float damage = (float)Math.log(i*100)*5f;
					worldManager.processHit(damage, target, null, hit, contact.getWorldManifold().getNormal());
				}
		}
	}

	@Override public void beginContact(Contact contact) {}
	@Override public void endContact(Contact contact) {}
	@Override public void preSolve(Contact contact, Manifold arg1) {}
}
