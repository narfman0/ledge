package com.blastedstudios.ledge.plugin.console;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.gameplay.GameplayScreen;
import com.blastedstudios.ledge.util.IConsoleCommand;
import com.blastedstudios.ledge.world.DropManager;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.weapon.Weapon;
import com.blastedstudios.ledge.world.weapon.WeaponFactory;

@PluginImplementation
public class GunDropConsole implements IConsoleCommand{
	@Override public String[] getMatches() {
		return new String[]{"gun"};
	}

	@Override public void execute(final WorldManager world, final GameplayScreen screen, String[] tokens) {
		if(tokens[1].equalsIgnoreCase("drop")){
			if(tokens.length == 2)
				Gdx.app.log("GunDropConsole.execute", "Gun drop probability: " + 
						Properties.getFloat(DropManager.GUN_DROP_PROPERTY, .05f));
			else if(tokens.length == 3){
				try{
					Properties.set(DropManager.GUN_DROP_PROPERTY, "" + Float.parseFloat(tokens[2]));
					Gdx.app.log("GunDropConsole.execute", "Gun drop probability set to: " + 
							Properties.getFloat(DropManager.GUN_DROP_PROPERTY));
				}catch(NumberFormatException e){
					String weaponName = tokens[2];
					final Weapon weapon;
					if(weaponName.equalsIgnoreCase("random")){
						int iLevel = WeaponFactory.generateILevel(world.getPlayer().getLevel());
						weapon = WeaponFactory.generateGun(iLevel, world.getPlayer().getLevel());
					}else
						weapon = WeaponFactory.getWeapon(weaponName);
					world.getPlayer().getGuns().add(0, weapon);
					world.getPlayer().setCurrentWeapon(0, world.getWorld());
					Gdx.app.log("GunDropConsole.execute", "Generated weapon: " + weapon + " for player");
				}
			}
		}
	}
}
