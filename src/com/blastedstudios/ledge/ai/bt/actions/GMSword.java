// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 07/23/2014 21:59:49
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions;

/** ModelAction class created from MMPM action GMSword. */
public class GMSword extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of GMSword. */
	public GMSword(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.ledge.ai.bt.actions.execution.GMSword task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.ledge.ai.bt.actions.execution.GMSword(
				this, executor, parent);
	}
}