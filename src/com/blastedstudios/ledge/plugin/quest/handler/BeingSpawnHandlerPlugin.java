package com.blastedstudios.ledge.plugin.quest.handler;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.beingspawn.IBeingSpawnHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.NPCData;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class BeingSpawnHandlerPlugin implements IBeingSpawnHandler{
	private WorldManager world;
	
	public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum beingSpawn(String being, Vector2 coordinates,
			String path) {
		if(being.equalsIgnoreCase("player"))
			world.setRespawnLocation(coordinates.cpy());
		else{
			NPCData data = NPCData.parse(being);
			data.set("Path", path);
			world.spawnNPC(being, coordinates, data, world.getAiWorld());
		}
		return CompletionEnum.COMPLETED;
	}

}