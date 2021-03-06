package com.blastedstudios.ledge.ui.gameplay;

import java.util.LinkedList;
import java.util.List;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.beingspawn.BeingSpawnManifestation;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.dialog.DialogManifestation;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.drawable.Drawable;
import com.blastedstudios.gdxworld.ui.drawable.DrawableSorter;
import com.blastedstudios.gdxworld.ui.drawable.ShapeDrawable;
import com.blastedstudios.gdxworld.ui.drawable.TiledMeshRendererDrawable;
import com.blastedstudios.gdxworld.ui.leveleditor.LevelEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.GDXGameFade;
import com.blastedstudios.gdxworld.util.GDXGameFade.IPopListener;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.TiledMeshRenderer;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXParticle;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.gdxworld.world.quest.GDXQuest;
import com.blastedstudios.gdxworld.world.quest.QuestStatus;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.ledge.input.ActionEnum;
import com.blastedstudios.ledge.plugin.level.ILevelCompletedListener;
import com.blastedstudios.ledge.ui.LedgeScreen;
import com.blastedstudios.ledge.ui.drawable.ParticleManagerDrawable;
import com.blastedstudios.ledge.ui.drawable.WorldManagerDrawable;
import com.blastedstudios.ledge.ui.gameplay.console.ConsoleWindow;
import com.blastedstudios.ledge.ui.gameplay.hud.HUD;
import com.blastedstudios.ledge.ui.gameplay.inventory.InventoryWindow;
import com.blastedstudios.ledge.ui.gameplay.particles.ParticleManager;
import com.blastedstudios.ledge.ui.loading.GameplayLoadingWindowExecutor;
import com.blastedstudios.ledge.ui.loading.LoadingWindow;
import com.blastedstudios.ledge.util.ui.LedgeWindow;
import com.blastedstudios.ledge.world.DialogBubble;
import com.blastedstudios.ledge.world.DialogManager;
import com.blastedstudios.ledge.world.DialogManager.DialogStruct;
import com.blastedstudios.ledge.world.QuestManifestationExecutor;
import com.blastedstudios.ledge.world.QuestTriggerInformationProvider;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.Player;
import com.blastedstudios.ledge.world.being.component.IComponent;

public class GameplayScreen extends LedgeScreen {
	private final DialogManager dialogManager;
	private final DialogBubble dialogBubble;
	private final ParticleManager particleManager;
	private final HUD hud;
	private OrthographicCamera camera;
	private WorldManager worldManager;
	private LedgeWindow characterWindow, inventoryWindow, vendorWindow, backWindow;
	private ConsoleWindow consoleWindow;
	private final Box2DDebugRenderer renderer;
	private final GDXRenderer gdxRenderer;
	private final GDXLevel level;
	private RayHandler rayHandler;
	private final GDXWorld world;
	private final FileHandle selectedFile;
	private Vector2 touchedDirection;
	private final TiledMeshRenderer tiledMeshRenderer;
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final AssetManager sharedAssets, assetManager;
	private final LinkedList<Drawable> drawables;
	private Table tintTable;
	
	public GameplayScreen(GDXGame game, Player player, GDXLevel level, GDXWorld world,
			FileHandle selectedFile, final GDXRenderer gdxRenderer, AssetManager sharedAssets,
			AssetManager assetManager){
		super(game);
		this.level = level;
		this.world = world;
		this.selectedFile = selectedFile;
		this.gdxRenderer = gdxRenderer;
		this.sharedAssets = sharedAssets;
		this.assetManager = assetManager;
		hud = new HUD(skin, player);
		dialogManager = new DialogManager(skin);
		dialogBubble = new DialogBubble(sharedAssets, skin);
		particleManager = new ParticleManager();
		for(GDXParticle particle : level.getParticles())
			particleManager.addParticle(particle);
		worldManager = new WorldManager(player, level, sharedAssets, new IGameplayListener() {
			@Override public void npcAdded(NPC npc) {
				hud.npcAdded(npc);
			}
		});
		player.getQuestManager().initialize(new QuestTriggerInformationProvider(this, worldManager), 
				new QuestManifestationExecutor(this, worldManager));
		player.getQuestManager().setCurrentLevel(level);
		player.getQuestManager().tick(0f);//to get "start" quest to set respawn location
		if(worldManager.getRespawnLocation() == null && Properties.getBool("player.spawn.onnewlevel.loadsavedspawn", false)){
			List<QuestStatus> statuses = player.getQuestManager().getQuestStatuses(level);
			worldManager.setRespawnLocation(getSavedSpawn(statuses));
		}
		if(worldManager.getRespawnLocation() != null && !player.isSpawned())
			worldManager.respawnPlayer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = Properties.getFloat("gameplay.camera.zoom", .02f);
		renderer = new Box2DDebugRenderer();
		rayHandler = worldManager.getCreateLevelStruct().lights.rayHandler;
		tiledMeshRenderer = new TiledMeshRenderer(gdxRenderer, level.getPolygons());
		if(sharedAssets.getQueuedAssets() > 0)
			Log.log("GameplayScreen.<init>", "Shared assets finishing loading");
		sharedAssets.finishLoading();//unsure if queued == 0 means its done... this verifies
		registerInput();
		drawables = gdxRenderer.generateDrawables(assetManager, spriteBatch, level, camera, 
				worldManager.getCreateLevelStruct().bodies.entrySet());
		drawables.sort(new DrawableSorter());
		int startIndex = drawables.size() - 1;
		for(int i = 1; i < drawables.size(); i++)
			if(drawables.get(i-1) instanceof ShapeDrawable && !(drawables.get(i) instanceof ShapeDrawable))
				startIndex = i;
		drawables.add(startIndex, new ParticleManagerDrawable(particleManager, worldManager));
		drawables.add(startIndex, new WorldManagerDrawable(worldManager));
		drawables.add(startIndex, new TiledMeshRendererDrawable(tiledMeshRenderer));
	}
	
