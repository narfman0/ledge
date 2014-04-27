package com.blastedstudios.ledge.world.being.component.blood;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.being.component.AbstractComponent;
import com.blastedstudios.ledge.world.weapon.DamageStruct;

@PluginImplementation
public class BloodComponent extends AbstractComponent {
	private static Random RANDOM = new Random();
	private static final long BLOOD_TIME = Properties.getInt("character.blood.time", 10000);
	private transient LinkedList<BloodStruct> blood = new LinkedList<>();
	
	@Override public void render(float dt, SpriteBatch spriteBatch, GDXRenderer gdxRenderer, boolean facingLeft){
		for(Iterator<BloodStruct> iter = blood.iterator(); iter.hasNext();){
			BloodStruct current = iter.next();
			current.effect.draw(spriteBatch, dt/5f);
			if(System.currentTimeMillis() - current.startTime > BLOOD_TIME){
				current.effect.dispose();
				iter.remove();
			}
		}
	}

	@Override public void receivedDamage(DamageStruct damage){
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/particles/blood.p"), Gdx.files.internal("data/particles"));
		Body body = being.getRagdoll().getBodyPart(damage.getBodyPart());
		effect.setPosition(body.getWorldCenter().x, body.getWorldCenter().y);
		float angle = (float)Math.atan2(damage.getDir().y, damage.getDir().x);
		ParticleEmitter emitter = effect.findEmitter("blood");
		float angleDegrees = (float)Math.toDegrees(angle), quantityScalar = 20f + damage.getDamage()/100f;
		emitter.getAngle().setHigh(angleDegrees - 20f - ((float)RANDOM.nextGaussian() * quantityScalar), 
				angleDegrees + 20f + ((float)RANDOM.nextGaussian() * quantityScalar));
		emitter.getAngle().setLow(angleDegrees);
		effect.start();
		blood.add(new BloodStruct(System.currentTimeMillis(), effect));
	}
}
