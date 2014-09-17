package com.blastedstudios.ledge;

import java.util.ArrayList;
import java.util.EnumSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.ledge.ui.gameplay.hud.HUD;
import com.blastedstudios.ledge.world.Stats;
import com.blastedstudios.ledge.world.being.FactionEnum;
import com.blastedstudios.ledge.world.being.NPCData;
import com.blastedstudios.ledge.world.being.Player;
import com.blastedstudios.ledge.world.weapon.WeaponFactory;
import com.blastedstudios.ledge.world.weapon.Weapon;

public class LedgeCharacterViewer extends GDXGame {
	@Override public void create () {
		Properties.load(FileUtil.find("ledge.properties").read());
		pushScreen(new MainScreen(this));
	}

	public static void main (String[] argv) {
		new LwjglApplication(new LedgeCharacterViewer(), GDXWorldEditor.generateConfiguration(LedgeCharacterViewer.class.getSimpleName()));
	}

	private class MainScreen extends AbstractScreen {
		private final OrthographicCamera camera;
		private final Window window;
		private final Box2DDebugRenderer renderer;
		private final World world;
		private final Player player;
		private final SpriteBatch spriteBatch;
		private final HUD hud;

		public MainScreen(GDXGame game){
			super(game, "uiskin.json");
			spriteBatch = new SpriteBatch();
			spriteBatch.enableBlending();
			world = new World(new Vector2(0,-20), false);
			GDXLevel level = GDXWorld.load(Gdx.files.internal("data/world/animationViewer.xml")).getLevels().get(0);
			level.createLevel(world);
			player = new Player("name", new ArrayList<Weapon>(WeaponFactory.getStockWeapons()), new ArrayList<Weapon>(), 
					Stats.parseNPCData(NPCData.parse("player")), 0, 1, 1, 1, FactionEnum.FRIEND, EnumSet.of(FactionEnum.FRIEND), 
					Properties.get("character.texture.default", "dummy"));
			player.respawn(world, 0, 0);
			hud = new HUD(skin, player);
			camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.zoom = Properties.getFloat("gameplay.camera.zoom", .02f);
			renderer = new Box2DDebugRenderer();
			
			final TextField nameField = new TextField(Properties.get("character.texture.default"), skin);
			nameField.setMessageText("<character template name>");
			Button loadButton = new TextButton("Load", skin);
			loadButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					player.setResource(nameField.getName());
					player.respawn(world, 0, 0);
				}
			});
			final Button exitButton = new TextButton("Exit", skin);
			exitButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					Gdx.app.exit();
				}
			});
			window = new Window("Control", skin);
			window.add(nameField);
			window.add(loadButton);
			window.row();
			window.add(exitButton);
			window.pack();
			stage.addActor(window);
		}

		@Override public void render(float delta) {
			super.render(delta);
			camera.position.set(player.getPosition().x, player.getPosition().y, 0);
			camera.update();
			world.step(delta, 4, 4);
			player.setMoveLeft(Gdx.input.isKeyPressed(Keys.A));
			player.setMoveRight(Gdx.input.isKeyPressed(Keys.D));
			player.setJump(Gdx.input.isKeyPressed(Keys.W));
			if(Properties.getBool("debug.draw", true))
				renderer.render(world, camera.combined);
			try{
				spriteBatch.setProjectionMatrix(camera.combined);
				spriteBatch.begin();
				player.render(delta, world, spriteBatch, null, null, null);
				spriteBatch.end();
			}catch(Exception e){
				e.printStackTrace();
			}
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.D)){
				Properties.set("debug.draw", ""+!Properties.getBool("debug.draw"));
				Gdx.app.log("GameplayScreen.render", "debug.draw: " + Properties.getBool("debug.draw"));
			}
			if(Gdx.input.isKeyPressed(Keys.ESCAPE))
				window.setVisible(!window.isVisible());
			hud.render();
			stage.draw();
		}
		
		@Override public boolean scrolled(int amount) {
			camera.zoom = Math.max(.001f, camera.zoom + amount*camera.zoom/8f);
			return false;
		}
	}
}