package com.blastedstudios.ledge.ai;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.physics.IntersectQueryCallback;

/**
 * Generates graph from world. This includes normal physics objects
 * and strategic points, to be loaded separately 
 */
public class GraphGenerator {
	private static final float 
		PATHING_LENGTH = Properties.getFloat("server.world.pathing.length"),
		WORLD_MAX_X = Properties.getFloat("world.dimensions.x.max"),
		WORLD_MIN_X = Properties.getFloat("world.dimensions.x.min"),
		WORLD_MIN_Y = Properties.getFloat("world.dimensions.y.min"),
		WORLD_MAX_Y = Properties.getFloat("world.dimensions.y.max");
	
	public static SimpleWeightedGraph<Vector2, DefaultWeightedEdge> generateGraph(World world){
		SimpleWeightedGraph<Vector2, DefaultWeightedEdge> graph = 
				new SimpleWeightedGraph<Vector2, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for(float y=WORLD_MIN_Y; y<WORLD_MAX_Y; y++)
			for(float x=WORLD_MIN_X; x<WORLD_MAX_X; x++){
				if(isValidNode(x,y,world)){
					graph.addVertex(new Vector2(x, y));
					if(x>WORLD_MIN_X && isValidNode(x-1,y,world)){
						DefaultWeightedEdge edge = graph.addEdge(new Vector2(x,y), new Vector2(x-1,y));
						if(isAirNode(x,y,world))
							graph.setEdgeWeight(edge, 2);
					}
					if(y>WORLD_MIN_Y && isValidNode(x,y-1,world)){
						DefaultWeightedEdge edge = graph.addEdge(new Vector2(x,y), new Vector2(x,y-1));
						if(isAirNode(x,y,world))
							graph.setEdgeWeight(edge, 2);
					}if(x > WORLD_MIN_X && y>WORLD_MIN_Y && isValidNode(x-1,y-1,world))
						graph.setEdgeWeight(graph.addEdge(new Vector2(x,y), new Vector2(x-1,y-1)), 
								isAirNode(x,y,world) ? 2.5f : 1.414f);
					if(x+1 < WORLD_MAX_X && y>WORLD_MIN_Y && isValidNode(x+1,y-1,world))
						graph.setEdgeWeight(graph.addEdge(new Vector2(x,y), new Vector2(x+1,y-1)), 
								isAirNode(x,y,world) ? 2.5f : 1.414f);
				}
			}
		return graph;
	}
	
	/**
	 * For world building only (when creating nodes)
	 * @return if that x/y intersects with physical objects loaded from json
	 */
	private static boolean isValidNode(float x, float y, World world){
		IntersectQueryCallback closeCallback = new IntersectQueryCallback(),
				insideFixtureCallback = new IntersectQueryCallback();
		world.QueryAABB(closeCallback, x-PATHING_LENGTH, y-PATHING_LENGTH, x+PATHING_LENGTH, y);
		world.QueryAABB(insideFixtureCallback, x-.01f, y-.01f, x+.01f, y+.01f);
		return closeCallback.called && !insideFixtureCallback.called;
	}

	private static boolean isAirNode(float x, float y, World world){
		IntersectQueryCallback closeCallback = new IntersectQueryCallback(),
				farCallback = new IntersectQueryCallback();
		world.QueryAABB(closeCallback, x-1, y-1, x+1, y);
		world.QueryAABB(farCallback, x-PATHING_LENGTH, y-PATHING_LENGTH, x+PATHING_LENGTH, y);
		return farCallback.called && !closeCallback.called;
	}
}
