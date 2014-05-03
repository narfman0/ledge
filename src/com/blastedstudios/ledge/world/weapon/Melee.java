package com.blastedstudios.ledge.world.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.blastedstudios.gdxworld.physics.PhysicsHelper;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.physics.IRagdoll;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.Being.BodyPart;

public class Melee extends Weapon {
	private static final long serialVersionUID = 1L;
	private float width, height, density;
	private transient Body body;
	private transient Being owner;
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getDensity() {
		return density;
	}
	
	public void setDensity(float density) {
		this.density = density;
	}
	
	public Being getOwner() {
		return owner;
	}

	public Body getBody() {
		return body;
	}
	
	@Override public void activate(World world, IRagdoll ragdoll, Being owner) {
		this.owner = owner;
		Vector2 position = ragdoll.getWeaponCenter();
		body = PhysicsHelper.createRectangle(world, width, height, position, BodyType.DynamicBody, 
				density, owner.getMask(), owner.getCat(), (short)0);
		body.setUserData(this);
		WeldJointDef def = new WeldJointDef();
		def.initialize(body, ragdoll.getBodyPart(BodyPart.lArm), position);
		world.createJoint(def);
	}
	
	@Override public void deactivate(World world) {
		if(body != null)
			world.destroyBody(body);
		else
			Gdx.app.error("Melee.deactivate", "Want to destroy weapon body, but body null! " + this);
	}
	
	public static double impulseToDamageScalar(float impulse, float dt){
		//TODO: use dt here, as it matters greatly. Probably divide impulse 
		//by it as long dts produce huge impulse, potentially
		return -.1 + .4 * Math.log(600 * impulse);
	}

	@Override public String toString(){
		return "[Melee name:" + name + " dmg: " + getDamage() + "]";
	}

	@Override public void handleContact(WorldManager worldManager, Being target, 
			Fixture hit, Contact contact, ContactImpulse oldManifold) {
		float impulse = 0;
		for(float normalImpulse : oldManifold.getNormalImpulses())
			impulse += normalImpulse;
		if(impulse > Properties.getFloat("melee.contact.impulse.threshold", .02f)){
			Gdx.app.log("ContactListener.postSolve","HandleMelee - scale damage with impulse impulse: " + impulse);
			worldManager.processHit(getDamage() * (float)Melee.impulseToDamageScalar(impulse, Gdx.graphics.getRawDeltaTime()), 
					target, owner, hit, contact.getWorldManifold().getNormal());
		}
	}
}
