package com.blastedstudios.ledge.plugin.quest.handler.manifestation;

import java.util.Iterator;
import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.ISoundHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.sound.SoundManifestationEnum;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.plugin.quest.handler.ISharedAssetConsumer;

@PluginImplementation
public class SoundThematicHandlerPlugin implements ISoundHandler, ISharedAssetConsumer{
	private final LinkedList<MusicStruct> previous = new LinkedList<>();
	private MusicStruct current;
	private AssetManager assets;
	
	@Override public CompletionEnum sound(float dt, SoundManifestationEnum manifestationType,
			String name, String filename, float volume, float pan, float pitch) {
		String path = "data/sounds/" + filename + ".mp3";
		if(!assets.isLoaded(path)){
			Log.error("SoundThematicHandlerPlugin.sound", "Sound not available: " + path);
			return CompletionEnum.COMPLETED;
		}
		switch(manifestationType){
		case THEMATIC:
			previous.add(new MusicStruct(current.sound, current.id));
			Sound sound = assets.get(path, Sound.class);
			current = new MusicStruct(sound, sound.loop(0f));
			return CompletionEnum.EXECUTING;
		default:
			return CompletionEnum.NOT_STARTED;
		}
	}

	@Override public CompletionEnum tick(float dt) {
		for(Iterator<MusicStruct> iter = previous.iterator(); iter.hasNext();){
			MusicStruct struct = iter.next();
			struct.time -= dt;
			struct.sound.setVolume(struct.id, struct.time/struct.duration);
			if(struct.time <= 0f){
				struct.sound.stop(struct.id);
				iter.remove();
				if(previous.isEmpty())
					current.time = struct.duration;
			}
		}
		if(previous.isEmpty()){
			current.time -= dt;
			if(current.time <= 0f){
				current.sound.setVolume(current.id, 1f);
				return CompletionEnum.COMPLETED;
			}else{
				current.sound.setVolume(current.id, (current.duration - current.time)/current.duration);
				return CompletionEnum.EXECUTING;
			}
		}
		return CompletionEnum.EXECUTING;
	}

	@Override public void setAssets(AssetManager assets) {
		this.assets = assets;
		String defaultMusic = Properties.get("sound.music", "EpicMovieGameTrailer");
		Sound sound = assets.get("data/sounds/music/" + defaultMusic + ".mp3", Sound.class);
		current = new MusicStruct(sound, sound.loop());
	}
	
	private class MusicStruct{
		final float duration = Properties.getFloat("sound.music.fade.duration", 2f); 
		final Sound sound;
		final long id;
		float time = duration;
		
		MusicStruct(Sound sound, long id){
			this.sound = sound;
			this.id = id;
		}
	}
}
