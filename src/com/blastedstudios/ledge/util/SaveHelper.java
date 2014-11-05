package com.blastedstudios.ledge.util;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.Log;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.being.Player;

public class SaveHelper {
	private static String SAVE_DIRECTORY = "/.ledge/save";

	public static List<Player> load() {
		init();
		Log.log("SaveHelper.load","Loading characters...");
		List<Player> characters = new LinkedList<Player>();
		for(FileHandle file : Gdx.files.external(SAVE_DIRECTORY).list()){
			try {
				Player being = (Player) FileUtil.getSerializer(file).load(file);
				characters.add(being);
				Log.log("SaveHelper.load","Loaded: " + being.getName());
			} catch (Exception e) {
				Log.error("SaveHelper.load","Failed to load " + file.path());
				e.printStackTrace();
			} 
		}
		Log.log("SaveHelper.load","Done loading characters");
		return characters;
	}

	public static void save(Player character){
		init();
		FileHandle file = Gdx.files.external(SAVE_DIRECTORY + "/" + character.getName() + 
				"." + Properties.get("save.extenstion", "xml"));
		try{
			FileUtil.getSerializer(file).save(file, character);
			Log.log("SaveHelper.save","Saved " + character.getName() + " successfully");
		}catch(Exception e){
			Log.error("SaveHelper.save","Failed to write " + character.getName() + " to " + file.path());
			e.printStackTrace();
		}
	}
	
	private static void init(){
		Gdx.files.external(SAVE_DIRECTORY).mkdirs();
	}
}
