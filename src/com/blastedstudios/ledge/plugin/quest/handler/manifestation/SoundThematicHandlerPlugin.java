package com.blastedstudios.ledge.plugin.quest.handler.manifestation;

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
	private Sound current, previous = null;
	private long currentId, previousId;
	private float transitionTime = 0f;
	private AssetManager assets;
	
	@Override public CompletionEnum sound(float dt, SoundManifestationEnum manifestationType,
			String name, String filename, float volume, float pan, float pitch) {
		String path = "data/sounds/" + filename + ".mp3";
		if(!assets.isLoaded(path)){
			Log.error("SoundThematicHandlerPlugin.sound", "Sound not available: " + path);
			return CompletionEnum.COMPLETED;
		}
		float duration = Properties.getFloat("sound.music.fade.duration", 2f);
		switch(manifestationType){
		case THEMATIC:
			transitionTime = duration;
			previous = current;
			current = assets.get(path, Sound.class);
			currentId = current.loop(0f);
			return CompletionEnum.EXECUTING;
		default:
			return CompletionEnum.NOT_STARTED;
		}
	}

	@Override public CompletionEnum tick(float dt) {
		transitionTime -= dt;
		if(transitionTime <= 0f){
			previous.stop(previousId);
			previous = null;
			current.setVolume(currentId, 1f);
			return CompletionEnum.COMPLETED;
		}else{
			float duration = Properties.getFloat("sound.music.fade.duration");
			current.setVolume(currentId, (duration - transitionTime)/duration);
			previous.setVolume(previousId, transitionTime/duration);
			Log.log("SoundTheme.tick", "Current: " + (duration - transitionTime)/duration + " previous: " + transitionTime/duration);
			return CompletionEnum.EXECUTING;
		}
	}

	@Override public void setAssets(AssetManager assets) {
		this.assets = assets;
		String defaultMusic = Properties.get("sound.music", "EpicMovieGameTrailer");
		current = assets.get("data/sounds/music/" + defaultMusic + ".mp3", Sound.class);
		currentId = current.loop();
	}
}
