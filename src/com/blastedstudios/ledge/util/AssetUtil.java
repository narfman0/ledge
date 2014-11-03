package com.blastedstudios.ledge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

public class AssetUtil {
	public static <T> void loadAssetsRecursive(AssetManager assets, FileHandle path, Class<T> type){
		for(FileHandle file : path.list()){
			if(file.isDirectory())
				loadAssetsRecursive(assets, file, type);
			else{
				try{
					assets.load(file.path(), type);
					Gdx.app.debug("MainScreen.loadAssetsRecursive", "Success loading asset path: " +
							path.path() + " as " + type.getCanonicalName());
				}catch(Exception e){
					Gdx.app.debug("MainScreen.loadAssetsRecursive", "Failed to load asset path: " +
							path.path() + " as " + type.getCanonicalName());
				}
			}
		}
	}
}
