package com.blastedstudios.ledge.ui.gameplay;

import java.io.File;
import java.util.List;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.beingspawn.BeingSpawnManifestation;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.dialog.DialogManifestation;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.AbstractWindow;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.leveleditor.LevelEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.TiledMeshRenderer;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.gdxworld.world.quest.GDXQuest;
import com.blastedstudios.gdxworld.world.quest.QuestStatus;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.ui.gameplay.hud.HUD;
import com.blastedstudios.ledge.ui.gameplay.inventory.InventoryWindow;
import com.blastedstudios.ledge.ui.gameplay.particles.ParticleManager;
import com.blastedstudios.ledge.util.SaveHelper;
import com.blastedstudios.ledge.world.DialogManager;
import com.blastedstudios.ledge.world.DialogManager.DialogStruct;
import com.blastedstudios.ledge.world.QuestManifestationExecutor;
import com.blastedstudios.ledge.world.QuestTriggerInformationProvider;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.Player;
import com.blastedstudios.ledge.world.being.component.IComponent;

public class GameplayScreen extends AbstractScreen {
	private final DialogManager dialogManager;
	private final ParticleManager particleManager;
	private HUD hud;
	private OrthographicCamera camera;
	private WorldManager worldManager;
	private AbstractWindow characterWindow, inventoryWindow, vendorWindow;
	private ConsoleWindow consoleWindow;
	private final Box2DDebugRenderer renderer;
	private final GDXRenderer gdxRenderer;
	private final GDXLevel level;
	private RayHandler rayHandler;
	private final GDXWorld world;
	private final File selectedFile;
	private Vector2 touchedDirection;
	private final TiledMeshRenderer tiledMeshRenderer;
	
	public GameplayScreen(GDXGame game, Player player, GDXLevel level, GDXWorld world, File selectedFile, final GDXRenderer gdxRenderer){
		super(game, Properties.get("screen.skin","data/ui/uiskinGame.json"));
		this.level = level;
		this.world = world;
		this.selectedFile = selectedFile;
		this.gdxRenderer = gdxRenderer;
		hud = new HUD(skin, player);
		dialogManager = new DialogManager(skin);
		particleManager = new ParticleManager();
		for(GDXParticle particle : level.getParticles())
			particleManager.addParticle(particle);
		worldManager = new WorldManager(player, level);
		player.getQuestManager().initialize(new QuestTriggerInformationProvider(this, worldManager), 
				new QuestManifestationExecutor(this, worldManager));
		player.getQuestManager().setCurrentLevel(level);
		player.getQuestManager().tick();//to get "start" quest to set respawn location
		if(worldManager.getRespawnLocation() == null && Properties.getBool("player.spawn.onnewlevel.loadsavedspawn", false)){
			List<QuestStatus> statuses = player.getQuestManager().getQuestStatuses(level);
			worldManager.setRespawnLocation(getSavedSpawn(statuses));
		}
		if(worldManager.getRespawnLocation() != null)
			worldManager.respawnPlayer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = Properties.getFloat("gameplay.camera.zoom", .02f);
		renderer = new Box2DDebugRenderer();
		rayHandler = worldManager.getCreateLevelStruct().lights.rayHandler;
		tiledMeshRenderer = new TiledMeshRenderer(gdxRenderer, level.getPolygons());
	}

	@Override public void render(float delta) {
		super.render(delta);
		//if player is not spawned then we are in a cutscene, if input isn't enabled we shouldn't update camera
		if(worldManager.getPlayer().isSpawned() && worldManager.isInputEnable())
			camera.position.set(worldManager.getPlayer().getPosition().x, worldManager.getPlayer().getPosition().y, 0);
		camera.update();
		rayHandler.setCombinedMatrix(camera.combined);

		gdxRenderer.render(level, camera, worldManager.getCreateLevelStruct().bodies.entrySet());
		tiledMeshRenderer.render(camera);
		worldManager.render(delta, gdxRenderer, camera);
		particleManager.render(delta, camera, worldManager);
		if(Properties.getBool("lighting.draw", true))
			rayHandler.updateAndRender();
		if(Properties.getBool("debug.draw"))
			renderer.render(worldManager.getWorld(), camera.combined);
		dialogManager.render(gdxRenderer);
		stage.draw();
		if(worldManager.getPlayer().isSpawned())
			hud.render();
		worldManager.getPlayer().getQuestManager().tick();
		
		int x = Gdx.input.getX(), y = Gdx.input.getY();
		if(!worldManager.isPause() && worldManager.isInputEnable() && Gdx.input.isTouched() && 
				(inventoryWindow == null || !inventoryWindow.contains(x, y)) &&
				(characterWindow == null || !characterWindow.contains(x, y)) &&
				(vendorWindow == null || !vendorWindow.contains(x, y)))
			worldManager.getPlayer().attack(touchedDirection, worldManager);
	}
	
