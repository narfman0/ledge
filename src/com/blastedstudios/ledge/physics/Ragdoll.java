package com.blastedstudios.ledge.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.physics.PhysicsHelper;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.Being.BodyPart;
import com.blastedstudios.ledge.world.weapon.DamageStruct;

public class Ragdoll implements IRagdoll {
	private static float DENSITY = Properties.getFloat("character.ragdoll.density", 6f),
			DEATH_IMPULSE = Properties.getFloat("character.death.impulse", .3f),
			DEATH_TORQUE = Properties.getFloat("character.death.torque", 50),
			SCALE = Properties.getFloat("character.ragdoll.scale", 1f);
	private static final BodyPart[] RIGHT_FACING_ORDER = new BodyPart[]{
		BodyPart.rLeg, BodyPart.rArm, BodyPart.torso, 
		BodyPart.head, BodyPart.lLeg, BodyPart.lArm}, 
		LEFT_FACING_ORDER = new BodyPart[]{
		BodyPart.lLeg, BodyPart.lArm, BodyPart.torso, 
		BodyPart.head, BodyPart.rLeg, BodyPart.rArm};
	public final Body torsoBody, headBody, rLegBody, lLegBody, rArmBody, lArmBody, lHandBody;
	public final Fixture torsoFixture, headFixture, rLegFixture, lLegFixture, 
		rArmFixture, lArmFixture, lHandFixture;
	private Joint headJoint, rLegJoint, lLegJoint, rArmJoint, lArmJoint, lHandJoint;
	private final List<Body> bodies;
	private final float torsoY = .27f, legX = .1f, legY = -.2f, armX = -.02f, armY = .3f, headY = .6f;
	private final Map<Body,Sprite> sprites = new HashMap<Body, Sprite>();
	private boolean facingLeft, fixedRotation = true;
	private float targetHeading;

