package com.blastedstudios.ledge.world.being.component.aidamage;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.component.AbstractComponent;
import com.blastedstudios.ledge.world.weapon.DamageStruct;

@PluginImplementation
public class AIDamageComponent extends AbstractComponent{
	private static float DAMAGE_TURN_DISTANCE = Properties.getFloat("ai.damage.received.turn.distance", 50f);
	@Override public void receivedDamage(DamageStruct damage){
		if(damage == null || damage.getOrigin() == null)
			return;
		Vector2 o = damage.getOrigin().getPosition(), t = being.getPosition();
		if(being instanceof NPC && o.dst(t) < DAMAGE_TURN_DISTANCE)
			being.getRagdoll().aim((float)Math.atan2(o.y - t.y, o.x - t.x));
	}
}
