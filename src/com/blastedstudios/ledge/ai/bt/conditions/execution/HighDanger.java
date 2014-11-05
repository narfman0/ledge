// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 01/25/2013 21:35:07
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.conditions.execution;

import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.ledge.util.VisibilityReturnStruct;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionCondition class created from MMPM condition HighDanger. */
public class HighDanger extends
		jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of HighDanger that is able to run a
	 * com.blastedstudios.ledge.ai.bt.conditions.HighDanger.
	 */
	public HighDanger(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.conditions.HighDanger)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.conditions.HighDanger");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Log.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		Log.debug(this.getClass().getCanonicalName(), "ticked");
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		VisibilityReturnStruct struct = world.isVisible((NPC) getContext().getVariable(AIFieldEnum.SELF.name()));
		if(struct.isVisible() && struct.enemyCount > 1){
			getContext().setVariable("HighDangerTarget", struct.target);
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}

	protected void internalTerminate() {
		/* TODO: this method's implementation must be completed. */
	}

	protected void restoreState(jbt.execution.core.ITaskState state) {
		/* TODO: this method's implementation must be completed. */
	}

	protected jbt.execution.core.ITaskState storeState() {
		/* TODO: this method's implementation must be completed. */
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		/* TODO: this method's implementation must be completed. */
		return null;
	}
}