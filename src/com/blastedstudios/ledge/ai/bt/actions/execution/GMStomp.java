// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 07/23/2014 21:59:49
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions.execution;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.animation.GDXAnimationHandler;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action GMStomp. */
public class GMStomp extends jbt.execution.task.leaf.action.ExecutionAction {
	public static final float STOMP_DISTANCE = Properties.getFloat("garbageman.stomp.distance", 3f);
	private static final float STOMP_RATE = Properties.getFloat("garbageman.stomp.rate", 5f);
	private static final String LAST_STOMP_ATTACK = "last.stomp.attack";

	/**
	 * Constructor. Constructs an instance of GMStomp that is able to run a
	 * com.blastedstudios.ledge.ai.bt.actions.GMStomp.
	 */
	public GMStomp(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.actions.GMStomp)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.actions.GMStomp");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
		getContext().setVariable(LAST_STOMP_ATTACK, 0f);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		NPC npc = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		GDXAnimationHandler handler = (GDXAnimationHandler) getContext().getVariable(GMTick.HANDLER_NAME);
		float total = ((float)System.currentTimeMillis())/1000f;
		if(total - STOMP_RATE > (Float)getContext().getVariable(LAST_STOMP_ATTACK)){
			float distance = world.getPlayer().getPosition().dst(npc.getPosition());
			if(distance < STOMP_DISTANCE){
				handler.applyCurrentAnimation(handler.getAnimations().getAnimation("stomp"), 0);
				getContext().setVariable(LAST_STOMP_ATTACK, total);
			}
		}
		//TODO find out when hit occurs
		if(handler.getCurrentAnimation().getName().equals("stomp")){
			handler.render(Gdx.graphics.getDeltaTime());
			return jbt.execution.core.ExecutionTask.Status.RUNNING;
		}
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