	public Ragdoll(World world, float x, float y, Being being, TextureAtlas atlas){
		bodies = new ArrayList<Body>();
		short mask = being.getMask();
		short cat = being.getCat();
		{
			torsoBody = PhysicsHelper.createRectangle(world, .15f*SCALE, .3f*SCALE, 
					new Vector2(0,torsoY).scl(SCALE), BodyType.DynamicBody, DENSITY, 
					mask, cat, (short)0);
			torsoFixture = torsoBody.getFixtureList().get(0);
		}{
			lLegBody = PhysicsHelper.createRectangle(world, .1f*SCALE, .25f*SCALE, 
					new Vector2(-legX, legY).scl(SCALE), BodyType.DynamicBody, DENSITY, 
					mask, cat, (short)0);
			lLegBody.setTransform(lLegBody.getPosition(), -.2f*SCALE);
			lLegFixture = lLegBody.getFixtureList().get(0);
			lLegJoint = PhysicsEnvironment.addWeld(world, lLegBody, torsoBody, new Vector2(-.1f,-.05f).scl(SCALE));
		}{
			rLegBody = PhysicsHelper.createRectangle(world, .1f*SCALE, .25f*SCALE, 
					new Vector2(legX, legY).scl(SCALE), BodyType.DynamicBody, DENSITY, 
					mask, cat, (short)0);
			rLegBody.setTransform(rLegBody.getPosition(), .2f*SCALE);
			rLegFixture = rLegBody.getFixtureList().get(0);
			rLegJoint = PhysicsEnvironment.addWeld(world, rLegBody, torsoBody, new Vector2(.1f,-.05f).scl(SCALE));
		}{
			PolygonShape lArm = new PolygonShape();
			lArm.setAsBox(.1f*SCALE, .22f*SCALE, new Vector2(-armX, armY), -1.57f*SCALE);
			lArmBody = world.createBody(getDynamicBody());
			lArmFixture = lArmBody.createFixture(lArm, DENSITY);
			Filter filter = lArmFixture.getFilterData();
			filter.maskBits = PhysicsEnvironment.MASK_NOTHING;
			filter.categoryBits = PhysicsEnvironment.CAT_NOTHING;
			lArmFixture.setFilterData(filter);
			lArm.dispose();
			lArmJoint = PhysicsEnvironment.addRevolute(world, lArmBody, torsoBody, 0,0,new Vector2(-.17f,.3f).scl(SCALE));
		}{
			CircleShape lHand = new CircleShape();
			lHand.setPosition(new Vector2(-armX+.14f, armY));
			lHand.setRadius(.1f);
			lHandBody = world.createBody(getDynamicBody());
			lHandFixture = lHandBody.createFixture(lHand, DENSITY);
			Filter filter = lHandFixture.getFilterData();
			filter.maskBits = PhysicsEnvironment.MASK_NOTHING;
			filter.categoryBits = PhysicsEnvironment.CAT_NOTHING;
			lHandFixture.setFilterData(filter);
			lHand.dispose();
			lHandJoint = PhysicsEnvironment.addWeld(world, lHandBody, lArmBody, new Vector2(-armX,armY-.11f).scl(SCALE));
		}{
			PolygonShape rArm = new PolygonShape();
			rArm.setAsBox(.1f*SCALE, .22f*SCALE, new Vector2(armX, armY).scl(SCALE), 1.57f*SCALE);
			rArmBody = world.createBody(getDynamicBody());
			rArmFixture = rArmBody.createFixture(rArm, DENSITY);
			Filter filter = rArmFixture.getFilterData();
			filter.maskBits = PhysicsEnvironment.MASK_NOTHING;
			filter.categoryBits = PhysicsEnvironment.CAT_NOTHING;
			rArmFixture.setFilterData(filter);
			rArm.dispose();
			rArmJoint = PhysicsEnvironment.addRevolute(world, rArmBody, torsoBody, 0,0,new Vector2(.17f,.3f).scl(SCALE));
		}{
			CircleShape head = new CircleShape();
			head.setRadius(.2f*SCALE);
			head.setPosition(new Vector2(0, headY).scl(SCALE));
			headBody = world.createBody(getDynamicBody());
			headFixture = headBody.createFixture(head, DENSITY);
			Filter filter = headFixture.getFilterData();
			filter.maskBits = mask;
			filter.categoryBits = cat;
			headFixture.setFilterData(filter);
			head.dispose();
			headJoint = PhysicsEnvironment.addWeld(world, headBody, torsoBody, new Vector2(0,.45f).scl(SCALE));
		}
		torsoBody.setFixedRotation(fixedRotation);
		bodies.add(torsoBody);
		bodies.add(headBody);
		bodies.add(lLegBody);
		bodies.add(rLegBody);
		bodies.add(lArmBody);
		bodies.add(rArmBody);
		bodies.add(lHandBody);
		for(Body body : bodies){
			body.setTransform(new Vector2(x,y), 0);
			body.setBullet(true);
			body.setUserData(being);
		}
		
		torsoFixture.setUserData(being);
		headFixture.setUserData(being);
		rArmFixture.setUserData(being);
		lArmFixture.setUserData(being);
		rLegFixture.setUserData(being);
		lLegFixture.setUserData(being);
		lHandFixture.setUserData(being);

		sprites.put(torsoBody, atlas.createSprite("torso"));
		sprites.put(rLegBody, atlas.createSprite("rleg"));
		sprites.put(lLegBody, atlas.createSprite("lleg"));
		sprites.put(headBody, atlas.createSprite("head"));
		sprites.put(rArmBody, atlas.createSprite("rarm"));
		sprites.put(lArmBody, atlas.createSprite("larm"));
		float scale = Properties.getFloat("ragdoll.custom.scale", .005f);
		for(Sprite sprite : sprites.values())
			sprite.setScale(scale);
	}

	private BodyDef getDynamicBody(){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		return def;
	}

	@Override public void setFriction(float friction){
		torsoFixture.setFriction(friction);
		headFixture.setFriction(friction);
		rArmFixture.setFriction(friction);
		lArmFixture.setFriction(friction);
		rLegFixture.setFriction(friction);
		lLegFixture.setFriction(friction);
		lHandFixture.setFriction(friction);
	}

	@Override public boolean standingOn(Contact contact){
		return contact.getFixtureA() == rLegFixture ||
				contact.getFixtureB() == rLegFixture ||
				contact.getFixtureA() == lLegFixture ||
				contact.getFixtureB() == lLegFixture;
	}

