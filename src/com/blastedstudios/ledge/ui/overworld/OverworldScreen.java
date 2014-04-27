package com.blastedstudios.ledge.ui.overworld;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.worldeditor.WorldEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.world.being.Player;

public class OverworldScreen extends AbstractScreen{
	private static final float OFFSET_SCALAR = Properties.getFloat("world.level.offset.scalar", 20);
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final Sprite levelSprite, levelSelectedSprite;
	private final Player player;
	private final OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	private LevelInformationWindow levelInfo;
	private final Vector2 upperLeft = new Vector2(), lowerRight = new Vector2();
	private final GDXWorld gdxWorld;
	private final File worldFile;
	private final GDXRenderer gdxRenderer;

	public OverworldScreen(GDXGame game, Player player, GDXWorld gdxWorld, File worldFile, GDXRenderer gdxRenderer){
		super(game, Properties.get("screen.skin","data/ui/uiskinGame.json"));
		this.player = player;
		this.gdxWorld = gdxWorld;
		this.worldFile = worldFile;
		this.gdxRenderer = gdxRenderer;
		Gdx.app.log("OverworldScreen.<init>", "Loaded world successfully");
		levelSprite = skin.getSprite("toggle-button");
		levelSelectedSprite = skin.getSprite("toggle-button-checked");
		for(GDXLevel level : gdxWorld.getLevels())
			if(level.getCoordinates().x < upperLeft.x)
				upperLeft.x = level.getCoordinates().x;
			else if(level.getCoordinates().y > upperLeft.y)
				upperLeft.y = level.getCoordinates().y;
			else if(level.getCoordinates().x > lowerRight.x)
				lowerRight.x = level.getCoordinates().x;
			else if(level.getCoordinates().y < lowerRight.y)
				lowerRight.y = level.getCoordinates().y;
		stage.addActor(levelInfo = new LevelInformationWindow(skin, 
				gdxWorld.getLevels().get(0), game, player, gdxWorld, worldFile, gdxRenderer));
	}

	@Override public void render(float delta){
		super.render(delta);
		camera.update();
		if(!Gdx.graphics.isGL20Available())
			camera.apply(Gdx.gl10);
		if(Gdx.input.isKeyPressed(Keys.UP) && upperLeft.y > camera.position.y)
			camera.position.y+=camera.zoom;
		if(Gdx.input.isKeyPressed(Keys.DOWN) && lowerRight.y < camera.position.y)
			camera.position.y-=camera.zoom;
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && lowerRight.x > camera.position.x)
			camera.position.x+=camera.zoom;
		if(Gdx.input.isKeyPressed(Keys.LEFT) && upperLeft.x < camera.position.x)
			camera.position.x-=camera.zoom;
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for(GDXLevel level : gdxWorld.getLevels()){
			if(player.isLevelAvailable(gdxWorld, level)){
				Sprite sprite = levelSprite;
				if(levelInfo != null && levelInfo.level == level)
					sprite = levelSelectedSprite;
				Vector2 position = level.getCoordinates().cpy().scl(OFFSET_SCALAR).
						sub(sprite.getWidth()/2, sprite.getHeight()/2);
				sprite.setPosition(position.x, position.y);
				sprite.draw(spriteBatch);
			}
		}
		spriteBatch.end();
		stage.draw();
	}

	@Override public boolean keyDown(int key) {
		switch(key){
		case Keys.ESCAPE:
			game.popScreen();
			break;
		case Keys.E:
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				game.pushScreen(new WorldEditorScreen(game, gdxWorld, worldFile));
		}
		return super.keyDown(key);
	}

	@Override public boolean touchDown(int x, int y, int ptr, int button) {
		super.touchDown(x, y, ptr, button);
		if(levelInfo == null || !levelInfo.contains(x, y)){
			Vector2 coordinates = GDXRenderer.toWorldCoordinates(camera, new Vector2(x,y));
			coordinates.div(OFFSET_SCALAR);
			GDXLevel level = gdxWorld.getClosestLevel(coordinates.x,coordinates.y);
			if(Math.abs(level.getCoordinates().x - coordinates.x) < levelSprite.getWidth()/2 &&
					Math.abs(level.getCoordinates().y - coordinates.y) < levelSprite.getHeight()/2 &&
					player.isLevelAvailable(gdxWorld, level)){
				if(levelInfo != null)
					levelInfo.remove();
				stage.addActor(levelInfo = new LevelInformationWindow(skin, 
						level, game, player, gdxWorld, worldFile, gdxRenderer));
			}
		}
		return false;
	}
}
