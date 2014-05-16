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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXLevel.CreateLevelReturnStruct;
import com.blastedstudios.gdxworld.world.GDXNPC;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.ledge.ai.AIWorld;
import com.blastedstudios.ledge.physics.ContactListener;
import com.blastedstudios.ledge.physics.VisibleQueryCallback;
import com.blastedstudios.ledge.util.VisibilityReturnStruct;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.FactionEnum;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.DifficultyEnum;
import com.blastedstudios.ledge.world.being.NPCData;
import com.blastedstudios.ledge.world.being.Being.IDeathCallback;
import com.blastedstudios.ledge.world.being.Player;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Melee;
import com.blastedstudios.ledge.world.weapon.Turret;
import com.blastedstudios.ledge.world.weapon.Weapon;
import com.blastedstudios.ledge.world.weapon.WeaponFactory;
import com.blastedstudios.ledge.world.weapon.DamageStruct;
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
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final DropManager dropManager;
	private final GDXLevel level;
	private final LinkedList<ParticleEffect> particles = new LinkedList<>();
	private final LinkedList<Turret> turrets = new LinkedList<>();
	private final AIWorld aiWorld;
	private boolean pause, inputEnable = true;
	
	public WorldManager(Player player, GDXLevel level){
		this.player = player;
		this.level = level;
		dropManager = new DropManager();
		Weapon gun = player.getEquippedWeapon();
		if(gun != null && !(gun instanceof Melee))
			((Gun)gun).addCurrentRounds(gun.getRoundsPerClip() - ((Gun)gun).getCurrentRounds());
		createLevelStruct = level.createLevel(world);
		world.setContactListener(new ContactListener(this));
		aiWorld = new AIWorld(world);
		aiWorldDebug = aiWorld.createGraphVisible();
		for(GDXNPC gdxNPC : level.getNpcs())
			spawnNPC(level, gdxNPC, aiWorld);
	}

	public void render(float dt, GDXRenderer gdxRenderer, Camera cam){
		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
		player.render(dt, world, spriteBatch, gdxRenderer, this);
		for(NPC npc : npcs)
			npc.render(dt, world, spriteBatch, gdxRenderer, this);
		for(Iterator<Entry<Body, GunShot>> iter = gunshots.entrySet().iterator(); iter.hasNext();){
			Entry<Body, GunShot> entry = iter.next();
			entry.getValue().render(dt, spriteBatch, gdxRenderer, entry.getKey(), this);
			if(entry.getValue().isCanRemove())
				iter.remove();
		}
		for(Turret turret : turrets)
			turret.render(dt, spriteBatch, gdxRenderer, this);
		dropManager.render(player, world, spriteBatch, gdxRenderer);
		renderTransferredParticles(dt);
		spriteBatch.end();
		if(Properties.getBool("world.debug.draw", false))
			debugRenderer.render(aiWorldDebug, cam.combined);
		if(!pause)//min 1/20 because larger and you get really high hits on level startup/big cpu hits
			world.step(Math.min(1f/20f, dt*2f), 10, 10);//TODO fix this to be reg, not *2
		for(Body body : getBodiesIterable())
			if(body != null && body.getUserData() != null && body.getUserData().equals(REMOVE_USER_DATA))
				world.destroyBody(body);
	}
	
	public Iterable<Body> getBodiesIterable(){
		Array<Body> bodyArray = new Array<>(world.getBodyCount());
		world.getBodies(bodyArray);
		return bodyArray;
	}
	
	public static void drawTexture(SpriteBatch spriteBatch, GDXRenderer renderer, 
			Body body, String textureName, float scale){
		Texture texture = renderer.getTexture(textureName + ".png");
		Sprite sprite = new Sprite(texture);
		sprite.setPosition(body.getWorldCenter().x - sprite.getWidth()/2, 
				body.getWorldCenter().y - sprite.getHeight()/2);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.setScale(scale);
		sprite.draw(spriteBatch);
	}

	public void processHit(float damageBase, Being target, Being origin, Fixture hit, Vector2 normal) {
		DamageStruct damage = new DamageStruct();
		damage.setTarget(target);
		float bodypartDmgModifier = target.handleShotDamage(hit, damage);
		float attackModifier = (100f + (origin == null ? 0f : origin.getAttack())) / 100f;
		float defenseModifier = 100f / (100f + target.getDefense());
		damage.setDamage(damageBase * bodypartDmgModifier * 
				attackModifier * defenseModifier);
		damage.setDir(normal);
		damage.setOrigin(origin);
		getProvider().beingHit(damage);
		if( (!Properties.getBool("character.godmode", false) || target != player) && !target.isInvulnerable() ){
			target.setHp(target.getHp() - damage.getDamage());
			target.receivedDamage(damage);
			Gdx.app.log("WorldManager.processHit","Processed damage on being: " + target.getName() + 
					" dmg: " + damage.getDamage() + " hp: " + target.getHp());
		}
	}
	
	public Player getPlayer(){
		return player;
	}

	public World getWorld() {
		return world;
	}

	public List<Being> getAllBeings() {
		List<Being> beings = new LinkedList<>();
		beings.addAll(npcs);
		beings.add(player);
		return beings;
	}

	public VisibilityReturnStruct isVisible(NPC origin){
		Being closestEnemy = null;
		float closestDistanceSq = Float.MAX_VALUE;
		int enemyCount = 0;
		for(Being being : getAllBeings())
			if(being != origin && !origin.isFriendly(being.getFaction()) && !being.isDead()){
				float currentClosestDistanceSq = being.getPosition().dst2(origin.getPosition());
				boolean closer = closestEnemy == null || closestDistanceSq > currentClosestDistanceSq,
						facingCorrectly = origin.getRagdoll().isFacingLeft() ?
								being.getPosition().x < origin.getPosition().x : 
								being.getPosition().x > origin.getPosition().x;
				if(closer && (facingCorrectly || currentClosestDistanceSq < origin.getDistanceAware())){
					VisibleQueryCallback callback = new VisibleQueryCallback(origin, being);
					world.rayCast(callback, origin.getPosition(), being.getPosition());
					if(!callback.called){
						closestDistanceSq = currentClosestDistanceSq;
						if(closestDistanceSq < origin.getDistanceVision()){
							enemyCount++;
							closestEnemy = being;
						}
					}
				}
			}
		return new VisibilityReturnStruct(enemyCount, closestEnemy);
	}

	public CreateLevelReturnStruct getCreateLevelStruct() {
		return createLevelStruct;
	}

	public void respawnPlayer() {
		player.respawn(world, respawnLocation.x, respawnLocation.y);
	}
	
	public Vector2 getRespawnLocation(){
		return respawnLocation;
	}

	public void setRespawnLocation(Vector2 respawnLocation) {
		this.respawnLocation = respawnLocation;
	}
	
	public Map<Body,GunShot> getGunshots(){
		return gunshots;
	}
	
	public NPC spawnNPC(GDXLevel level, GDXNPC gdxNPC, AIWorld aiWorld){
		NPCData npcData = NPCData.parse(gdxNPC.getProperties().get("NPCData"));
		if(npcData == null){
			npcData = new NPCData();
			Gdx.app.error("WorldManager.<init>", "NPC failed to initialize " + gdxNPC + ", attempting defaults");
		}
		npcData.apply(gdxNPC.getProperties());
		return spawnNPC(gdxNPC.getName(), gdxNPC.getCoordinates(), npcData, aiWorld);
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
		NPC npc = new NPC(name, WeaponFactory.getGuns(npcData.get("Weapons")), 
				new ArrayList<Weapon>(), Stats.parseNPCData(npcData), 0, cash, 
				npcLevel, xp, npcData.get("Behavior"), level.getPath(npcData.get("Path")),
				faction, factions, this, npcData.get("Resource"), npcData.get("RagdollResource"),
				difficulty, aiWorld);
		npc.aim(npcData.getFloat("Aim"));
		npcs.add(npc);
		npc.respawn(world, coordinates.x, coordinates.y);
		return npc;
	}
	
	public void changePlayerWeapon(int weapon){
		player.setCurrentWeapon(weapon, world);
	}

	@Override public void dead(Being being) {
		if(being != player)
			dropManager.generateDrop(being, world);
		being.death(this);
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
		return inputEnable;
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

	public void transferParticles(ParticleEffect... particles) {
		this.particles.addAll(Arrays.asList(particles));
	}
	
	private void renderTransferredParticles(float dt){
		for(Iterator<ParticleEffect> i = particles.iterator(); i.hasNext();){
			ParticleEffect effect = i.next();
			effect.draw(spriteBatch, dt);
			if(effect.isComplete())
				i.remove();
		}
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
}