	@Override public boolean aim(float heading) {
		targetHeading = heading;
		return facingLeft = Math.cos(heading) < 0;
	}

	@Override public void death(World world, DamageStruct damage) {
		setFriction(10);
		torsoBody.setFixedRotation(false);
		torsoBody.setBullet(false);
		Vector2 dir = damage.getDir().cpy();
		switch(damage.getBodyPart()){
		case head:
			headBody.applyLinearImpulse(dir.scl(DEATH_IMPULSE), headBody.getPosition(),true);
			world.destroyJoint(headJoint);
			headJoint = null;
			break;
		case lArm:
			lArmBody.applyLinearImpulse(dir.scl(DEATH_IMPULSE), lArmBody.getPosition(),true);
			world.destroyJoint(lArmJoint);
			lArmJoint = null;
			break;
		case rArm:
			rArmBody.applyLinearImpulse(dir.scl(DEATH_IMPULSE), rArmBody.getPosition(),true);
			world.destroyJoint(rArmJoint);
			rArmJoint = null;
			break;
		case lLeg:
			lLegBody.applyLinearImpulse(dir.scl(DEATH_IMPULSE), lLegBody.getPosition(),true);
			world.destroyJoint(lLegJoint);
			lLegJoint = null;
			break;
		case rLeg:
			rLegBody.applyLinearImpulse(dir.scl(DEATH_IMPULSE), rLegBody.getPosition(),true);
			world.destroyJoint(rLegJoint);
			rLegJoint = null;
			break;
		default:
			torsoBody.applyLinearImpulse(dir.scl(DEATH_IMPULSE), torsoBody.getPosition(),true);
			break;
		}
		torsoBody.applyTorque((dir.x > 0 ? -1 : 1) * DEATH_TORQUE, true);
	}

	@Override public Vector2 getPosition(){
		return torsoBody.getPosition();
	}

	@Override public Vector2 getLinearVelocity(){
		return torsoBody.getLinearVelocity();
	}

	@Override public void setLinearVelocity(float x, float y) {
		torsoBody.setLinearVelocity(x, y);
	}

	@Override public void applyLinearImpulse(float i, float j, float x, float y) {
		i /= bodies.size();
		j /= bodies.size();
		for(Body body : bodies)
			if(body != rArmBody && body != lArmBody && body != lHandBody)
				body.applyLinearImpulse(i,j,x,y,true);
	}

	@Override public void applyForceAtCenter(float x, float y) {
		x /= bodies.size();
		y /= bodies.size();
		for(Body body : bodies)
			if(body != rArmBody && body != lArmBody && body != lHandBody)
				body.applyForceToCenter(x,y,true);
	}

	@Override public void setTransform(float x, float y, float angle) {
		for(Body body : bodies)
			body.setTransform(x, y, angle);
	}

	@Override public boolean isOwned(Fixture fixture){
		return fixture == torsoFixture || fixture == headFixture || fixture == rLegFixture ||
				fixture == lLegFixture || fixture == rArmFixture || fixture == lArmFixture ||
				fixture == lHandFixture;
	}

	@Override public void dispose(World world) {
		disposeJoints(world);
		disposeBodies(world);
	}

	private void disposeBodies(World world) {
		for(Body body : bodies)
			world.destroyBody(body);
	}

	private void disposeJoints(World world) {
		if(headJoint != null){
			world.destroyJoint(headJoint);
			headJoint = null;
		}
		if(lArmJoint != null){
			world.destroyJoint(lArmJoint);
			lArmJoint = null;
		}
		if(rArmJoint != null){
			world.destroyJoint(rArmJoint);
			rArmJoint = null;
		}
		if(lHandJoint != null){
			world.destroyJoint(lHandJoint);
			lHandJoint = null;
		}
		if(lLegJoint != null){
			world.destroyJoint(lLegJoint);
			lLegJoint = null;
		}
		if(rLegJoint != null){
			world.destroyJoint(rLegJoint);
			rLegJoint = null;
		}
	}

	@Override public BodyPart getBodyPart(Fixture fixture) {
		if(headFixture == fixture)
			return BodyPart.head;
		if(torsoFixture == fixture)
			return BodyPart.torso;
		if(lArmFixture == fixture || fixture == lHandFixture)
			return BodyPart.lArm;
		if(rArmFixture == fixture)
			return BodyPart.rArm;
		if(lLegFixture == fixture)
			return BodyPart.lLeg;
		if(rLegFixture == fixture)
			return BodyPart.rLeg;
		return null;
	}

