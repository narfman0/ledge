package com.blastedstudios.ledge.world;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXLevel.CreateLevelReturnStruct;
import com.blastedstudios.gdxworld.world.GDXNPC;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.ledge.ai.AIWorld;
import com.blastedstudios.ledge.physics.ContactListener;
import com.blastedstudios.ledge.ui.drawable.SavePillarDrawable;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen.IGameplayListener;
import com.blastedstudios.ledge.util.VisibilityReturnStruct;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.Being.IDeathCallback;
import com.blastedstudios.ledge.world.being.FactionEnum;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.DifficultyEnum;
import com.blastedstudios.ledge.world.being.NPCData;
import com.blastedstudios.ledge.world.being.Player;
import com.blastedstudios.ledge.world.weapon.DamageStruct;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Melee;
import com.blastedstudios.ledge.world.weapon.Turret;
import com.blastedstudios.ledge.world.weapon.Weapon;
import com.blastedstudios.ledge.world.weapon.WeaponFactory;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public class WorldManager implements IDeathCallback{
	public static final String REMOVE_USER_DATA = "r";
	private final World world = new World(new Vector2(0, -10), true), aiWorldDebug;
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final List<NPC> npcs = new LinkedList<>();
	private final Map<Body,GunShot> gunshots = new HashMap<>();
	private final Player player;
	private final CreateLevelReturnStruct createLevelStruct;
	private Vector2 respawnLocation;
	private final DropManager dropManager;
	private final GDXLevel level;
	private final LinkedList<ParticleEffect> particles = new LinkedList<>();
	private final LinkedList<Turret> turrets = new LinkedList<>();
	private final AIWorld aiWorld;
	private final TweenManager tweenManager;
	private final AssetManager sharedAssets;
	private final IGameplayListener listener;
	private final SavePillarDrawable savePillarDrawable;
	private boolean pause, inputEnable = true, playerTrack = true, desireFixedRotation = true;
	private final Random random;
	
	public WorldManager(Player player, GDXLevel level, AssetManager sharedAssets, IGameplayListener listener){
		this.player = player;
		this.level = level;
		this.sharedAssets = sharedAssets;
		this.listener = listener;
		random = new Random();
		tweenManager = new TweenManager();
		dropManager = new DropManager();
		Weapon gun = player.getEquippedWeapon();
		if(gun != null && !(gun instanceof Melee))
			((Gun)gun).addCurrentRounds(gun.getRoundsPerClip() - ((Gun)gun).getCurrentRounds());
		createLevelStruct = level.createLevel(world);
		world.setContactListener(new ContactListener(this));
		aiWorld = new AIWorld(world);
		aiWorldDebug = aiWorld.createGraphVisible();
		for(GDXNPC gdxNPC : level.getNpcs())
			spawnNPC(gdxNPC, aiWorld);
		savePillarDrawable = new SavePillarDrawable(level, sharedAssets);
	}

	public void render(float dt, GDXRenderer gdxRenderer, Camera cam, Batch batch){
		if(!pause && inputEnable && !player.isDead())
			player.setFixedRotation(desireFixedRotation);
		batch.end();
		batch.begin();
		if(Properties.getBool("save.pillar.draw", true))
			savePillarDrawable.render(dt, null, batch, cam, gdxRenderer);
		if(player.isSpawned())
			player.render(dt, world, batch, sharedAssets, gdxRenderer, this, pause, inputEnable);
		for(NPC npc : npcs)
			npc.render(dt, world, batch, sharedAssets, gdxRenderer, this, pause, true);
		for(Iterator<Entry<Body, GunShot>> iter = gunshots.entrySet().iterator(); iter.hasNext();){
			Entry<Body, GunShot> entry = iter.next();
			if(entry.getValue().isCanRemove())
				iter.remove();
			else
				entry.getValue().render(dt, batch, sharedAssets, entry.getKey(), this);
		}
		for(Turret turret : turrets)
			turret.render(dt, batch, gdxRenderer, this);
		if(player.isSpawned())
			dropManager.render(dt, pause, player, world, batch, gdxRenderer, sharedAssets);
		renderTransferredParticles(dt, batch);
		if(Properties.getBool("world.debug.draw", false))
			debugRenderer.render(aiWorldDebug, cam.combined);
		if(!pause)//min 1/20 because larger and you get really high hits on level startup/big cpu hits
			world.step(Math.min(1f/20f, dt*2f), 10, 10);//TODO fix this to be reg, not *2
		for(Body body : getBodiesIterable())
			if(body != null && body.getUserData() != null && body.getUserData().equals(REMOVE_USER_DATA))
				world.destroyBody(body);
		tweenManager.update(dt);
	}
	
	public Iterable<Body> getBodiesIterable(){
		Array<Body> bodyArray = new Array<>(world.getBodyCount());
		world.getBodies(bodyArray);
		return bodyArray;
	}
	
	public static void drawTexture(Batch batch, Body body,
			String textureName, float scale, AssetManager... assetManagers){
		Texture texture = null;
		for(AssetManager assetManager : assetManagers){
			if(!textureName.endsWith("png"))
				Log.error("WorldManager.drawTexture", "Texture must end with png: " + textureName);
			if(assetManager.isLoaded(textureName))
				texture = assetManager.get(textureName);
		}
		if(texture == null)
			Log.error("WorldManager.drawTexture", "Can't find texture: " + textureName);
		Sprite sprite = new Sprite(texture);
		sprite.setPosition(body.getWorldCenter().x - sprite.getWidth()/2, 
				body.getWorldCenter().y - sprite.getHeight()/2);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.setScale(scale);
		sprite.draw(batch);
	}

	public void processHit(float damageBase, Being target, Being origin, Fixture hit, Vector2 normal, Vector2 damagePosition) {
		DamageStruct damage = new DamageStruct();
		damage.setTarget(target);
		damage.setDir(normal);
		damage.setOrigin(origin);
		damage.setDamagePosition(damagePosition);
		float bodypartDmgModifier = target.handleShotDamage(hit, damage);
		float attackModifier = (100f + (origin == null ? 0f : origin.getAttack())) / 100f;
		float defenseModifier = 100f / (100f + target.getDefense());
		damage.setDamage(damageBase * bodypartDmgModifier * 
				attackModifier * defenseModifier);
		getProvider().beingHit(damage);
		if( (!Properties.getBool("character.godmode", false) || target != player) && !target.isInvulnerable() ){
			if(Properties.getBool("being.appendage.break.dead", false) && target.isDead())
				target.getRagdoll().breakAppendage(damage.getBodyPart(), world, damage.getDir());
			target.setHp(target.getHp() - damage.getDamage());
			target.receivedDamage(damage);
			Log.log("WorldManager.processHit","Processed damage on being: " + target.getName() + 
					" dmg: " + damage.getDamage() + " hp: " + target.getHp());
		}
	}

	public VisibilityReturnStruct isVisible(NPC origin){
		Being closestEnemy = null;
		float closestDistanceSq = Float.MAX_VALUE;
		LinkedList<Being> enemies = new LinkedList<>();
		for(Being being : getAllBeings())
			if(being != origin && !origin.isFriendly(being.getFaction()) && !being.isDead()){
				float currentClosestDistanceSq = being.getPosition().dst2(origin.getPosition());
				boolean closer = closestEnemy == null || closestDistanceSq > currentClosestDistanceSq,
						facingCorrectly = origin.getRagdoll().isFacingLeft() ?
								being.getPosition().x < origin.getPosition().x : 
								being.getPosition().x > origin.getPosition().x;
				if(origin.sees(being, world)){
					enemies.add(being);
					if(closer && (facingCorrectly || currentClosestDistanceSq < origin.getDistanceAware())){
						closestDistanceSq = currentClosestDistanceSq;
						if(closestDistanceSq < origin.getDistanceVision())
							closestEnemy = being;
					}
				}
			}
		return new VisibilityReturnStruct(enemies, closestEnemy);
	}
	
	public NPC spawnNPC(GDXNPC gdxNPC, AIWorld aiWorld){
		String npcDataName = gdxNPC.getProperties().get("NPCData");
		if(npcDataName == null)
			npcDataName = gdxNPC.getName();
		NPCData npcData = NPCData.parse(npcDataName);
		if(npcData == null){
			npcData = new NPCData();
			Log.error("WorldManager.spawnNPC", "NPC failed to initialize " + gdxNPC + ", attempting defaults");
		}
		try{
			npcData.apply(gdxNPC.getProperties());
			return spawnNPC(gdxNPC.getName(), gdxNPC.getCoordinates(), npcData, aiWorld);
		}catch(Exception e){
			Log.error("WorldManager.spawnNPC", "NPC defaults failed for " + gdxNPC + ", giving up");
		}
		return null;
	}
	
	public NPC spawnNPC(String name, Vector2 coordinates, NPCData npcData, AIWorld aiWorld){
		EnumSet<FactionEnum> factions = EnumSet.noneOf(FactionEnum.class);
		for(String factionStr : npcData.get("Faction").split(","))
			factions.add(FactionEnum.valueOf(factionStr.toUpperCase()));
		FactionEnum faction = factions.iterator().next();
		int cash = npcData.getInteger("Cash"),
				npcLevel = npcData.getInteger("Level"), 
				xp = npcData.getInteger("XP");
		DifficultyEnum difficulty = DifficultyEnum.valueOf(Properties.get(
				"npc.difficulty.value", DifficultyEnum.MEDIUM.name()));
		//Generate weapon list once per vendor, first if he has specific weapons, then generate randomly
		LinkedList<Weapon> vendorWeapons = new LinkedList<Weapon>();
		for(String weapon : npcData.get("VendorWeapons").split(","))
			if(!weapon.equals(""))
				vendorWeapons.add(WeaponFactory.getWeapon(weapon));
		if(npcData.getBool("VendorRandom")){
			int count = random.nextInt(5)+5;
			for(int i=0; i<count; i++)
				vendorWeapons.add(WeaponFactory.generateGun(npcLevel, player.getLevel()));
		}
		List<Weapon> weapons = WeaponFactory.getGuns(npcData.get("Weapons"));
		int currentWeapon = 0;
		for(int i=0; i<weapons.size(); i++)
			if(!(weapons.get(i) instanceof Melee))
				currentWeapon = i;
		NPC npc = new NPC(name, weapons, 
				new ArrayList<Weapon>(), Stats.parseNPCData(npcData), currentWeapon, cash, 
				npcLevel, xp, npcData.get("Behavior"), level.getPath(npcData.get("Path")),
				faction, factions, this, npcData.get("Resource"), npcData.get("RagdollResource"),
				difficulty, aiWorld, npcData.getBool("Vendor"), vendorWeapons, npcData.getBool("boss"));
		npc.aim(npcData.getFloat("Aim"));
		npcs.add(npc);
		npc.respawn(world, coordinates.x, coordinates.y);
		listener.npcAdded(npc);
		return npc;
	}
	
	public void setRespawnLocation(Vector2 respawnLocation) {
		this.respawnLocation = respawnLocation;
		if(!player.isSpawned())
			player.respawn(world, respawnLocation.x, respawnLocation.y);
	}

	@Override public void dead(Being being) {
		if(being != player)
			dropManager.generateDrop(being, world);
		being.death(this);
	}

	public void respawnPlayer() {
		player.respawn(world, respawnLocation.x, respawnLocation.y);
	}

	public List<Being> getAllBeings() {
		List<Being> beings = new LinkedList<>();
		beings.addAll(npcs);
		if(player.isSpawned())
			beings.add(player);
		return beings;
	}

	public CreateLevelReturnStruct getCreateLevelStruct() {
		return createLevelStruct;
	}
	
	public Vector2 getRespawnLocation(){
		return respawnLocation;
	}
	
	public Map<Body,GunShot> getGunshots(){
		return gunshots;
	}
	
	public void changePlayerWeapon(int weapon){
		player.setCurrentWeapon(weapon, world, false);
	}

	public DropManager getDropManager() {
		return dropManager;
	}

	public GDXPath getPath(String path) {
		return level.getPath(path);
	}

	public void pause(boolean pause) {
		this.pause = pause;
	}
	
	public boolean isPause(){
		return pause;
	}

	public void setInputEnable(boolean inputEnable) {
		this.inputEnable = inputEnable;
		if(!inputEnable)
			player.stopMovement();
	}
	
	public boolean isInputEnable(){
		return inputEnable && player.isSpawned();
	}
	
	/**
	 * @return trigger info provider reflectively. Some nastiness for the greater good?
	 */
	public QuestTriggerInformationProvider getProvider(){
		try {
			Field providerField = player.getQuestManager().getClass().getDeclaredField("provider");
			providerField.setAccessible(true);
			return (QuestTriggerInformationProvider) providerField.get(player.getQuestManager());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void renderTransferredParticles(float dt, Batch batch){
		for(Iterator<ParticleEffect> i = particles.iterator(); i.hasNext();){
			ParticleEffect effect = i.next();
			effect.draw(batch, dt);
			if(effect.isComplete())
				i.remove();
		}
	}
	
	public List<Being> matchBeings(String beingName){
		LinkedList<Being> targets = new LinkedList<>();
		if("player".matches(beingName))
			targets.add(player);
		for(Being being : getAllBeings())
			if(being.getName().matches(beingName))
				targets.add(being);
		return targets;
	}

	public List<Joint> matchPhysicsJoint(String name) {
		LinkedList<Joint> joints = new LinkedList<>();
		Array<Joint> allJoints = new Array<>(world.getJointCount());
		world.getJoints(allJoints);
		for(Joint joint : allJoints)
			if(joint.getUserData() != null && joint.getUserData() instanceof String && ((String)joint.getUserData()).matches(name))
				joints.add(joint);
		return joints;
	}

	public List<Body> matchPhysicsObject(String name) {
		LinkedList<Body> bodies = new LinkedList<>();
		Array<Body> bodyArray = new Array<>(world.getBodyCount());
		world.getBodies(bodyArray);
		for(Body body : bodyArray)
			if(name != null && body.getUserData() instanceof String && ((String)body.getUserData()).matches(name))
				bodies.add(body);
		return bodies;
	}
	
	public static void playSoundTuned(Sound sound, Vector2 origin, Vector2 destination){
		float pan = destination == null ? 0f : Math.max(-1, Math.min(1, (destination.x - origin.x) / 15f));
		float volume = destination == null ? 1f : (float)Math.min(1, 1.0/Math.log(destination.dst(origin)+1f));
		sound.play(volume * Properties.getFloat("sound.volume", 1f), 1, pan);
	}

	public void transferParticles(ParticleEffect... particles) {
		this.particles.addAll(Arrays.asList(particles));
	}

	public AIWorld getAiWorld() {
		return aiWorld;
	}

	public void turretAdd(Turret turret) {
		turrets.add(turret);
	}
	
	public LinkedList<Turret> getTurrets(){
		return turrets;
	}

	public TweenManager getTweenManager() {
		return tweenManager;
	}

	/**
	 * @return vendor if player is close enough, otherwise null
	 */
	public NPC findVendor() {
		for(NPC npc : npcs)
			if(npc.isVendor() && npc.getPosition().dst(player.getPosition()) < Properties.getFloat("vendor.distance", 2f))
				return npc;
		return null;
	}

	public void dispose(Being being) {
		being.dispose(world);
		npcs.remove(being);
	}

	public Random getRandom() {
		return random;
	}

	public AssetManager getSharedAssets() {
		return sharedAssets;
	}

	public boolean isPlayerTrack() {
		return playerTrack;
	}

	public void setPlayerTrack(boolean playerTrack) {
		this.playerTrack = playerTrack;
	}

	public void setDesireFixedRotation(boolean desireFixedRotation) {
		this.desireFixedRotation = desireFixedRotation;
	}
	
	public Player getPlayer(){
		return player;
	}

	public World getWorld() {
		return world;
	}
}
