package com.blastedstudios.ledge.plugin.quest.trigger.beinghit;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.TriggerTable;
import com.blastedstudios.gdxworld.world.quest.trigger.AbstractQuestTrigger;

public class BeingHitTable extends TriggerTable {
	private final TextField beingText, weaponText, damageAmountText;
	private final BeingHitTrigger trigger;

	public BeingHitTable(Skin skin, BeingHitTrigger trigger) {
		super(skin);
		this.trigger = trigger;
		beingText = new TextField(trigger.getTarget(), skin);
		beingText.setMessageText("<being name regex>");
		damageAmountText = new TextField(trigger.getDamageAmount()+"", skin);
		damageAmountText.setMessageText("<damage amount>");
		weaponText = new TextField(trigger.getOrigin(), skin);
		weaponText.setMessageText("<weapon name regex>");
		add(new Label("Being: ", skin));
		add(beingText);
		add(new Label("Damage amount: ", skin));
		add(damageAmountText);
		add(new Label("Weapon: ", skin));
		add(weaponText);
	}

	@Override public AbstractQuestTrigger apply() {
		trigger.setTarget(beingText.getText());
		trigger.setDamageAmount(Float.parseFloat(damageAmountText.getText()));
		trigger.setOrigin(weaponText.getText());
		return trigger;
	}

}
