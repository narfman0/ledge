// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/25/2014 15:29:55
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions;

/** ModelAction class created from MMPM action NearDeadTick. */
public class NearDeadTick extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of NearDeadTick. */
	public NearDeadTick(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.ledge.ai.bt.actions.execution.NearDeadTick
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.ledge.ai.bt.actions.execution.NearDeadTick(
				this, executor, parent);
	}
}