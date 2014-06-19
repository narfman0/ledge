package com.blastedstudios.ledge.plugin.quest.handler;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.particle.IParticleHandler;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.particle.ParticleManifestationTypeEnum;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class ParticleHandlerPlugin implements IParticleHandler{
	private GameplayScreen screen;
	
	public void setGameplayScreen(GameplayScreen screen){
		this.screen = screen;
	}

	@Override public CompletionEnum particle(String name, String effectFile,
			String imagesDir, int duration, Vector2 position,
			ParticleManifestationTypeEnum modificationType, String emitterName,
			String attachedBody) {
		switch(modificationType){
		case REMOVE:
			screen.getParticleManager().scheduleRemove(name);
			break;
		case MODIFY:
			screen.getParticleManager().modify(name, duration, position, emitterName, attachedBody);
			break;
		case CREATE:
			screen.getParticleManager().addParticle(new GDXParticle(name, 
					effectFile, imagesDir, duration, position, emitterName, attachedBody));
			break;
		}
		return CompletionEnum.COMPLETED;
	}
}
