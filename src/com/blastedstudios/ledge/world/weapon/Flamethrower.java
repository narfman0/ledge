package com.blastedstudios.ledge.world.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.weapon.shot.Flame;
import com.blastedstudios.ledge.world.weapon.shot.GunShot;

public class Flamethrower extends Gun {
	private static final long serialVersionUID = 1L;
	private long timeToRemove = -1l;

	@Override public String toString(){
		return "[Flamethrower name:" + name + " dmg: " + getDamage() + "]";
	}
	
	@Override public void handleContact(Body body, GunShot gunshot, WorldManager worldManager, WorldManifold manifold){
		if(timeToRemove == -1l)
			timeToRemove = System.currentTimeMillis() + Properties.getInt("flame.contact.duration", 1000);
		if(timeToRemove != -1l && System.currentTimeMillis() >= timeToRemove){
			Flame flame = (Flame) gunshot;
			worldManager.transferParticles(flame.flame);
			super.handleContact(body, gunshot, worldManager, manifold);
		}
	}
	
	@Override public GunShot createGunShot(Being origin, Vector2 dir){
		return new Flame(origin, dir, this);
	}
}
