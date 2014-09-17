package com.blastedstudios.ledge.plugin.quest.handler;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.ISoundHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.SoundManifestationEnum;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.world.WorldManager;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class SoundHandlerPlugin implements ISoundHandler{
	private final HashMap<String, Long> soundIdMap = new HashMap<>();
	private WorldManager world;
	
	public void setWorldManager(WorldManager world){
		this.world = world;
	}

	@Override public CompletionEnum sound(SoundManifestationEnum manifestationType, String name,
			String filename, float volume, float pan, float pitch) {
		String path = "data/sounds/" + filename + ".mp3";
		if(!world.getSharedAssets().isLoaded(path)){
			Gdx.app.error("SoundHandlerPlugin.sound", "Sound not available: " + path);
			return CompletionEnum.COMPLETED;
		}
		Sound sound = world.getSharedAssets().get(path, Sound.class);
		switch(manifestationType){
		case PLAY:
			soundIdMap.put(name, sound.play(volume, pan, pitch));
			break;
		case LOOP:
			soundIdMap.put(name, sound.loop(volume, pan, pitch));
			break;
		case PAUSE:
			sound.pause(soundIdMap.get(name));
			break;
		case PITCHPAN:
			sound.setPitch(soundIdMap.get(name), pitch);
			sound.setPan(soundIdMap.get(name), pan, volume);
		case RESUME:
			sound.resume(soundIdMap.get(name));
			break;
		case STOP:
			sound.stop();
			break;
		case VOLUME:
			sound.setVolume(soundIdMap.get(name), volume);
			break;
		default:
			break;
		}
		return CompletionEnum.COMPLETED;
	}
}
