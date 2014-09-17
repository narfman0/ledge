package com.blastedstudios.ledge.world;

import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.beingspawn.IBeingSpawnHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.dialog.IDialogHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.endlevel.IEndLevelHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.inputenable.IInputEnableHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.particle.IParticleHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.pause.IPauseHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.ISoundHandler;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.IQuestManifestationExecutor;
import com.blastedstudios.ledge.plugin.quest.handler.BeingSpawnHandlerPlugin;
import com.blastedstudios.ledge.plugin.quest.handler.DialogHandlerPlugin;
import com.blastedstudios.ledge.plugin.quest.handler.EndLevelHandlerPlugin;
import com.blastedstudios.ledge.plugin.quest.handler.InputEnableHandlerPlugin;
import com.blastedstudios.ledge.plugin.quest.handler.ParticleHandlerPlugin;
import com.blastedstudios.ledge.plugin.quest.handler.PauseHandlerPlugin;
import com.blastedstudios.ledge.plugin.quest.handler.SoundHandlerPlugin;
import com.blastedstudios.ledge.plugin.quest.manifestation.cameratween.CameraAccessor;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.FactionEnum;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Turret;
import com.blastedstudios.ledge.world.weapon.WeaponFactory;

public class QuestManifestationExecutor implements IQuestManifestationExecutor{
	private final GameplayScreen screen;
	private final WorldManager worldManager;
	private final Random random;
	
	public QuestManifestationExecutor(GameplayScreen screen, WorldManager worldManager){
		this.screen = screen;
		this.worldManager = worldManager;
		random = new Random();
		for(IDialogHandler handler : PluginUtil.getPlugins(IDialogHandler.class))
			((DialogHandlerPlugin)handler).setDialogManager(screen.getDialogManager());
		for(IEndLevelHandler handler : PluginUtil.getPlugins(IEndLevelHandler.class))
			((EndLevelHandlerPlugin)handler).setGameplayScreen(screen);
		for(IBeingSpawnHandler handler : PluginUtil.getPlugins(IBeingSpawnHandler.class))
			((BeingSpawnHandlerPlugin)handler).setWorldManager(worldManager);
		for(IParticleHandler handler : PluginUtil.getPlugins(IParticleHandler.class))
			((ParticleHandlerPlugin)handler).setGameplayScreen(screen);
		for(IInputEnableHandler handler : PluginUtil.getPlugins(IInputEnableHandler.class))
			((InputEnableHandlerPlugin)handler).setWorldManager(worldManager);
		for(IPauseHandler handler : PluginUtil.getPlugins(IPauseHandler.class))
			((PauseHandlerPlugin)handler).setWorldManager(worldManager);
		for(ISoundHandler handler : PluginUtil.getPlugins(ISoundHandler.class))
			((SoundHandlerPlugin)handler).setWorldManager(worldManager);
	}

	@Override public Joint getPhysicsJoint(String name) {
		Array<Joint> joints = new Array<>(worldManager.getWorld().getJointCount());
		worldManager.getWorld().getJoints(joints);
		for(Joint joint : joints)
			if(joint.getUserData() != null && joint.getUserData().equals(name))
				return joint;
		return null;
	}

	@Override public Body getPhysicsObject(String name) {
		Array<Body> bodyArray = new Array<>(worldManager.getWorld().getBodyCount());
		worldManager.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray)
			if(name != null && name.equals(body.getUserData()))
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
	
	public CompletionEnum cameraTween(int tweenType, float duration, Vector2 target, TweenEquation ease){
		if(screen.getCamera() == null)
			return CompletionEnum.EXECUTING;
		Tween.registerAccessor(screen.getCamera().getClass(), new CameraAccessor());
		Tween.to(screen.getCamera(), tweenType, duration).target(target.x, target.y).
			ease(ease).start(worldManager.getTweenManager());
		return CompletionEnum.COMPLETED;
	}
}
