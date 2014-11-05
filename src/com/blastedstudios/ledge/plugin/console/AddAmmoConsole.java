package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Melee;
import com.blastedstudios.ledge.world.weapon.Weapon;

@PluginImplementation
public class AddAmmoConsole extends AbstractConsole{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(String[] tokens) {
		if(tokens.length == 3 && tokens[1].equalsIgnoreCase("addammo")){
			int amount = Integer.parseInt(tokens[2]);
			Weapon equipped = world.getPlayer().getEquippedWeapon();
			Gdx.app.log("AddAmmoConsole.execute", "Adding: " + amount + " rounds of " + equipped.getClass().getSimpleName());
			if(equipped != null && !(equipped instanceof Melee)){
				Gun gun = (Gun) equipped;
				world.getPlayer().addAmmo(gun.getAmmoType(), amount);
			}
		}
	}

	@Override public String getHelp() {
		return "Add ammo to current weapon. Usage:\nplayer addammo <rds>, where <rds> is an integer value";
	}
}