package com.blastedstudios.ledge.plugin.ragdoll.loader;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.group.GDXGroupExportStruct;
import com.blastedstudios.ledge.physics.ragdoll.AbstractRagdoll;
import com.blastedstudios.ledge.world.being.Being;

public class RagdollLoader extends AbstractRagdoll {
	public RagdollLoader(World world, float x, float y, Being being, TextureAtlas atlas, Map<String,Body> bodyMap){
		super(being, atlas, x, y, bodyMap.get("torso"), bodyMap.get("head"), bodyMap.get("rLeg"), 
				bodyMap.get("lLeg"), bodyMap.get("rArm"), bodyMap.get("lArm"), bodyMap.get("lHand"));
		initializeJoints(world);
		setTransform(x,y,0);
	}
	
	public RagdollLoader(World world, float x, float y, Being being, TextureAtlas atlas, FileHandle file){
		this(world, x, y, being, atlas, createBodyMap(world, file, x, y));
		initializeFilters(being.getMask(), being.getCat());
	}
	
	protected static Map<String,Body> createBodyMap(World world, FileHandle fileHandle, float x, float y){
		GDXGroupExportStruct struct = null;
		for(ISerializer serializer : PluginUtil.getPlugins(ISerializer.class))
			if(serializer.getFileFilter().accept(fileHandle.file()))
				try {
					struct = (GDXGroupExportStruct) serializer.load(fileHandle.file());
				} catch (Exception e) {
					Gdx.app.error("RagdollCustom.<init>", "Error deserializing ragdoll: " + 
							fileHandle.path() +" message: " + e.getMessage());
					e.printStackTrace();
				}
		if(struct == null)
			Gdx.app.error("RagdollCustom.<init>", "Error deserializing ragdoll: " + fileHandle.path());
		return struct.instantiate(world, new Vector2(x,y));
	}
}
