// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 07/23/2014 22:10:47
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions.execution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.world.animation.GDXAnimationHandler;
import com.blastedstudios.gdxworld.world.animation.GDXAnimations;
import com.blastedstudios.gdxworld.world.quest.manifestation.IQuestManifestationExecutor;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being.BodyPart;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action GMTick. */
public class GMTick extends
		jbt.execution.task.leaf.action.ExecutionAction {
	public static String HANDLER_NAME = "AnimationHandler",
			HANDLER_PATH = "data/world/npc/animation/garbageman.xml";

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
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		if(getContext().getVariable(HANDLER_NAME) == null){
			final NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
			final WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
			FileHandle handle = FileUtil.find(HANDLER_PATH);
			GDXAnimationHandler handler = null;
			IQuestManifestationExecutor executor = new IQuestManifestationExecutor() {
				@Override public World getWorld() {
					return world.getWorld();
				}
				@Override public Body getPhysicsObject(String name) {
					return self.getRagdoll().getBodyPart(BodyPart.valueOf(name));
				}
				@Override public Joint getPhysicsJoint(String name) {
					return null;
				}
			};
			ISerializer serializer = FileUtil.getSerializer(handle);
			try {
				handler = new GDXAnimationHandler((GDXAnimations) serializer.load(handle), executor);
			} catch (Exception e) {
				e.printStackTrace();
			}
			getContext().setVariable(HANDLER_NAME, handler);
			handler.setActive(true);
		}else{
			GDXAnimationHandler handler = (GDXAnimationHandler) getContext().getVariable(HANDLER_NAME);
			handler.render(Gdx.graphics.getRawDeltaTime());
		}
		//Need to initialize to carry on with next nodes, though don't want to
		//keep running, so fail always. Lame but effective, TODO think of more 
		//elegant way later.
		return jbt.execution.core.ExecutionTask.Status.FAILURE;
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