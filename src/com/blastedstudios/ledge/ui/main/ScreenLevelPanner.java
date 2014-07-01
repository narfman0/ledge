package com.blastedstudios.ledge.ui.main;

import java.util.Random;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.TiledMeshRenderer;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXLevel.CreateLevelReturnStruct;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.gameplay.particles.ParticleManager;

public class ScreenLevelPanner {
	private static final int TIME_TO_TRANSITION = Properties.getInt("screen.panner.transition.time", 10000);
	private final GDXRenderer gdxRenderer;
	private final GDXWorld gdxWorld;
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private ParticleManager particleManager;
	private OrthographicCamera camera;
	private TiledMeshRenderer tiledMeshRenderer;
	private RayHandler rayHandler;
	private GDXLevel gdxLevel;
	private CreateLevelReturnStruct struct;
	private World world;
	private Random random;
	private long timeLastTransition;
	private float cameraMoveAmountX;

	public ScreenLevelPanner(GDXWorld gdxWorld, GDXRenderer gdxRenderer){
		this.gdxWorld = gdxWorld;
		this.gdxRenderer = gdxRenderer;
		random = new Random();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = Properties.getFloat("gameplay.camera.zoom", .02f);
		transitionLevel();
	}

	public void transitionLevel(){
		GDXLevel nextLevel;
		do{
			nextLevel = gdxWorld.getLevels().get(random.nextInt(gdxWorld.getLevels().size()));
		}while(nextLevel == gdxLevel && gdxWorld.getLevels().size() != 1);
		initializeLevel(nextLevel);
	}

	public void initializeLevel(GDXLevel level){
		this.gdxLevel = level;
		world = new World(new Vector2(0, -10), true);
		particleManager = new ParticleManager();
		for(GDXParticle particle : level.getParticles())
			particleManager.addParticle(particle);
		//worldManager = new WorldManager(player, level);
		struct = level.createLevel(world);
		rayHandler = struct.lights.rayHandler;
		tiledMeshRenderer = new TiledMeshRenderer(gdxRenderer, level.getPolygons());
		cameraMoveAmountX = random.nextFloat()*.1f - .05f;
	}

	public void render(){
		if(System.currentTimeMillis() - timeLastTransition > TIME_TO_TRANSITION){
			timeLastTransition = System.currentTimeMillis();
			transitionLevel();
		}
		
		camera.position.add(cameraMoveAmountX, 0, 0);
		camera.update();
		rayHandler.setCombinedMatrix(camera.combined);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		gdxRenderer.render(spriteBatch, gdxLevel, camera, struct.bodies.entrySet());
		spriteBatch.end();
		tiledMeshRenderer.render(camera);
		world.step(1f/30f, 10, 10);
		//worldManager.render(gdxRenderer, camera);
		//particleManager.render(camera, worldManager);
		if(Properties.getBool("lighting.draw", true))
			rayHandler.updateAndRender();
	}
}
