package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.weapon.Gun;
import com.blastedstudios.ledge.world.weapon.Weapon;
import com.blastedstudios.ledge.world.weapon.WeaponType;

@PluginImplementation
public class AddAmmoConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"player"};
	}

	@Override public void execute(WorldManager world, String[] tokens) {
		if(tokens.length == 3 && tokens[1].equalsIgnoreCase("addammo")){
			int amount = Integer.parseInt(tokens[2]);
			Weapon equipped = world.getPlayer().getEquippedWeapon();
			Gdx.app.log("AddAmmoConsole.execute", "Adding: " + amount + " rounds of " + equipped.getType());
			if(equipped != null && equipped.getType() != WeaponType.MELEE){
				Gun gun = (Gun) equipped;
				world.getPlayer().addAmmo(gun.getAmmoType(), amount);
			}
		}
	}
}