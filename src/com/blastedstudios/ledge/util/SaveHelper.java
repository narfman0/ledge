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
	{ init(); }

	public static List<Player> load() {
		Log.log("SaveHelper.load","Loading characters...");
		List<Player> characters = new LinkedList<Player>();
		for(FileHandle file : Gdx.files.external(".ledge/save").list()){
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
		FileHandle file = Gdx.files.external(".ledge/save").child(character.getName() + "." + Properties.get("save.extenstion", "xml"));
		try{
			FileUtil.getSerializer(file).save(file, character);
			Log.log("SaveHelper.save","Saved " + character.getName() + " successfully");
		}catch(Exception e){
			Log.error("SaveHelper.save","Failed to write " + character.getName() + " to " + file.path());
			e.printStackTrace();
		}
	}
	
	public static void saveProperties(){
		Properties.store(Gdx.files.external(".ledge/ledge.properties").write(false), "");
	}
	
	public static void loadProperties(){
		try{
			Properties.load(Gdx.files.external(".ledge/ledge.properties").read());
		}catch(Exception e){
			Log.debug("SaveHelper.loadProperties", "Error loading properties: " + e.getMessage());
		}
	}
	
	private static void init(){
		Gdx.files.external(".ledge/save").mkdirs();
	}
}
