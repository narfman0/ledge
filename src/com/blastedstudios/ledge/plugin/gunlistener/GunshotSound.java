package com.blastedstudios.ledge.plugin.gunlistener;

import java.util.Random;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.plugin.mode.sound.SoundManager;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Gun.IGunListener;

@PluginImplementation
public class GunshotSound implements IGunListener {
	@Override public void shoot(Gun gun, Being source, Random random, Vector2 direction, WorldManager world, Vector2 origin) {
		final Sound sound = SoundManager.getSound(gun.getFireSound());
		if(sound != null){
			float volume = 1f - Math.min(1, world.getPlayer().getPosition().dst(origin) / 25f);
			float pan = Math.max(-1, Math.min(1, (world.getPlayer().getPosition().x - origin.x) / 15f));
			sound.play(volume, 1, pan);
		}else
			Gdx.app.error("Being.attack", "Sound null for fireSound: " + gun.getFireSound());
	}

	@Override public void setCurrentRounds(Gun gun, int currentRounds) {}
}