	private void registerInput(){
		register(ActionEnum.BACK, new AbstractInputHandler() {
			public void down(){
				handlePause();
			}
		});
		register(ActionEnum.CROUCH, new AbstractInputHandler() {
			public void down(){
				worldManager.setDesireFixedRotation(false);
			}
			public void up(){
				worldManager.setDesireFixedRotation(true);
			}
		});
		register(ActionEnum.RELOAD, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable())
					worldManager.getPlayer().setReloading(true);
			}
		});
		register(ActionEnum.INVENTORY, new AbstractInputHandler() {
			public void down(){
				handlePause();
			}
		});
		register(ActionEnum.LEFT, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable())
					worldManager.getPlayer().setMoveLeft(true);
			}
			public void up(){
				worldManager.getPlayer().setMoveLeft(false);
			}
		});
		register(ActionEnum.RIGHT, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable())
					worldManager.getPlayer().setMoveRight(true);
			}
			public void up(){
				worldManager.getPlayer().setMoveRight(false);
			}
		});
		register(ActionEnum.UP, new AbstractInputHandler() {
			public void down(){
				if(!worldManager.isPause() && worldManager.isInputEnable())
					worldManager.getPlayer().setJump(true);
			}
			public void up(){
				worldManager.getPlayer().setJump(false);
			}
		});
		register(ActionEnum.CONSOLE, new AbstractInputHandler() {
			public void down(){
				if(Properties.getBool("debug.commands")){
					if(consoleWindow == null){
						EventListener listener = new EventListener() {
							@Override public boolean handle(Event event) {
								consoleWindow.remove();
								consoleWindow = null;
								return false;
							}
						};
						stage.addActor(consoleWindow = new ConsoleWindow(skin, worldManager, GameplayScreen.this, listener));
						consoleWindow.setX(Gdx.graphics.getWidth()/2 - consoleWindow.getWidth()/2);
					}else{
						consoleWindow.remove();
						consoleWindow = null;
					}
				}
			}
		});
		register(ActionEnum.ACTION, new AbstractInputHandler() {
			public void down(){
				if(debugCommandEnabled()){
					game.pushScreen(new LevelEditorScreen(game, world, selectedFile, level, assetManager));
					Log.log("GameplayScreen.render", "Edit mode entered");
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
									worldManager.getWorld(), listener, worldManager.getSharedAssets()));
							stage.addActor(inventoryWindow = new InventoryWindow(skin, 
									worldManager.getPlayer(), listener, worldManager.getSharedAssets(), stage, true));
							worldManager.pause(true);
						}
					}
				}else
					cleanCharacterWindows();
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
				}else if(worldManager.getPlayer().isDead() && worldManager.getRespawnLocation() != null)
					worldManager.respawnPlayer();
			}
		});
	}
	
	private void handlePause(){
		if(consoleWindow == null && worldManager.isInputEnable()){
			if(characterWindow == null){
				cleanCharacterWindows();//just to be safe
				ChangeListener listener = new ChangeListener() {
					@Override public void changed(ChangeEvent event, Actor actor) {
						cleanCharacterWindows();
					}
				};
				stage.addActor(characterWindow = new CharacterWindow(skin, worldManager.getPlayer(), listener));
				stage.addActor(backWindow = new BackWindow(skin, this));
				stage.addActor(inventoryWindow = new InventoryWindow(skin, 
						worldManager.getPlayer(), listener, worldManager.getSharedAssets(), stage, false));
				worldManager.pause(true);
			}else
				cleanCharacterWindows();
		}
	}

	@Override public void render(float delta) {
		super.render(delta);
		//if disposed this up in super's render, need to drop it before rendering scene
		if(assetManager.getLoadedAssets() == 0)
			return;
		//if player is not spawned then we are in a cutscene, if input isn't enabled we shouldn't update camera
		if(worldManager.getPlayer().isSpawned() && worldManager.isPlayerTrack())
			camera.position.set(worldManager.getPlayer().getPosition().x, worldManager.getPlayer().getPosition().y, 0);
		camera.update();
		rayHandler.setCombinedMatrix(camera);

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for(Drawable drawable : drawables)
			drawable.render(delta, assetManager, spriteBatch, camera, gdxRenderer);
		spriteBatch.end();
		
		if(Properties.getBool("lighting.draw", true))
			rayHandler.updateAndRender();
		if(Properties.getBool("debug.draw"))
			renderer.render(worldManager.getWorld(), camera.combined);
		dialogBubble.render(sharedAssets, worldManager, dialogManager, camera);
		dialogManager.render(assetManager);
		stage.draw();
		if(worldManager.getPlayer().isSpawned())
			hud.render();
		worldManager.getPlayer().getQuestManager().tick(delta);
		
		int x = Gdx.input.getX(), y = Gdx.input.getY();
		if(!worldManager.isPause() && worldManager.isInputEnable() && Gdx.input.isTouched() && 
				(inventoryWindow == null || !inventoryWindow.contains(x, y)) &&
				(characterWindow == null || !characterWindow.contains(x, y)) &&
				(backWindow == null || !backWindow.contains(x, y)) &&
				(vendorWindow == null || !vendorWindow.contains(x, y)) && 
				(consoleWindow == null || !consoleWindow.contains(x, y)))
			worldManager.getPlayer().attack(touchedDirection, worldManager);
	}
	
	public void levelComplete(final boolean success, final String nextLevelName){
		for(ILevelCompletedListener listener : PluginUtil.getPlugins(ILevelCompletedListener.class))
			listener.levelComplete(success, nextLevelName, worldManager, level);
		GDXGameFade.fadeOutPopScreen(game, new IPopListener() {
			@Override public void screenPopped() {
				dispose();
				if(success && nextLevelName != null && !nextLevelName.equals(""))
					((AbstractScreen)game.getScreen()).getStage().addActor(new LoadingWindow(skin, 
							new GameplayLoadingWindowExecutor(game, worldManager.getPlayer(), 
									world.getLevel(nextLevelName), 
									world, selectedFile, gdxRenderer, sharedAssets)));
			}
		});
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
	
	@Override public boolean touchUp(int x, int y, int x1, int y1){
		worldManager.getPlayer().touchUp(x, y, x1, y1);
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
					Log.log("GameplayScreen.render","New gun selected: " + worldManager.getPlayer().getGuns().get(i));
				}
		switch(key){
		case Keys.F2:
			if(worldManager.getPlayer().isDead() && worldManager.getRespawnLocation() != null)
				worldManager.respawnPlayer();
			break;
		case Keys.F6:
			if(debugCommandEnabled()){
				Properties.set("debug.draw", ""+!Properties.getBool("debug.draw"));
				Log.log("GameplayScreen.render", "debug.draw: " + Properties.getBool("debug.draw"));
			}
			break;
		case Keys.F9:
			if(debugCommandEnabled())
				worldManager.getDropManager().generateDrop(worldManager.getPlayer(), worldManager.getWorld());
			break;
		case Keys.F10:
			if(debugCommandEnabled()){
				Vector2 position = worldManager.getPlayer().getPosition();
				worldManager.spawnNPC(level.getClosestNPC(position.x, position.y), worldManager.getAiWorld());
			}
			break;
		case Keys.F12:
			if(debugCommandEnabled()){
				levelComplete(true, "");
				Log.log("Gameplayscreen.keyDown", "beat level cheater");
			}
			break;
		}
		for(IComponent component : worldManager.getPlayer().getListeners())
			component.keyDown(key, worldManager);
		return super.keyDown(key);
	}
	
	private void cleanCharacterWindows(){
		for(LedgeWindow window : new LedgeWindow[]{characterWindow, inventoryWindow, vendorWindow, backWindow})
			if(window != null)
				window.remove();
		characterWindow = null;
		inventoryWindow = null;
		backWindow = null;
		vendorWindow = null;
		worldManager.pause(false);
	}
	
	private boolean debugCommandEnabled(){
		return Properties.getBool("debug.commands") && ActionEnum.MODIFIER.isPressed();
	}
	
	@Override public boolean keyUp(int key) {
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
					if(manifestation.getNpc().getName().equals("player"))
						return manifestation.getNpc().getCoordinates();
				}
		return null;
	}
	
	public Camera getCamera(){
		return camera;
	}
	
	@Override public void dispose(){
		super.dispose();
		assetManager.dispose();
		worldManager.getPlayer().dispose(worldManager.getWorld());
		worldManager.getWorld().dispose();
	}

	public void applyTintTable(Table tintTable) {
		if(this.tintTable != null)
			this.tintTable.remove();
		stage.addActor(this.tintTable = tintTable);
		Log.log("GameplayScreen.applyTintTable", "New table applied at " + System.currentTimeMillis());
	}

	public interface IGameplayListener{
		void npcAdded(NPC npc);
	}
}