	public void levelComplete(boolean success){
		SaveHelper.save(worldManager.getPlayer());
		worldManager.getPlayer().clean(worldManager.getWorld());
		//cant always setLevelCompleted(...,success), because if player 
		//previously beats a level then replays and loses, we don't want to 
		//make it so he can't play any later levels again
		if(success) 
			worldManager.getPlayer().setLevelCompleted(level.getName(), true);
		game.popScreen();
	}

	public boolean isAction() {
		return Gdx.input.isKeyPressed(Keys.ENTER) || Gdx.input.isKeyPressed(Keys.E);
	}

	public DialogManager getDialogManager() {
		return dialogManager;
	}

	@Override public boolean touchDown(int x, int y, int x1, int y1) {
		mouseMovedHandler(x, y);
		return false;
	}

	@Override public boolean touchDragged(int x, int y, int ptr) {
		mouseMovedHandler(x, y);
		return false;
	}
	
	@Override public boolean mouseMoved(int x, int y){
		mouseMovedHandler(x, y);
		return false;
	}

	private void mouseMovedHandler(int x, int y) {
		if(!worldManager.isPause() && worldManager.isInputEnable()){
			float angle = (float)Math.atan2(Gdx.graphics.getHeight()/2 - y, x - Gdx.graphics.getWidth()/2);
			touchedDirection = new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
			worldManager.getPlayer().aim(angle);
		}
	}
	
	@Override public boolean scrolled(int amount) {
		if(!worldManager.isPause() && worldManager.isInputEnable()){
			int max = Math.min(Being.MAX_GUNS, worldManager.getPlayer().getGuns().size());
			if(max > 0){
				int gun = (worldManager.getPlayer().getCurrentGun() + amount + max) % max;
				worldManager.changePlayerWeapon(gun);
			}
		}
		return false;
	}
	
