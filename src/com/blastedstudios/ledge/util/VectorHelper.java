package com.blastedstudios.ledge.util;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class VectorHelper {
	public static Vector2 calculateAverage(List<Vector2> vectors){
		Vector2 average = new Vector2();
		for(Vector2 vector : vectors)
			average.add(vector);
		return average.div(vectors.size());
	}
	
	public static Vector2 calculateAverage(Vector2[] vectors){
		return calculateAverage(Arrays.asList(vectors));
	}
}
