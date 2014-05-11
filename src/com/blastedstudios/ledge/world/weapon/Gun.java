package com.blastedstudios.ledge.world.weapon;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.ledge.physics.PhysicsEnvironment;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public class Gun extends Weapon {
	private static final long serialVersionUID = 1L;
	private int currentRounds;
	private AmmoTypeEnum ammoType;
	private long lastFireTime;
	
	public GunShot createGunShot(Being origin, Vector2 dir){
		return new GunShot(origin, dir, this);
	}

	public int getCurrentRounds() {
		return currentRounds;
	}

	public int setCurrentRounds(int currentRounds) {
		return this.currentRounds = currentRounds;
	}
	
	public int addCurrentRounds(int currentRounds) {
		return this.currentRounds += currentRounds;
	}
	
	public AmmoTypeEnum getAmmoType() {
		return ammoType;
	}
	
	public void setAmmoType(AmmoTypeEnum ammoType) {
		this.ammoType = ammoType;
	}
	
	@Override public long getMSSinceAttack(){
		return System.currentTimeMillis() - lastFireTime;
	}
	
	@Override public String toString(){
		return "[Gun name:" + name + " dmg: " + getDamage() + " currentRounds:" + currentRounds + "]";
	}
	
	@Override public String toStringPretty(Being owner) {
		int rightHandSide = Being.INFINITE_AMMO ? getRoundsPerClip() : owner.getAmmo().get(ammoType);
		return name + ": " + currentRounds + "/" + rightHandSide;
	}
	
	public void shoot(Being source, Random random, Vector2 direction, WorldManager world, Vector2 position){
		currentRounds -= 1;
		lastFireTime = System.currentTimeMillis();
		for(int i=0; i<getProjectileCount(); i++){
			float degrees = (float) (random.nextGaussian() * 20 * (1f-getAccuracy()));
			Vector2 dir = direction.cpy().rotate(degrees);
			GunShot gunshot = createGunShot(source, dir);
			Body body = PhysicsEnvironment.createBullet(world.getWorld(), gunshot, position);
			world.getGunshots().put(body, gunshot);
		}
	}
	
	@Override public boolean canAttack(){
		return getMSSinceAttack()/1000f > 1f/getRateOfFire() && getCurrentRounds() > 0;
	}
}
