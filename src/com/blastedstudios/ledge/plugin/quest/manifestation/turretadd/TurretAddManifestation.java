package com.blastedstudios.ledge.plugin.quest.manifestation.turretadd;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;
import com.blastedstudios.ledge.world.QuestManifestationExecutor;

public class TurretAddManifestation extends AbstractQuestManifestation {
	private static final long serialVersionUID = 1L;
	public static final TurretAddManifestation DEFAULT = new TurretAddManifestation();
	private Vector2 location = new Vector2();
	private float direction, directionLow = 0, directionHigh = (float)Math.PI*2f;
	private String weapon = "ak47";

	public TurretAddManifestation(){}
	public TurretAddManifestation(Vector2 location, String weapon, 
			float direction, float directionLow, float directionHigh){
		this.location = location;
		this.weapon = weapon;
		this.direction = direction;
		this.directionLow = directionLow;
		this.directionHigh = directionHigh;
	}
	
	@Override public CompletionEnum execute() {
		return ((QuestManifestationExecutor)executor).turretAdd(location, weapon, direction, directionLow, directionHigh);
	}

	@Override public AbstractQuestManifestation clone() {
		return new TurretAddManifestation(location.cpy(), weapon, direction, directionLow, directionHigh);
	}

	@Override public String toString() {
		return "[TurretAddManifestation location:" + location + " weapon: " + weapon + "]";
	}

	public Vector2 getLocation() {
		return location;
	}

	public void setLocation(Vector2 location) {
		this.location = location;
	}
	
	public float getDirection() {
		return direction;
	}
	
	public void setDirection(float direction) {
		this.direction = direction;
	}
	
	public String getWeapon() {
		return weapon;
	}
	
	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	
	public float getDirectionLow() {
		return directionLow;
	}
	
	public void setDirectionLow(float directionLow) {
		this.directionLow = directionLow;
	}
	
	public float getDirectionHigh() {
		return directionHigh;
	}
	
	public void setDirectionHigh(float directionHigh) {
		this.directionHigh = directionHigh;
	}
}
