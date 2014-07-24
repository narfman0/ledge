// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 07/23/2014 22:10:47
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions;

/** ModelAction class created from MMPM action GMTick. */
public class GMTick extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of GMTick. */
	public GMTick(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.ledge.ai.bt.actions.execution.GMTick
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.ledge.ai.bt.actions.execution.GMTick(
				this, executor, parent);
	}
}