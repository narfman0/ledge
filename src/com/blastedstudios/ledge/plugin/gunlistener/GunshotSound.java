package com.blastedstudios.ledge.plugin.gunlistener;

import java.util.Random;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Gun.IGunListener;

@PluginImplementation
public class GunshotSound implements IGunListener {
	@Override public void shoot(Gun gun, Being source, Random random,
			Vector2 direction, WorldManager world, Vector2 origin, AssetManager sharedAssets) {
		String filename = "data/sounds/guns/" + gun.getFireSound() + ".mp3";
		if(!sharedAssets.isLoaded(filename)){
			Log.error("GunshotSound.shoot", "Sound not loaded: " + filename);
			return;
		}
		final Sound sound = sharedAssets.get(filename, Sound.class);
		if(sound != null){
			Vector2 playerPosition = world.getPlayer().getPosition();
			float volume =  playerPosition == null ? 1f : (float)Math.min(1, 1.0/Math.log(playerPosition.dst(origin)+1f));
			float pan = playerPosition == null ? 0f : Math.max(-1, Math.min(1, (world.getPlayer().getPosition().x - origin.x) / 15f));
			sound.play(volume, 1, pan);
		}else
			Log.error("Being.attack", "Sound null for fireSound: " + gun.getFireSound());
	}

	@Override public void setCurrentRounds(Gun gun, int currentRounds) {}
}
