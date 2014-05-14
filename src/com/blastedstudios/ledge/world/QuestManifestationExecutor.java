package com.blastedstudios.ledge.world;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.plugin.mode.sound.SoundManager;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.particle.ParticleManifestationTypeEnum;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.SoundManifestationEnum;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.IQuestManifestationExecutor;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.FactionEnum;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPCData;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Turret;
import com.blastedstudios.ledge.world.weapon.WeaponFactory;

public class QuestManifestationExecutor implements IQuestManifestationExecutor{
	private final HashMap<String, Long> soundIdMap = new HashMap<>();
	private final GameplayScreen screen;
	private final WorldManager worldManager;
	private final Random random;
	
	public QuestManifestationExecutor(GameplayScreen screen, WorldManager worldManager){
		this.screen = screen;
		this.worldManager = worldManager;
		random = new Random();
	}

	@Override public CompletionEnum addDialog(String dialog, String origin, String type) {
		screen.getDialogManager().add(dialog, origin);
		return CompletionEnum.EXECUTING;
	}

	@Override public void endLevel(boolean success) {
		screen.levelComplete(success);
	}

	@Override public Joint getPhysicsJoint(String name) {
		Array<Joint> joints = new Array<>(worldManager.getWorld().getJointCount());
		worldManager.getWorld().getJoints(joints);
		for(Joint joint : joints)
			if(joint.getUserData() != null && joint.getUserData().equals(name))
				return joint;
		return null;
	}

	@Override public void beingSpawn(String being, Vector2 coordinates, String path) {
		if(being.equalsIgnoreCase("player"))
			worldManager.setRespawnLocation(coordinates.cpy());
		else{
			NPCData data = NPCData.parse(being);
			data.set("Path", path);
			worldManager.spawnNPC(being, coordinates, data, worldManager.getAiWorld());
		}
	}

