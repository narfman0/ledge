package com.blastedstudios.ledge.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public class PhysicsEnvironment {
	public static final short 
		CAT_SCENERY		= 0x1,
		CAT_NOTHING		= 0x2,
		CAT_PROJECTILE	= 0x4,
		CAT_FRIEND		= 0x8,
		CAT_ENEMY		= 0x10,
		MASK_FRIEND		= (short)-1 & ~CAT_FRIEND,
		MASK_ENEMY		= (short)-1 & ~CAT_ENEMY,
		MASK_PROJECTILE	= (short)-1 & ~CAT_PROJECTILE,
		MASK_NOTHING	= CAT_SCENERY,
		MASK_SCENERY	= -1;
	private static final float BULLET_DENSITY = Properties.getFloat("gun.bullet.density", 1f),
			BULLET_RADIUS = Properties.getFloat("gun.bullet.radius", 1f);

	public static Body createBullet(World world, GunShot gunshot, Vector2 origin){
		Vector2 position = origin.cpy().add(gunshot.getDir()),
				impulse = gunshot.getDir().cpy().scl(gunshot.getGun().getMuzzleVelocity());
		Body body = com.blastedstudios.gdxworld.physics.PhysicsHelper.createCircle(world, 
				BULLET_RADIUS, position, BodyType.DynamicBody, BULLET_DENSITY, MASK_PROJECTILE, CAT_PROJECTILE, (short)0);
		float angle = (float)Math.atan2(gunshot.getDir().y, gunshot.getDir().x);
		body.setTransform(position, angle);
		body.applyLinearImpulse(impulse,position,true);
		body.setBullet(true);
		body.setUserData(gunshot);
		return body;
	}
	
	public static Joint addRevolute(World world, Body bodyA, Body bodyB, float lowerAngle,
			float upperAngle, Vector2 anchor){
		RevoluteJointDef joint = new RevoluteJointDef();
		if(lowerAngle != 0f || upperAngle != 0f)
			joint.enableLimit = true;
		joint.lowerAngle = lowerAngle;
		joint.upperAngle = upperAngle;
		joint.motorSpeed = 1;
		joint.maxMotorTorque = 1;
		joint.initialize(bodyA, bodyB, anchor);
		return world.createJoint(joint);
	}
	
	public static Joint addWeld(World world, Body bodyA, Body bodyB, Vector2 anchor){
		WeldJointDef joint = new WeldJointDef();
		joint.initialize(bodyA, bodyB, anchor);
		return world.createJoint(joint);
	}
}
