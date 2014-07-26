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
import com.blastedstudios.gdxworld.world.animation.GDXAnimationHandler;

/** ExecutionAction class created from MMPM action GMMove. */
public class GMMove extends jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of GMMove that is able to run a
	 * com.blastedstudios.ledge.ai.bt.actions.GMMove.
	 */
	public GMMove(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.actions.GMMove)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.actions.GMMove");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		GDXAnimationHandler handler = (GDXAnimationHandler) getContext().getVariable(GMTick.HANDLER_NAME);
		//TODO figure out which direction, time it out
		handler.applyCurrentAnimation(handler.getAnimations().getAnimation("walkLeft"), 0);
		return jbt.execution.core.ExecutionTask.Status.SUCCESS;
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