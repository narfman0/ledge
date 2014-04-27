// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 01/25/2013 21:35:07
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.conditions.execution;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionCondition class created from MMPM condition IsDead. */
public class IsDead extends
		jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of IsDead that is able to run a
	 * com.blastedstudios.ledge.ai.bt.conditions.IsDead.
	 */
	public IsDead(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.conditions.IsDead)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.conditions.IsDead");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		Gdx.app.debug(this.getClass().getCanonicalName(), "ticked");
		Being self = (Being) getContext().getVariable(AIFieldEnum.SELF.name());
		return self.isDead() ? Status.SUCCESS : Status.FAILURE;
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