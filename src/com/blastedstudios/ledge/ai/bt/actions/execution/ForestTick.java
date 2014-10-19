// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/19/2014 13:46:12
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions.execution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.INPCActionExecutor;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action ForestTick. */
public class ForestTick extends jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of ForestTick that is able to run a
	 * com.blastedstudios.ledge.ai.bt.actions.ForestTick.
	 */
	public ForestTick(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.actions.ForestTick)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.actions.ForestTick");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		final NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		final WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		INPCActionExecutor executor = new INPCActionExecutor() {
			@Override public Status execute(String attackIdentifier) {
				if(attackIdentifier.equals("lunge")){
					Gdx.app.log("ForestTick.internalTick", "Lunge executed");
					float aimAngle = Being.getAimAngle(self, world.getPlayer());
					Vector2 lungeForce = new Vector2((float)Math.cos(aimAngle), (float)Math.sin(aimAngle)).scl(
							Properties.getFloat("ai.forest.lunge.magnitude", 120f));
					self.aim(aimAngle);
					self.getRagdoll().applyForceAtCenter(lungeForce.x, lungeForce.y);
				}
				return Status.SUCCESS;
			}
		};
		getContext().setVariable(INPCActionExecutor.EXECUTE_CONTEXT_NAME, executor);
		return Status.SUCCESS;
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