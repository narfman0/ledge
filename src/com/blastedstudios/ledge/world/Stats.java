package com.blastedstudios.ledge.world;

import java.io.Serializable;

import com.blastedstudios.ledge.world.being.NPCData;

public class Stats implements Serializable {
	private static final long serialVersionUID = 1L;
	private float hp, attack, defense, hpPerLevel, attackPerLevel, defensePerLevel,
		damage, accuracy, rateOfFire, reloadSpeed, muzzleVelocity, recoil, 
		jetpackRecharge, jetpackMax, jetpackImpulse;
	private int projectileCount, roundsPerClip;

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public float getRateOfFire() {
		return rateOfFire;
	}

	public void setRateOfFire(float rateOfFire) {
		this.rateOfFire = rateOfFire;
	}

	public float getReloadSpeed() {
		return reloadSpeed;
	}

	public void setReloadSpeed(float reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
	}

	public float getMuzzleVelocity() {
		return muzzleVelocity;
	}

	public void setMuzzleVelocity(float muzzleVelocity) {
		this.muzzleVelocity = muzzleVelocity;
	}

	public float getRecoil() {
		return recoil;
	}

	public void setRecoil(float recoil) {
		this.recoil = recoil;
	}

	public int getProjectileCount() {
		return projectileCount;
	}

	public void setProjectileCount(int projectileCount) {
		this.projectileCount = projectileCount;
	}

	public int getRoundsPerClip() {
		return roundsPerClip;
	}

	public void setRoundsPerClip(int roundsPerClip) {
		this.roundsPerClip = roundsPerClip;
	}

	public float getAttack() {
		return attack;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public float getDefense() {
		return defense;
	}

	public void setDefense(float defense) {
		this.defense = defense;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getHpPerLevel() {
		return hpPerLevel;
	}

	public void setHpPerLevel(float hpPerLevel) {
		this.hpPerLevel = hpPerLevel;
	}

	public float getAttackPerLevel() {
		return attackPerLevel;
	}

	public void setAttackPerLevel(float attackPerLevel) {
		this.attackPerLevel = attackPerLevel;
	}

	public float getDefensePerLevel() {
		return defensePerLevel;
	}

	public void setDefensePerLevel(float defensePerLevel) {
		this.defensePerLevel = defensePerLevel;
	}

	public float getJetpackRecharge() {
		return jetpackRecharge;
	}

	public void setJetpackRecharge(float jetpackRecharge) {
		this.jetpackRecharge = jetpackRecharge;
	}
	
	public boolean hasJetpack(){
		return jetpackMax != 0f;
	}

	public float getJetpackMax() {
		return jetpackMax;
	}

	public void setJetpackMax(float jetpackMax) {
		this.jetpackMax = jetpackMax;
	}

	public float getJetpackImpulse() {
		return jetpackImpulse;
	}

	public void setJetpackImpulse(float jetpackImpulse) {
		this.jetpackImpulse = jetpackImpulse;
	}
	
	public static Stats parseNPCData(NPCData npcData){
		Stats stats = new Stats();
		stats.hp = npcData.getFloat("HP");
		stats.attack = npcData.getFloat("Attack");
		stats.defense = npcData.getFloat("Defense");
		stats.hpPerLevel = npcData.getFloat("HPPerLevel");
		stats.attackPerLevel = npcData.getFloat("AttackPerLevel");
		stats.defensePerLevel = npcData.getFloat("DefensePerLevel");
		stats.jetpackRecharge = npcData.getFloat("JetpackRecharge");
		stats.jetpackMax = npcData.getFloat("JetpackMax");
		stats.jetpackImpulse = npcData.getFloat("JetpackImpulse");
		return stats;
	}
}
