// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/19/2014 13:46:12
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions;

/** ModelAction class created from MMPM action ForestTick. */
public class ForestTick extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of ForestTick. */
	public ForestTick(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.ledge.ai.bt.actions.execution.ForestTick
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.ledge.ai.bt.actions.execution.ForestTick(
				this, executor, parent);
	}
}