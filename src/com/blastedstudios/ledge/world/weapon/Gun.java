package com.blastedstudios.ledge.world.weapon;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public class Gun extends Weapon {
	private static final long serialVersionUID = 1L;
	private int currentRounds;
	private AmmoTypeEnum ammoType;
	
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
	
	@Override public String toString(){
		return "[Gun name:" + name + " dmg: " + getDamage() + " currentRounds:" + currentRounds + "]";
	}
	
	@Override public String toStringPretty(Being owner) {
		int rightHandSide = Being.INFINITE_AMMO ? getRoundsPerClip() : owner.getAmmo().get(ammoType);
		return name + ": " + currentRounds + "/" + rightHandSide;
	}
}
