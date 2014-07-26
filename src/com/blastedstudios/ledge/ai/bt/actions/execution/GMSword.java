// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 07/23/2014 21:59:49
// ******************************************************* 
package com.blastedstudios.ledge.ai.bt.actions.execution;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.animation.GDXAnimationHandler;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.NPC;
import com.blastedstudios.ledge.world.being.NPC.AIFieldEnum;

/** ExecutionAction class created from MMPM action GMSword. */
public class GMSword extends jbt.execution.task.leaf.action.ExecutionAction {
	private static final float SWORD_RATE = Properties.getFloat("garbageman.sword.rate", 10f);
	private static final String LAST_SWORD_ATTACK = "last.sword.attack";

	/**
	 * Constructor. Constructs an instance of GMSword that is able to run a
	 * com.blastedstudios.ledge.ai.bt.actions.GMSword.
	 */
	public GMSword(jbt.model.core.ModelTask modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

		if (!(modelTask instanceof com.blastedstudios.ledge.ai.bt.actions.GMSword)) {
			throw new java.lang.RuntimeException(
					"The ModelTask must subclass com.blastedstudios.ledge.ai.bt.actions.GMSword");
		}
	}

	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		Gdx.app.debug(this.getClass().getCanonicalName(), "spawned");
		getContext().setVariable(LAST_SWORD_ATTACK, 0f);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		WorldManager world = (WorldManager) getContext().getVariable(AIFieldEnum.WORLD.name());
		NPC npc = (NPC) getContext().getVariable(AIFieldEnum.SELF.name());
		GDXAnimationHandler handler = (GDXAnimationHandler) getContext().getVariable(GMTick.HANDLER_NAME);
		float total = ((float)System.currentTimeMillis())/1000f;
		if(total - SWORD_RATE > (Float)getContext().getVariable(LAST_SWORD_ATTACK)){
			float distance = world.getPlayer().getPosition().dst(npc.getPosition());
			if(distance < Properties.getFloat("garbageman.sword.distance.max", 7f) && distance > GMStomp.STOMP_DISTANCE){
				handler.applyCurrentAnimation(handler.getAnimations().getAnimation("sword"), 0);
				getContext().setVariable(LAST_SWORD_ATTACK, total);
			}
		}
		//TODO find out when hit occurs
		if(handler.getCurrentAnimation().getName().equals("sword")){
			handler.render(Gdx.graphics.getDeltaTime());
			return jbt.execution.core.ExecutionTask.Status.RUNNING;
		}
		return jbt.execution.core.ExecutionTask.Status.FAILURE;
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