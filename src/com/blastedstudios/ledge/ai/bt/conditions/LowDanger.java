// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 01/25/2013 21:35:07
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.conditions;

/** ModelCondition class created from MMPM condition LowDanger. */
public class LowDanger extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of LowDanger. */
	public LowDanger(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.blastedstudios.ledge.ai.bt.conditions.execution.LowDanger
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.blastedstudios.ledge.ai.bt.conditions.execution.LowDanger(
				this, executor, parent);
	}
}