	@Override public boolean isFoot(Fixture fixture) {
		return fixture == rLegFixture || fixture == lLegFixture;
	}

	@Override public Body getBodyPart(BodyPart part) {
		switch(part){
		case head:
			return headBody;
		case lArm:
			return lArmBody;
		case lLeg:
			return lLegBody;
		case rArm:
			return rArmBody;
		case rLeg:
			return rLegBody;
		case torso:
			return torsoBody;
		default:
			break;
		}
		return null;
	}
	
	private static float getAngularImpulse(Body body, float heading){
		float angle = body.getAngle() + body.getAngularVelocity() * Gdx.graphics.getRawDeltaTime();
		float rotation = heading - angle;
		while ( rotation < -Math.PI ) 
			rotation += Math.PI*2f;
		while ( rotation >  Math.PI ) 
			rotation -= Math.PI*2f;
		float desiredAngularVelocity = rotation / Gdx.graphics.getRawDeltaTime();
		return body.getInertia() * desiredAngularVelocity;
	}
	
	private static void rotateLeg(Sprite sprite, boolean isRightLeg, float velX){
		float rotation = (float)Math.sin( ((double)System.currentTimeMillis())/100.0 ) * 45f * 
				(isRightLeg ? -1f : 1f) * Math.min(1f, Math.max(-1f, velX/5f)); 
		sprite.rotate(rotation);
		sprite.translateX(rotation/300f);
	}
	
	@Override public void render(SpriteBatch spriteBatch, boolean dead, boolean isGrounded, boolean isMoving, float velX) {
		if(!dead){
			lArmBody.applyTorque(getAngularImpulse(lArmBody, targetHeading)*5f, true);
			rArmBody.applyTorque(getAngularImpulse(rArmBody, targetHeading+(float)Math.PI)*5f, true);
		}
		for(BodyPart part : facingLeft ? LEFT_FACING_ORDER : RIGHT_FACING_ORDER){
			Body body = getBodyPart(part);
			Sprite sprite = sprites.get(body);
			applyBodyTransform(sprite, body);
			if(part == BodyPart.head || part == BodyPart.torso)
				sprite.flip(facingLeft, false);
			if(isGrounded && isMoving && (part == BodyPart.rLeg || part == BodyPart.lLeg))
				rotateLeg(sprite, part == BodyPart.rLeg, velX);
			sprite.draw(spriteBatch);
			if(part == BodyPart.head || part == BodyPart.torso)
				sprite.flip(facingLeft, false);
		}
		if(isFixedRotation() && Math.abs(torsoBody.getAngle()) > .05f){
			recoverFromFreeRotation(torsoBody);
			if(Math.abs(lLegBody.getAngle()) > .05f)
				recoverFromFreeRotation(lLegBody);
			if(Math.abs(rLegBody.getAngle()) > .05f)
				recoverFromFreeRotation(rLegBody);
		}
	}
	
	private static void recoverFromFreeRotation(Body body){
		body.setTransform(body.getPosition().x, body.getPosition().y, 
				(body.getAngle() % (float)((body.getAngle() < 0 ? 2.0 : -2.0)*Math.PI))*.83f);
	}
	
	private static void applyBodyTransform(Sprite sprite, Body body){
		sprite.setPosition(body.getWorldCenter().x - sprite.getWidth()/2f, 
				body.getWorldCenter().y - sprite.getHeight()/2);
		sprite.setRotation((float)Math.toDegrees(body.getAngle()));
	}

	@Override public boolean isFixedRotation() {
		return torsoBody.isFixedRotation();
	}

	@Override public void setFixedRotation(boolean fixedRotation) {
		torsoBody.setFixedRotation(fixedRotation);
	}

	@Override public void applyTorque(float torque) {
		torsoBody.applyTorque(torque, true);
	}

	@Override public Vector2 getWeaponCenter() {
		return lHandBody.getWorldCenter();
	}
	
	@Override public boolean isFacingLeft(){
		return facingLeft;
	}
}
