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

/** ExecutionAction class created from MMPM action GMMove. */
public class GMMove extends jbt.execution.task.leaf.action.ExecutionAction {
	private static final float TOTAL_TIME_DIRECTION = Properties.getFloat("garbageman.move.time", 10f);
	private static final String TIME_MOVED_DIRECTION = "TimeMoved", CURRENT = "CurrentAnimation";
	
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
		if(getContext().getVariable(TIME_MOVED_DIRECTION) == null)
			getContext().setVariable(TIME_MOVED_DIRECTION, 0f);
		if(getContext().getVariable(CURRENT) == null)
			getContext().setVariable(CURRENT, "walkLeft");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		GDXAnimationHandler handler = (GDXAnimationHandler) getContext().getVariable(GMTick.HANDLER_NAME);
		float timeMoved = (float) getContext().getVariable(TIME_MOVED_DIRECTION) + Gdx.graphics.getDeltaTime();
		if(timeMoved > TOTAL_TIME_DIRECTION){
			getContext().setVariable(TIME_MOVED_DIRECTION, 0f);
			String currentAnimation = (String) getContext().getVariable(CURRENT), newAnimation;
			getContext().setVariable(CURRENT, newAnimation = currentAnimation.equals("walkLeft") ? "walkRight" : "walkLeft");
			handler.applyCurrentAnimation(handler.getAnimations().getAnimation(newAnimation), 0f);
			Gdx.app.log("GMMove.tick", "Swapped to animation: " + newAnimation);
		}else
			getContext().setVariable(TIME_MOVED_DIRECTION, timeMoved);
		//if we got here, the last node in the tree, we want to update handler and return success
		handler.render(Gdx.graphics.getDeltaTime());
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