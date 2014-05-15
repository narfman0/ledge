package com.blastedstudios.ledge.world.being;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.gdxworld.world.quest.GDXQuestManager;
import com.blastedstudios.ledge.world.Stats;
import com.blastedstudios.ledge.world.weapon.Weapon;

public class Player extends Being {
	private static final long serialVersionUID = 1L;
	private GDXQuestManager questManager = new GDXQuestManager();
	private final Map<String,Boolean> levelCompleted = new HashMap<>();

	public Player(String name, List<Weapon> guns, List<Weapon> inventory, Stats stats, int currentGun,
			int cash, int level, int xp, FactionEnum faction,
			EnumSet<FactionEnum> factions, String resource) {
		super(name, guns, inventory, stats, currentGun, cash, level, xp, faction, factions, resource, null);
	}
	
	@Override public void render(float dt, World world, SpriteBatch spriteBatch, 
			GDXRenderer gdxRenderer, IDeathCallback deathCallback){
		super.render(dt, world, spriteBatch, gdxRenderer, deathCallback);
		questManager.tick();
	}

	public GDXQuestManager getQuestManager(){
		return questManager;
	}
	
	public boolean isLevelCompleted(String level){
		return levelCompleted.containsKey(level) && levelCompleted.get(level);
	}
	
	public void setLevelCompleted(String level, boolean completed){
		levelCompleted.put(level, completed);
	}
	
	/**
	 * @return true if the player may start this level
	 */
	public boolean isLevelAvailable(GDXWorld world, GDXLevel level){
		//if player beat level, immediately return true. Player may replay levels, 
		//so this catches the case if he goes back and replays stuff
		if(isLevelCompleted(level.getName()))
			return true;
		String prerequisies = level.getProperties().get("Prerequisites");
		if(prerequisies != null)
			for(String prereq : prerequisies.split(","))
				for(GDXLevel gdxLevel : world.getLevels())
					if(gdxLevel.getName().equals(prereq.trim()) && !isLevelCompleted(prereq))
						return false;
		return true;
	}
}
