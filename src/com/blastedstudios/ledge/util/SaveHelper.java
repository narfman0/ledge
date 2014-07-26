package com.blastedstudios.ledge.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.being.Player;

public class SaveHelper {
	private static String SAVE_DIRECTORY = System.getProperty("user.home") + "/.ledge/save";

	public static List<Player> load() {
		init();
		Gdx.app.log("SaveHelper.load","Loading characters...");
		List<Player> characters = new ArrayList<Player>();
		for(FileHandle file : new FileHandle(SAVE_DIRECTORY).list()){
			try {
				Player being = (Player) FileUtil.getSerializer(file).load(file);
				characters.add(being);
				Gdx.app.log("SaveHelper.load","Loaded: " + being.getName());
			} catch (Exception e) {
				Gdx.app.error("SaveHelper.load","Failed to load " + file.path());
				e.printStackTrace();
			} 
		}
		Gdx.app.log("SaveHelper.load","Done loading characters");
		return characters;
	}

	public static void save(Player character){
		init();
		FileHandle file = new FileHandle(SAVE_DIRECTORY + "/" + character.getName() + 
				"." + Properties.get("save.extenstion", "xml"));
		try{
			FileUtil.getSerializer(file).save(file, character);
		}catch(Exception e){
			Gdx.app.error("SaveHelper.save","Failed to write " + character.getName());
			e.printStackTrace();
		}
		Gdx.app.log("SaveHelper.save","Saved " + character.getName() + " successfully");
	}
	
	private static void init(){
		File savePath = new File(SAVE_DIRECTORY);
		if(!savePath.isDirectory())
			savePath.mkdirs();
	}
}
