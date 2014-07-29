package com.blastedstudios.ledge.physics.ragdoll;

import net.xeoh.plugins.base.Plugin;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.Being.BodyPart;
import com.blastedstudios.ledge.world.weapon.DamageStruct;

public interface IRagdoll{
	public static interface IRagdollPlugin extends Plugin{
		public boolean canCreate(String resource);
		public IRagdoll create(World world, float x, float y, Being being, TextureAtlas atlas, FileHandle file);
	}
	
	public void setFriction(float friction);
	public boolean standingOn(Contact contact);
	public boolean aim(float heading);
	public void death(World world, DamageStruct shotDamage);
	public Vector2 getPosition();
	public Vector2 getLinearVelocity();
	public void setLinearVelocity(float x, float y);
	public void applyLinearImpulse(float i, float j, float x, float y);
	public void applyForceAtCenter(float x, float y);
	public void setTransform(float x, float y, float angle);
	public boolean isOwned(Fixture fixture);
	public boolean isFoot(Fixture fixture);
	public void dispose(World world);
	public BodyPart getBodyPart(Fixture fixture);
	public Body getBodyPart(BodyPart part);
	public void render(SpriteBatch spriteBatch, boolean dead, boolean isGrounded, boolean isMoving, float velX);
	public boolean isFixedRotation();
	public void setFixedRotation(boolean fixedRotation);
	public void applyTorque(float torque);
	public boolean isFacingLeft();
	/**
	 * @return position of the hand according to which direction the ragdoll is facing.
	 * If facing left, will return the right hand (as it is "behind" and further out)
	 */
	public Vector2 getHandLocationFacing();
}