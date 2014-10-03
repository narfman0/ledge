package com.blastedstudios.ledge.plugin.quest.trigger.beinghit;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.TriggerTable;
import com.blastedstudios.gdxworld.world.quest.trigger.AbstractQuestTrigger;

public class BeingHitTable extends TriggerTable {
	private final TextField beingText, originText, damageAmountText;
	private final BeingHitTrigger trigger;

	public BeingHitTable(Skin skin, BeingHitTrigger trigger) {
		super(skin);
		this.trigger = trigger;
		beingText = new TextField(trigger.getTarget(), skin);
		beingText.setMessageText("<being name regex>");
		damageAmountText = new TextField(trigger.getDamageAmount()+"", skin);
		damageAmountText.setMessageText("<damage amount>");
		originText = new TextField(trigger.getOrigin(), skin);
		originText.setMessageText("<origin name regex>");
		add(new Label("Being: ", skin));
		add(beingText);
		add(new Label("Damage amount: ", skin));
		add(damageAmountText);
		add(new Label("Origin: ", skin));
		add(originText);
	}

	@Override public AbstractQuestTrigger apply() {
		trigger.setTarget(beingText.getText());
		trigger.setDamageAmount(Float.parseFloat(damageAmountText.getText()));
		trigger.setOrigin(originText.getText());
		return trigger;
	}

}
