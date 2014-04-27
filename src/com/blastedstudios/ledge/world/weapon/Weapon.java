package com.blastedstudios.ledge.world.weapon;

import java.io.Serializable;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.blastedstudios.ledge.physics.IRagdoll;
import com.blastedstudios.ledge.world.Stats;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public abstract class Weapon implements Serializable{
	private static final long serialVersionUID = 1L;
	protected WeaponType type = WeaponType.MELEE;
	protected int cost;
	protected Stats stats;
	protected String name, resource, fireSound;
	
	public WeaponType getType() {
		return type;
	}
	
	public void setType(WeaponType type) {
		this.type = type;
	}

	public float getDamage() {
		return stats.getDamage();
	}

	public void setDamage(float damage) {
		stats.setDamage(damage);
	}

	public float getAccuracy() {
		return stats.getAccuracy();
	}

	public float getRateOfFire() {
		return stats.getRateOfFire();
	}

	public float getReloadSpeed() {
		return stats.getReloadSpeed();
	}

	public float getMuzzleVelocity() {
		return stats.getMuzzleVelocity();
	}

	public float getRecoil() {
		return stats.getRecoil();
	}

	public int getProjectileCount() {
		return stats.getProjectileCount();
	}

	public int getRoundsPerClip() {
		return stats.getRoundsPerClip();
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override public String toString(){
		return "[Gun name:" + name + " dmg: " + getDamage() + "]";
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public float getAttack() {
		return stats.getAttack();
	}
	
	public float getDefense() {
		return stats.getDefense();
	}
	
	public float getHp() {
		return stats.getHp();
	}
	
	public float getAttackPerLevel() {
		return stats.getAttackPerLevel();
	}
	
	public float getDefensePerLevel() {
		return stats.getDefensePerLevel();
	}
	
	public float getHpPerLevel() {
		return stats.getHpPerLevel();
	}
	
	public Stats getStats() {
		return stats;
	}
	
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	public String getFireSound() {
		return fireSound;
	}
	
	public void setFireSound(String fireSound) {
		this.fireSound = fireSound;
	}
	
	public String toStringPretty(Being owner) {
		return name;
	}
	
	public void handleContact(Body body, GunShot gunshot, WorldManager worldManager, WorldManifold manifold){
		body.setUserData(WorldManager.REMOVE_USER_DATA);
		worldManager.getGunshots().remove(body);
	}

	public void activate(World world, IRagdoll ragdoll, Being owner){}
	public void deactivate(World world){}
}
