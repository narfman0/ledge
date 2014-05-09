// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 05/08/2014 20:29:41
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions.execution;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;
import com.blastedstudios.ledge.world.weapon.Turret;

/** ExecutionAction class created from MMPM action ClosestTurret. */
public class ClosestTurret extends
		jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of ClosestTurret that is able to run
	 * a com.blastedstudios.ledge.ai.bt.actions.ClosestTurret.
	 */
	public ClosestTurret(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.actions.ClosestTurret)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.actions.ClosestTurret");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		Turret closest = null;
		for(Turret turret : world.getTurrets())
			if(closest == null || closest.getLocation().dst2(self.getPosition()) > turret.getLocation().dst2(self.getPosition()))
				closest = turret;
		if(closest == null)
			return Status.FAILURE;
		getContext().setVariable(AIFieldEnum.TURRET.name(), closest);
		getContext().setVariable("ClosestTurretObjective", new float[]{closest.getLocation().x, closest.getLocation().y});
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