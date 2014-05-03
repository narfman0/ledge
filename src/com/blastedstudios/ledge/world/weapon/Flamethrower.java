package com.blastedstudios.ledge.world.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.shot.Flame;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public class Flamethrower extends Gun {
	private static final long serialVersionUID = 1L;

	@Override public String toString(){
		return "[Flamethrower name:" + name + " dmg: " + getDamage() + "]";
	}
	
	@Override public void handleContact(Body body, GunShot gunshot, WorldManager worldManager, WorldManifold manifold){
		Flame flame = (Flame) gunshot;
		if(!flame.isTimeToRemoveSet()){
			flame.setTimeToRemove(System.currentTimeMillis() + Properties.getInt("flame.contact.duration", 1500));
			//would body.setType(BodyType.StaticBody); here, but we are inside the contact
			//listener and the code would case a native crash. Thus we need to delay the
			//deletion until after the solvers are done
			worldManager.getToChangeBodyType().put(body, BodyType.StaticBody);
		}
		if(flame.isTimeToRemoveSet() && System.currentTimeMillis() >= flame.getTimeToRemove()){
			worldManager.transferParticles(flame.flame);
			super.handleContact(body, gunshot, worldManager, manifold);
		}
	}
	
	@Override public GunShot createGunShot(Being origin, Vector2 dir){
		return new Flame(origin, dir, this);
	}
}
