package com.blastedstudios.ledge.world.being;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import jbt.execution.core.BTExecutorFactory;
import jbt.execution.core.ContextFactory;
import jbt.execution.core.IBTExecutor;
import jbt.execution.core.IBTLibrary;
import jbt.execution.core.IContext;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXPath;
import com.blastedstudios.ledge.ai.AIWorld;
import com.blastedstudios.ledge.world.Stats;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Melee;
import com.blastedstudios.ledge.world.weapon.Weapon;

public class NPC extends Being {
	public enum AIFieldEnum{AI_WORLD, OBJECTIVE, SELF, LAST_HEALTH, WORLD, TURRET}
	public enum DifficultyEnum{HARD, MEDIUM, EASY}
	private static final long serialVersionUID = 1L;
	private IContext context;
	private IBTExecutor btExecutor;
	private GDXPath path;
	private final DifficultyEnum difficulty;
	private final boolean vendor;
	private final LinkedList<Weapon> vendorWeapons;
	
	public NPC(String name, List<Weapon> guns, List<Weapon> inventory, Stats stats,
			int currentGun, int cash, int level, int xp, String behavior,
			GDXPath path, FactionEnum faction, EnumSet<FactionEnum> factions,
			WorldManager world, String resource, String ragdollResource, 
			DifficultyEnum difficulty, AIWorld aiWorld, boolean vendor,
			LinkedList<Weapon> vendorWeapons) {
		super(name, guns, inventory, stats, currentGun, cash, level, xp, 
				faction, factions, resource, ragdollResource);
		this.difficulty = difficulty;
		this.vendor = vendor;
		this.vendorWeapons = vendorWeapons;
		String basePackage = "com.blastedstudios.ledge.ai.bt.trees";
		try{
			Class<?> btLibClass = Class.forName(basePackage+"."+behavior);
			IBTLibrary library = (IBTLibrary) btLibClass.newInstance();
			context = ContextFactory.createContext(library);
			context.setVariable(AIFieldEnum.AI_WORLD.name(), aiWorld);
			context.setVariable(AIFieldEnum.SELF.name(), this);
			context.setVariable(AIFieldEnum.WORLD.name(), world);
			setPath(path);
			btExecutor = BTExecutorFactory.createBTExecutor(library.getBT("Root"), context);
		}catch(Exception e){
			Gdx.app.error("NPC.<init>", "Error making NPC: " + name + " with message: " + e.getMessage());
		}
	}
	
	@Override public void render(float dt, World world, SpriteBatch spriteBatch, 
			GDXRenderer gdxRenderer, IDeathCallback callback){
		super.render(dt, world, spriteBatch, gdxRenderer, callback);
		if(!dead)
			btExecutor.tick();
	}

	public GDXPath getPath() {
		return path;
	}
	
	public void setPath(GDXPath path){
		this.path = path;
		stopMovement();
		if(path != null)
			context.setVariable(AIFieldEnum.OBJECTIVE.name(), path.clone());
		else{
			context.clearVariable(AIFieldEnum.OBJECTIVE.name());
			context.clearVariable("CurrentObjectiveTarget");//make sure its removed
		}
	}
	
	@Override public void reload(){
		super.reload();
		Weapon gun = getEquippedWeapon();
		if(gun != null && !(gun instanceof Melee))
			((Gun)gun).addCurrentRounds(gun.getRoundsPerClip() - ((Gun)gun).getCurrentRounds());
	}

	public DifficultyEnum getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Used as a handicap/difficulty slider, this function provides the value
	 * for NPCs to wait before shooting the next round so they don't slam a
	 * low level/easy difficulty player with an endless hail of bullets.
	 * @param level of the NPC shooting
	 * @param difficulty of the game, lower difficulty will increase time
	 * @return Delay before an NPC of the given level may shoot his weapon. 
	 */
	public static long shootDelay(int level, DifficultyEnum difficulty){
		int scalar = Properties.getInt("npc.difficulty.shoot.delay.scalar", 2000);
		return 5186L - 1045L * (long)Math.log(level) + scalar * difficulty.ordinal();
	}
	
	public float getDistanceAware(){
		return stats.getDistanceAware();
	}
	
	public float getDistanceVision(){
		return stats.getDistanceVision();
	}

	public boolean isVendor() {
		return vendor;
	}

	public LinkedList<Weapon> getVendorWeapons() {
		return vendorWeapons;
	}
}