	@Override public Body getPhysicsObject(String name) {
		Array<Body> bodyArray = new Array<>(worldManager.getWorld().getBodyCount());
		worldManager.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray)
			if(body.getUserData().equals(name))
				return body;
		return null;
	}

	public CompletionEnum factionChange(String beingName, FactionEnum faction) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().equalsIgnoreCase(beingName) || 
					beingName.equalsIgnoreCase("player") && being == worldManager.getPlayer()){
				being.setFaction(faction);
				being.respawn(worldManager.getWorld(), being.getPosition().x, being.getPosition().y);
			}
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum pathChange(String beingString, String pathString) {
		GDXPath path = worldManager.getPath(pathString);
		
		boolean found = false;
		for(Being being : worldManager.getAllBeings())
			if(being.getName().equalsIgnoreCase(beingString)){
				((NPC)being).setPath(path);
				found = true;
			}
		
		if(path == null)
			Gdx.app.error("QuestManifestationExecutor.pathChange", "Path null " +
					"for quest manifestation! path:" + pathString + " being:" + beingString);
		if(!found)
			Gdx.app.error("QuestManifestationExecutor.pathChange", "Being null " +
					"for quest manifestation! path:" + pathString + " being:" + beingString);
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum pause(boolean pause) {
		worldManager.pause(pause);
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum inputEnable(boolean inputEnable) {
		worldManager.setInputEnable(inputEnable);
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum beingRotation(String beingString, boolean fixedRotation, float torque) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().equalsIgnoreCase(beingString) || 
					beingString.equalsIgnoreCase("player") && being == worldManager.getPlayer()){
				being.setFixedRotation(fixedRotation);
				being.getRagdoll().applyTorque(torque);
			}
		return CompletionEnum.COMPLETED;
	}
	
	public CompletionEnum weaponSpawn(String weapon, Vector2 location) {
		worldManager.getDropManager().dropGun(worldManager.getWorld(), WeaponFactory.getWeapon(weapon), location);;
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum weaponAdd(String weapon, String target) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().equalsIgnoreCase(target) ||
					target.equalsIgnoreCase("player") && being == worldManager.getPlayer()){
				if(!being.getGuns().isEmpty())
					being.getGuns().get(0).deactivate(worldManager.getWorld());
				being.getGuns().add(0, WeaponFactory.getWeapon(weapon));
				being.setCurrentWeapon(0, worldManager.getWorld());
				if(being.getGuns().size() > 3)
					being.getInventory().add(being.getGuns().remove(3));
			}
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum addXP(String beingName, long xp) {
		for(Being being : worldManager.getAllBeings())
			if(being.getName().equalsIgnoreCase(beingName) || 
					beingName.equalsIgnoreCase("player") && being == worldManager.getPlayer())
				being.addXp(xp);
		return CompletionEnum.COMPLETED;
	}

	@Override public void particle(String name, String effectFile, String imagesDir,
			int duration, Vector2 position,
			ParticleManifestationTypeEnum modificationType, String emitterName,
			String attachedBody) {
		switch(modificationType){
		case REMOVE:
			screen.getParticleManager().scheduleRemove(name);
			break;
		case MODIFY:
			screen.getParticleManager().modify(name, duration, position, emitterName, attachedBody);
			break;
		case CREATE:
			screen.getParticleManager().addParticle(new GDXParticle(name, 
					effectFile, imagesDir, duration, position, emitterName, attachedBody));
			break;
		}
	}

	@Override public void sound(SoundManifestationEnum manifestationType, String name,
			String filename, float volume, float pan, float pitch) {
		Sound sound = SoundManager.getSound(filename);
		switch(manifestationType){
		case PLAY:
			soundIdMap.put(name, sound.play(volume, pan, pitch));
			break;
		case LOOP:
			soundIdMap.put(name, sound.loop(volume, pan, pitch));
			break;
		case PAUSE:
			sound.pause(soundIdMap.get(name));
			break;
		case PITCHPAN:
			sound.setPitch(soundIdMap.get(name), pitch);
			sound.setPan(soundIdMap.get(name), pan, volume);
		case RESUME:
			sound.resume(soundIdMap.get(name));
			break;
		case STOP:
			sound.stop();
			break;
		case VOLUME:
			sound.setVolume(soundIdMap.get(name), volume);
			break;
		default:
			break;
		}
	}

	public CompletionEnum turretAdd(Vector2 location, Vector2 mountLocation, String weapon, 
			String baseResource, float direction, float directionLow, float directionHigh) {
		worldManager.turretAdd(new Turret((Gun)WeaponFactory.getWeapon(weapon), baseResource, 
				location, mountLocation, direction, directionLow, directionHigh, random));
		return CompletionEnum.COMPLETED;
	}

	@Override public World getWorld() {
		return worldManager.getWorld();
	}

	public CompletionEnum beingInvuln(String beingName, boolean invuln) {
		if(beingName.equalsIgnoreCase("player"))
			worldManager.getPlayer().setInvulnerable(invuln);
		else
			for(Being being : worldManager.getAllBeings())
				if(being.getName().equalsIgnoreCase(beingName))
					being.setInvulnerable(invuln);
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum jetpackManifestation(String beingName,
			boolean changeMax, float max, boolean changeRecharge,
			float recharge, boolean changeImpulse, float impulse) {
		Being target = null;
		if(beingName.equalsIgnoreCase("player"))
			target = worldManager.getPlayer();
		else
			for(Being being : worldManager.getAllBeings())
				if(being.getName().equalsIgnoreCase(beingName))
					target = being;
		if(target == null)
			Gdx.app.error("QuestManifestationExecutor.jetpackManifestation", "Being not found with name: " + beingName);
		else{
			if(changeImpulse && target.getStats().getJetpackImpulse() < impulse)
				target.getStats().setJetpackImpulse(impulse);
			if(changeMax && target.getStats().getJetpackMax() < max)
				target.getStats().setJetpackMax(max);
			if(changeRecharge && target.getStats().getJetpackRecharge() < recharge)
				target.getStats().setJetpackRecharge(recharge);
		}
		return CompletionEnum.COMPLETED;
	}
}
