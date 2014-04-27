package com.blastedstudios.ledge.ui.gameplay.particles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.shape.GDXShape;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;

public class ParticleManager {
	private static final long TIME_TO_REMOVE_SCHEDULED = Properties.getInt("particles.remove.time", 11000);
	private final HashMap<String, ParticleStruct> particles = new HashMap<>();
	private final SpriteBatch spriteBatch = new SpriteBatch();

	public void addParticle(GDXParticle particle){
		if(!particles.containsKey(particle.getName())){
			particles.put(particle.getName(), new ParticleStruct(particle.createEffect(), particle));
			Gdx.app.log("ParticleManager.addParticle", "Particle added: " + particle);
		}
	}

	public void render(float dt, OrthographicCamera camera, WorldManager worldManager){
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for(Iterator<Entry<String,ParticleStruct>> iter = particles.entrySet().iterator(); iter.hasNext();){
			Entry<String,ParticleStruct> entry = iter.next();
			ParticleStruct struct = entry.getValue();
			if(!struct.particle.getAttachedBody().isEmpty()){
				Being being = worldManager.getAllBeings().get(struct.particle.getAttachedBody());
				if(being != null)
					struct.effect.setPosition(being.getPosition().x, being.getPosition().y);
				else
					for(Entry<GDXShape, Body> body : worldManager.getCreateLevelStruct().bodies.entrySet())
						if(body.getKey().getName().equals(struct.particle.getAttachedBody()))
							struct.effect.setPosition(body.getValue().getPosition().x, body.getValue().getPosition().y);
			}
			if(struct.particle.getEmitterName().isEmpty())
				struct.effect.draw(spriteBatch, dt);
			else
				struct.effect.findEmitter(struct.particle.getEmitterName()).draw(spriteBatch, dt);
			
			if(struct.isRemoveScheduled() && 
					(System.currentTimeMillis() - struct.getRemoveScheduled() > TIME_TO_REMOVE_SCHEDULED)){
				disposeParticle(entry.getKey());
				iter.remove();
			}
		}
		spriteBatch.end();
	}

	public void scheduleRemove(String name) {
		particles.get(name).scheduleRemove(name);
	}
	
	public void disposeParticle(String name){
		ParticleStruct struct = particles.get(name);
		if(struct == null)
			Gdx.app.error("ParticleManager.disposeParticle", "Particle remove requested for " + 
					name + ", but no such particle exists");
		else{
			struct.effect.dispose();
			Gdx.app.debug("ParticleManager.disposeParticle", "Disposed " + name);
		}
	}

	public void modify(String name, int duration, Vector2 position,
			String emitterName, String attachedBody) {
		ParticleStruct struct = particles.get(name);
		if(struct != null){
			if(duration != -1)
				struct.effect.setDuration(duration);
			struct.effect.setPosition(position.x, position.y);
			struct.particle.setEmitterName(emitterName);
			struct.particle.setAttachedBody(attachedBody);
		}else
			Gdx.app.error("ParticleManager.modify", "Particle modify requested for " + 
					name + ", but no such particle exists");
	}
}
