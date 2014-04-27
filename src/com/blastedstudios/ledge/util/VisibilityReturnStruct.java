package com.blastedstudios.ledge.util;

import com.blastedstudios.ledge.world.being.Being;

public class VisibilityReturnStruct {
	public final int enemyCount;
	public final Being closestEnemy;
	public final float[] target; 
	
	public VisibilityReturnStruct(int enemyCount, Being closestEnemy){
		this.enemyCount = enemyCount;
		this.closestEnemy = closestEnemy;
		if(closestEnemy != null)
			target = new float[]{closestEnemy.getPosition().x, closestEnemy.getPosition().y};
		else
			target = null;
	}
	
	public boolean isVisible(){
		return closestEnemy != null;
	}
}
