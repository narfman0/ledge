package com.blastedstudios.ledge.world.being.component.levelupsound;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.blastedstudios.ledge.plugin.quest.handler.ISharedAssetConsumer;
import com.blastedstudios.ledge.world.being.component.AbstractComponent;

@PluginImplementation
public class LevelUpSoundComponent extends AbstractComponent implements ISharedAssetConsumer{
	private AssetManager assets;
	
	@Override public void levelUp(){
		assets.get("data/sounds/levelup.mp3", Sound.class).play();
	}

	@Override public void setAssets(AssetManager assets) {
		this.assets = assets;
	}
}
