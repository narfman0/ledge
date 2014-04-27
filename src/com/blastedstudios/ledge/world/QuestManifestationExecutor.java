package com.blastedstudios.ledge.world;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
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
import com.blastedstudios.ledge.world.weapon.WeaponFactory;

public class QuestManifestationExecutor implements IQuestManifestationExecutor{
	private final HashMap<String, Long> soundIdMap = new HashMap<>();
	private final GameplayScreen screen;
	private final WorldManager worldManager;
	
	public QuestManifestationExecutor(GameplayScreen screen, WorldManager worldManager){
		this.screen = screen;
		this.worldManager = worldManager;
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
			int suffix = 0;
			String name = being;
			while(worldManager.getAllBeings().containsKey(name + suffix))
				suffix++;
			NPCData data = NPCData.parse(being);
			data.set("Path", path);
			worldManager.spawnNPC(being + suffix, coordinates, data);
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
		Being being = worldManager.getAllBeings().get(beingName);
		being.setFaction(faction);
		being.respawn(worldManager.getWorld(), being.getPosition().x, being.getPosition().y);
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum pathChange(String beingString, String pathString) {
		GDXPath path = worldManager.getPath(pathString);
		Being being = worldManager.getAllBeings().get(beingString);
		if(path == null)
			Gdx.app.error("QuestManifestationExecutor.pathChange", "Path null " +
					"for quest manifestation! path:" + pathString + " being:" + beingString);
		if(being == null)
			Gdx.app.error("QuestManifestationExecutor.pathChange", "Being null " +
					"for quest manifestation! path:" + pathString + " being:" + beingString);
		((NPC)being).setPath(path);
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
		final Being being = getBeing(beingString);
		being.setFixedRotation(fixedRotation);
		being.getRagdoll().applyTorque(torque);
		return CompletionEnum.COMPLETED;
	}
	
	public CompletionEnum weaponSpawn(String weapon, Vector2 location) {
		worldManager.getDropManager().dropGun(worldManager.getWorld(), WeaponFactory.getWeapon(weapon), location);;
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum weaponAdd(String weapon, String target) {
		Being recipient = target.equals("player") ? worldManager.getPlayer() : worldManager.getAllBeings().get(target);
		recipient.getGuns().add(0, WeaponFactory.getWeapon(weapon));
		recipient.setCurrentWeapon(0, worldManager.getWorld());
		if(recipient.getGuns().size() > 3)
			recipient.getInventory().add(recipient.getGuns().remove(3));
		return CompletionEnum.COMPLETED;
	}

	public CompletionEnum addXP(String being, long xp) {
		getBeing(being).addXp(xp);
		return CompletionEnum.COMPLETED;
	}
	
	/**
	 * @return being corresponding to name, returns the player if string is "player"
	 */
	private Being getBeing(String beingName){
		return worldManager.getAllBeings().get(beingName);
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
}