	@Override public boolean keyDown(int key) {
		//switch weapon
		for(int i=0; i<Being.MAX_GUNS; i++)
			if(key == Keys.NUM_1 + i && consoleWindow == null)
				if(worldManager.getPlayer().getGuns().size() > i && i != worldManager.getPlayer().getCurrentGun()){
					worldManager.changePlayerWeapon(i);
					Gdx.app.log("GameplayScreen.render","New gun selected: " + worldManager.getPlayer().getGuns().get(i));
				}
		switch(key){
		case Keys.ESCAPE:
			SaveHelper.save(worldManager.getPlayer());
			worldManager.getPlayer().clean(worldManager.getWorld());
			game.popScreen();
			break;
		case Keys.C:
			worldManager.getPlayer().setFixedRotation(false);
			break;
		case Keys.R:
			if(!worldManager.isPause() && worldManager.isInputEnable())
				worldManager.getPlayer().setReloading(true);
			break;
		case Keys.I:
			if(consoleWindow == null && !worldManager.isPause() && worldManager.isInputEnable()){
				if(characterWindow == null){
					cleanCharacterWindows();//just to be safe
					ChangeListener listener = new ChangeListener() {
						@Override public void changed(ChangeEvent event, Actor actor) {
							cleanCharacterWindows();
						}
					};
					stage.addActor(characterWindow = new CharacterWindow(skin, worldManager.getPlayer(), listener));
					stage.addActor(inventoryWindow = new InventoryWindow(skin, 
							worldManager.getPlayer(), listener, gdxRenderer, stage, false));
					worldManager.pause(true);
				}else
					cleanCharacterWindows();
			}
			break;
		case Keys.A:
			if(!worldManager.isPause() && worldManager.isInputEnable())
				worldManager.getPlayer().setMoveLeft(true);
			break;
		case Keys.D:
			if(!worldManager.isPause() && worldManager.isInputEnable())
				worldManager.getPlayer().setMoveRight(true);
			break;
		case Keys.E:
			if(debugCommandEnabled()){
				game.pushScreen(new LevelEditorScreen(game, world, selectedFile, level));
				Gdx.app.log("GameplayScreen.render", "Edit mode entered");
			}
			if(vendorWindow == null){
				if(!worldManager.isPause() && worldManager.isInputEnable()){
					NPC npc = worldManager.findVendor();
					if(npc != null){
						cleanCharacterWindows();//just to be safe
						ChangeListener listener = new ChangeListener() {
							@Override public void changed(ChangeEvent event, Actor actor) {
								cleanCharacterWindows();
							}
						};
						stage.addActor(vendorWindow = new VendorWindow(skin, npc, stage, worldManager.getPlayer(),
								worldManager.getWorld(), listener, gdxRenderer));
						stage.addActor(inventoryWindow = new InventoryWindow(skin, 
								worldManager.getPlayer(), listener, gdxRenderer, stage, true));
						worldManager.pause(true);
					}
				}
			}else
				cleanCharacterWindows();
			break;
		case Keys.W:
			if(!worldManager.isPause() && worldManager.isInputEnable())
				worldManager.getPlayer().setJump(true);
			break;
		case Keys.ENTER:
			DialogStruct struct = dialogManager.poll();
			if(struct != null){
				//TODO kinda nasty hack, here... on the slow side and doesn't go through GDXQuestManager
				for(GDXQuest quest : level.getQuests())
					if(quest.getManifestation() instanceof DialogManifestation){
						String converted = DialogManager.splitRenderable(((DialogManifestation)quest.getManifestation()).getDialog(), DialogManager.DIALOG_WIDTH);
						if(converted.equals(struct.dialog)){
							worldManager.getPlayer().getQuestManager().setStatus(quest.getName(), CompletionEnum.COMPLETED);
							break;
						}
					}
			}else if(debugCommandEnabled()){
		 		if(consoleWindow == null){
		 			EventListener listener = new EventListener() {
						@Override public boolean handle(Event event) {
							consoleWindow.remove();
				 			consoleWindow = null;
							return false;
						}
					};
		 			stage.addActor(consoleWindow = new ConsoleWindow(worldManager, skin, listener));
					consoleWindow.setX(Gdx.graphics.getWidth()/2);
		 		}else{
		 			consoleWindow.execute();
		 			consoleWindow.remove();
		 			consoleWindow = null;
		 		}
			}
			break;
		case Keys.F2:
			if(worldManager.getPlayer().isDead() && worldManager.getRespawnLocation() != null)
				worldManager.respawnPlayer();
			break;
		case Keys.F6:
			if(debugCommandEnabled()){
				Properties.set("debug.draw", ""+!Properties.getBool("debug.draw"));
				Gdx.app.log("GameplayScreen.render", "debug.draw: " + Properties.getBool("debug.draw"));
			}
			break;
		case Keys.F9:
			if(debugCommandEnabled())
				worldManager.getDropManager().generateDrop(worldManager.getPlayer(), worldManager.getWorld());
			break;
		case Keys.F10:
			if(debugCommandEnabled()){
				Vector2 position = worldManager.getPlayer().getPosition();
				worldManager.spawnNPC(level, level.getClosestNPC(position.x, position.y), worldManager.getAiWorld());
			}
			break;
		case Keys.F12:
			if(debugCommandEnabled()){
				levelComplete(true);
				Gdx.app.log("Gameplayscreen.keyDown", "beat level cheater");
			}
			break;
		}
		for(IComponent component : worldManager.getPlayer().getListeners())
			component.keyDown(key, worldManager);
		return super.keyDown(key);
	}
	
	private void cleanCharacterWindows(){
		for(AbstractWindow window : new AbstractWindow[]{characterWindow,inventoryWindow,vendorWindow})
			if(window != null)
				window.remove();
		characterWindow = null;
		inventoryWindow = null;
		vendorWindow = null;
		worldManager.pause(false);
	}
	
	private boolean debugCommandEnabled(){
		return Properties.getBool("debug.commands") && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT);
	}
	
	@Override public boolean keyUp(int key) {
		switch(key){
		case Keys.A:
			worldManager.getPlayer().setMoveLeft(false);
			break;
		case Keys.D:
			worldManager.getPlayer().setMoveRight(false);
			break;
		case Keys.W:
			worldManager.getPlayer().setJump(false);
			break;
		case Keys.C:
			worldManager.getPlayer().setFixedRotation(true);
			break;
		}
		for(IComponent component : worldManager.getPlayer().getListeners())
			component.keyUp(key, worldManager);
		return super.keyUp(key);
	}

	public ParticleManager getParticleManager() {
		return particleManager;
	}

	/**
	 * Find starting location from last completed myself. Since completion list is
	 * ordered by completion of quest, it will be first "being spawn" quest.
	 * @return coordinates the player should spawn, given saves
	 */
	private Vector2 getSavedSpawn(List<QuestStatus> statuses){
		for(QuestStatus status : statuses)
			for(GDXQuest quest : level.getQuests())
				if(quest.getName().equals(status.questName) && status.getCompleted() == CompletionEnum.COMPLETED &&
						quest.getManifestation() instanceof BeingSpawnManifestation){
					BeingSpawnManifestation manifestation = ((BeingSpawnManifestation)quest.getManifestation());
					if(manifestation.getBeing().equals("player"))
						return manifestation.getCoordinates();
				}
		return null;
	}
	
	public Camera getCamera(){
		return camera;
	}
}
