package com.blastedstudios.ledge.world.weapon;

import com.badlogic.gdx.Gdx;
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
			flame.setBodyType(BodyType.StaticBody);
		}
		Gdx.app.log("Gun.handleContact","Move this to gunshot ahndleContact");
	}
	
	@Override public GunShot createGunShot(Being origin, Vector2 dir){
		return new Flame(origin, dir, this);
	}
}
