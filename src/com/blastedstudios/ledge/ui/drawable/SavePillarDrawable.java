package com.blastedstudios.ledge.ui.drawable;

import java.util.LinkedList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.beingspawn.BeingSpawnManifestation;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.drawable.Drawable;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.quest.GDXQuest;

public class SavePillarDrawable extends Drawable {
	private final Sprite sprite;
	private final LinkedList<Vector2> pillarPositions = new LinkedList<>();
	
	public SavePillarDrawable(GDXLevel level, AssetManager sharedAssets){
		sprite = new Sprite(sharedAssets.get("data/textures/savePillar.png"));
		sprite.setScale(.02f);
		for(GDXQuest quest : level.getQuests())
			if(quest.getManifestation() instanceof BeingSpawnManifestation){
				BeingSpawnManifestation manifestation = (BeingSpawnManifestation) quest.getManifestation();
				if(manifestation.getNpc().getName().equals("player") || manifestation.getNpc().getName().equals("colligan"))
					pillarPositions.add(manifestation.getNpc().getCoordinates());
			}
	}

	@Override public void render(float dt, AssetManager assetManager, Batch batch, Camera camera, GDXRenderer renderer) {
		for(Vector2 position : pillarPositions){
			sprite.setPosition(position.x - sprite.getWidth()/2f, position.y - sprite.getHeight()/2f);
			sprite.draw(batch);
		}
	}
}
