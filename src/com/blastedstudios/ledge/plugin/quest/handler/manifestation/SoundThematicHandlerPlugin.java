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
	private AssetManager assets;
	
	@Override public CompletionEnum sound(SoundManifestationEnum manifestationType, String name,
			String filename, float volume, float pan, float pitch) {
		String path = "data/sounds/music/" + filename + ".mp3";
		if(!assets.isLoaded(path)){
			Log.error("SoundHandlerPlugin.sound", "Sound not available: " + path);
			return CompletionEnum.COMPLETED;
		}
		switch(manifestationType){
		case THEMATIC:
			previous = current;
			previous.stop();
			current = assets.get(path, Sound.class);
			current.loop();
			break;
		default:
			break;
		}
		return CompletionEnum.EXECUTING;
	}

	@Override public void setAssets(AssetManager assets) {
		this.assets = assets;
		String defaultMusic = Properties.get("sound.theme.music", "EpicMovieGameTrailer");
		current = assets.get("data/sounds/music/" + defaultMusic + ".mp3", Sound.class);
		current.loop();
	}
}
