package com.blastedstudios.ledge.plugin.ragdoll.garbageman;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.ledge.physics.PhysicsEnvironment;
import com.blastedstudios.ledge.plugin.ragdoll.loader.RagdollLoader;
import com.blastedstudios.ledge.world.being.Being;

public class RagdollGarbageman extends RagdollLoader {
	public RagdollGarbageman(World world, float x, float y, Being being, TextureAtlas atlas, FileHandle file){
		super(world, x, y, being, atlas, createBodyMap(world, file, x, y));
		for(Body body : bodies)
			for(Fixture fixture : body.getFixtureList()){
				Filter filter = fixture.getFilterData();
				filter.maskBits = PhysicsEnvironment.MASK_ENEMY;
				filter.categoryBits = PhysicsEnvironment.CAT_ENEMY;
				fixture.setFilterData(filter);
			}
		Filter filter = lHandFixture.getFilterData();
		filter.maskBits = PhysicsEnvironment.MASK_NOTHING;
		filter.categoryBits = PhysicsEnvironment.CAT_NOTHING;
		lHandFixture.setFilterData(filter);
	}
}
