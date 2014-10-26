// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/25/2014 15:29:55
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions.execution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.INPCActionExecutor;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action NearDeadTick. */
public class NearDeadTick extends
		jbt.execution.task.leaf.action.ExecutionAction {

	/**
	 * Constructor. Constructs an instance of NearDeadTick that is able to run a
	 * com.blastedstudios.ledge.ai.bt.actions.NearDeadTick.
	 */
	public NearDeadTick(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.actions.NearDeadTick)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.actions.NearDeadTick");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
	}

	protected Status internalTick() {
		final NPC self = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		final WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		INPCActionExecutor executor = new INPCActionExecutor() {
			@Override public Status execute(String identifier) {
				if(identifier.equals("aim")){
					float aimAngle = Being.getAimAngle(self, world.getPlayer());
					// If within 45 degrees below player, aim at him
					if(aimAngle > -.75*Math.PI && aimAngle < -.25*Math.PI){
						float[] target = new float[]{world.getPlayer().getPosition().x, world.getPlayer().getPosition().y};
						getContext().setVariable("aim_target", target);
					}else
						return Status.FAILURE;
				}else if(identifier.equals("choose_weapon")){
					float d = self.getPosition().dst(world.getPlayer().getPosition());
					if(d > 15f)
						self.setCurrentWeapon(2, world.getWorld());
					else if(d > 9f)
						self.setCurrentWeapon(1, world.getWorld());
					else
						self.setCurrentWeapon(0, world.getWorld());
				}else if(identifier.equals("path")){
					boolean moveLeft = System.currentTimeMillis() % 15000 < 7500;
					Vector2 origin = world.getPlayer().getPosition().cpy(),
							target = origin.add(10f * (moveLeft ? -1f : 1f), 10f);
					getContext().setVariable("move_target", new float[]{target.x, target.y});
				}
				return Status.SUCCESS;
			}
		};
		getContext().setVariable(INPCActionExecutor.EXECUTE_CONTEXT_NAME, executor);
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