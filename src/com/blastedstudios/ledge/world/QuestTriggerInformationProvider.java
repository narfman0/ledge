package com.blastedstudios.ledge.world;

import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.world.quest.trigger.IQuestTriggerInformationProvider;
import com.blastedstudios.ledge.plugin.quest.trigger.beinghit.IBeingHitListener;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.DamageStruct;

public class QuestTriggerInformationProvider implements IQuestTriggerInformationProvider{
	private final GameplayScreen screen;
	private final WorldManager worldManager;
	private final HashSet<IBeingHitListener> beingHitListeners = new HashSet<>();
	
	public QuestTriggerInformationProvider(GameplayScreen screen, WorldManager worldManager){
		this.screen = screen;
		this.worldManager = worldManager;
	}

	@Override public Vector2 getPlayerPosition() {
		try{
			return worldManager.getPlayer().getPosition();
		}catch(Exception e){
			return worldManager.getRespawnLocation();
		}
	}

	@Override public boolean isDead(String name) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().equalsIgnoreCase(name) && being.isDead())
				return true;
		return false;
	}

	@Override public boolean isNear(String origin, String target, float distance) {
		Body originBody = null, targetBody = null;
		Array<Body> bodyArray = new Array<>(worldManager.getWorld().getBodyCount());
		worldManager.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray){
			if(body.getUserData().equals(origin))
				originBody = body;
			if(body.getUserData().equals(target))
				targetBody = body;
		}
		return originBody.getPosition().dst(targetBody.getPosition()) < distance;
	}

	@Override public Body getPhysicsObject(String name) {
		Array<Body> bodyArray = new Array<>(worldManager.getWorld().getBodyCount());
		worldManager.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray)
			if(body.getUserData().equals(name))
				return body;
		return null;
	}

	@Override public boolean isAction() {
		return screen.isAction();
	}
	
	public void beingHit(DamageStruct damageStruct){
		for(IBeingHitListener listener : beingHitListeners)
			listener.beingHit(damageStruct);
	}

	public boolean addBeingHitListener(IBeingHitListener beingHitTrigger) {
		return beingHitListeners.add(beingHitTrigger);
	}
}
