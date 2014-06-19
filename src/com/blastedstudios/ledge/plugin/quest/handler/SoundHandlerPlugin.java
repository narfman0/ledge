package com.blastedstudios.ledge.plugin.quest.handler;

import java.util.HashMap;

import com.badlogic.gdx.audio.Sound;
import com.blastedstudios.gdxworld.plugin.mode.sound.SoundManager;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.ISoundHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.SoundManifestationEnum;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class SoundHandlerPlugin implements ISoundHandler{
	private final HashMap<String, Long> soundIdMap = new HashMap<>();

	@Override public CompletionEnum sound(SoundManifestationEnum manifestationType, String name,
			String filename, float volume, float pan, float pitch) {
		Sound sound = SoundManager.getSound(filename);
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
