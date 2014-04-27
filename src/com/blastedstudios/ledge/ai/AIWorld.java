package com.blastedstudios.ledge.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class AIWorld {
	private static final Logger logger = Logger.getLogger(AIWorld.class.getCanonicalName());
	private final SimpleWeightedGraph<Vector2, DefaultWeightedEdge> movementGraph;
	
	public AIWorld(World gameWorld){
		movementGraph = GraphGenerator.generateGraph(gameWorld);
		logger.info("Initialized graph with " +movementGraph.edgeSet().size() + " edges and " + 
				movementGraph.vertexSet().size() + " vertices");
	}
	
	/**
	 * @return closest valid runtime node (not to be used while building world)
	 */
	private Vector2 getClosestNode(float x, float y){
		if(movementGraph.containsVertex(new Vector2(x,y)))
			return new Vector2(x,y);
		for(float dst=1; dst<100; dst++)
			for(float angle=0; angle<Math.PI*2; angle+=Math.PI/12){
				Vector2 tmp = new Vector2(
						(int) (x + Math.cos(angle)*dst), 
						(int) (y + Math.sin(angle)*dst));
				if(movementGraph.containsVertex(tmp))
					return tmp;
			}
		return new Vector2(x,y);
	}
	
	public Queue<Vector2> getPathToPoint(Vector2 origin, Vector2 destination){
		LinkedList<Vector2> steps = new LinkedList<Vector2>();
		Vector2 originRounded = getClosestNode(Math.round(origin.x), Math.round(origin.y));
		Vector2 destinationRounded = getClosestNode(Math.round(destination.x),Math.round(destination.y));
		try{
			List<DefaultWeightedEdge> list = DijkstraShortestPath.findPathBetween(
					movementGraph, originRounded, destinationRounded);
			for(DefaultWeightedEdge edge : list)
				steps.add(movementGraph.getEdgeSource(edge));
			steps.add(movementGraph.getEdgeTarget(list.get(list.size()-1)));
		}catch(Exception e){
			logger.warning("Error pathfinding for origin="+originRounded+
					" destination="+destinationRounded+" with message: "+e.getMessage());
		}
		return steps;
	}
}
