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
import com.blastedstudios.ledge.util.VisibilityReturnStruct;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;
import com.blastedstudios.ledge.world.WorldManager;

/** ExecutionCondition class created from MMPM condition LowDanger. */
public class LowDanger extends
		jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of LowDanger that is able to run a
	 * com.blastedstudios.ledge.ai.bt.conditions.LowDanger.
	 */
	public LowDanger(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.conditions.LowDanger)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.conditions.LowDanger");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		Gdx.app.debug(this.getClass().getCanonicalName(), "ticked");
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		
		if(self.getLastDamage() != null && self.getLastDamage().getOrigin() != null && 
				getContext().getVariable(AIFieldEnum.LAST_HEALTH.name()) != null && 
				self.getHp() < (Float)getContext().getVariable(AIFieldEnum.LAST_HEALTH.name())){
			float[] target = new float[]{self.getLastDamage().getOrigin().getPosition().x, 
					self.getLastDamage().getOrigin().getPosition().y};
			getContext().setVariable("LowDangerTarget", target);
			getContext().setVariable("LowDangerLastTime", (int)System.currentTimeMillis());
			return Status.SUCCESS;
		}
		getContext().setVariable(AIFieldEnum.LAST_HEALTH.name(), self.getHp());
		
		VisibilityReturnStruct struct = world.isVisible((Being) getContext().getVariable(AIFieldEnum.SELF.name()));
		if(struct.isVisible()){
			getContext().setVariable("LowDangerTarget", struct.target);
			getContext().setVariable("LowDangerLastTime", (int)System.currentTimeMillis());
			return Status.SUCCESS;
		}
		return Status.FAILURE;
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