// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 07/23/2014 22:10:47
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions.execution;

import jbt.execution.core.IContext;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.animation.GDXAnimationHandler;
import com.blastedstudios.gdxworld.world.animation.GDXAnimations;
import com.blastedstudios.gdxworld.world.quest.manifestation.IQuestManifestationExecutor;
import com.blastedstudios.ledge.ai.bt.TimeKeeper;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being.BodyPart;
import com.blastedstudios.ledge.world.being.INPCActionExecutor;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action GMTick. */
public class GMTick extends
jbt.execution.task.leaf.action.ExecutionAction {
	private static String HANDLER_NAME = "AnimationHandler",
			HANDLER_PATH = "data/world/npc/animation/garbageman.xml",
			TIME_MOVED_DIRECTION = "TimeMoved",
			CURRENT = "CurrentAnimation";
	private static final float STOMP_DISTANCE = Properties.getFloat("garbageman.stomp.distance", 6f);
	private static final long TOTAL_TIME_DIRECTION = Properties.getInt("garbageman.move.time", 7000);//ms

	/**
	 * Constructor. Constructs an instance of GMTick that is able to run a
	 * com.blastedstudios.ledge.ai.bt.actions.GMTick.
	 */
	public GMTick(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.actions.GMTick)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.actions.GMTick");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		final NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		final WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		self.aim(4f);
		getContext().setVariable(TIME_MOVED_DIRECTION, new TimeKeeper());
		FileHandle handle = FileUtil.find(HANDLER_PATH);
		ISerializer serializer = FileUtil.getSerializer(handle);
		try {
			final GDXAnimationHandler handler = new GDXAnimationHandler((GDXAnimations) serializer.load(handle),
					createQuestExecutor(self, world.getWorld()));
			handler.setActive(true);//defer until we see player?
			getContext().setVariable(HANDLER_NAME, handler);
			getContext().setVariable(INPCActionExecutor.EXECUTE_CONTEXT_NAME,
					createActionExecutor(self, world, handler, getContext()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.SUCCESS;
	}
	
	private static IQuestManifestationExecutor createQuestExecutor(final NPC self, final World world){
		return new IQuestManifestationExecutor() {
			@Override public World getWorld() {
				return world;
			}
			@Override public Body getPhysicsObject(String name) {
				return self.getRagdoll().getBodyPart(BodyPart.valueOf(name));
			}
			@Override public Joint getPhysicsJoint(String name) {
				return null;
			}
		};
	}
	
	private static INPCActionExecutor createActionExecutor(final NPC self, final WorldManager world, 
			final GDXAnimationHandler handler, final IContext context){
		return new INPCActionExecutor() {
			@Override public Status execute(String identifier) {
				if(identifier.equals("sword")){
					// check if we should start if, if not already started
					float distance = world.getPlayer().getPosition().dst(self.getPosition());
					if(!handler.getCurrentAnimation().getName().equals("sword")){
						if(distance < Properties.getFloat("garbageman.sword.distance.max", 10f) && distance > STOMP_DISTANCE){
							handler.applyCurrentAnimation(handler.getAnimations().getAnimation("sword"), 0);
							return Status.RUNNING;
						}else
							return Status.FAILURE;
					}
					handler.render(Gdx.graphics.getDeltaTime());
					return handler.getCurrentAnimation().getName().equals("sword") ? Status.RUNNING : Status.SUCCESS;
				}else if(identifier.equals("stomp")){
					// check if we should start if, if not already started
					float distance = world.getPlayer().getPosition().dst(self.getPosition());
					if(!handler.getCurrentAnimation().getName().equals("stomp")){
						if(distance < STOMP_DISTANCE){
							handler.applyCurrentAnimation(handler.getAnimations().getAnimation("stomp"), 0);
							return Status.RUNNING;
						}else
							return Status.FAILURE;
					}
					handler.render(Gdx.graphics.getDeltaTime());
					return handler.getCurrentAnimation().getName().equals("stomp") ? Status.RUNNING : Status.SUCCESS;
				}else{ //GMMove
					TimeKeeper timeKeeper = (TimeKeeper) context.getVariable(TIME_MOVED_DIRECTION);
					float dt = timeKeeper.dt()/1000f;
					if(!timeKeeper.isMarked() || timeKeeper.markDt() > TOTAL_TIME_DIRECTION){
						String currentAnimation = (String) context.getVariable(CURRENT);
						boolean isRight = currentAnimation == null || currentAnimation.equals("walkLeft");
						String newAnimation = isRight ? "walkRight" : "walkLeft";
						context.setVariable(CURRENT, newAnimation);
						handler.applyCurrentAnimation(handler.getAnimations().getAnimation(newAnimation), 0f);
						timeKeeper.mark();
					}
					//if we got here, the last node in the tree, we want to update handler and return success
					handler.render(dt);
					return Status.SUCCESS;
				}
			}
		};
	}

	protected void internalTerminate() {}
	protected void restoreState(jbt.execution.core.ITaskState state) {}

	protected jbt.execution.core.ITaskState storeState() {
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		return null;
	}
}