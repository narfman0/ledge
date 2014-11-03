package com.blastedstudios.ledge.world;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.ui.main.MainScreen;

public class DialogManager {
	public final static int DIALOG_WIDTH = Properties.getInt("dialog.width", 67);
	private final static int TEXT_PORTRAIT_DRAW_HEIGHT = Properties.getInt("dialog.yoffset", 236);
	private final int TEXT_DRAW_HEIGHT;
	private final BitmapFont font;
	private final Queue<DialogStruct> dialogs = new LinkedList<>();
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final NinePatch patch;

	public DialogManager(Skin skin){
		spriteBatch.setColor(MainScreen.WINDOW_ALPHA_COLOR);
		font = skin.getFont("default-font-noblur");
		font.setColor(skin.getColor("dialog"));
		TEXT_DRAW_HEIGHT = TEXT_PORTRAIT_DRAW_HEIGHT - (int)font.getLineHeight();
		patch = skin.getPatch("character");
	}

	public void add(String dialog, String portrait){
		dialogs.add(new DialogStruct(splitRenderable(dialog, DIALOG_WIDTH), portrait));
	}

	public DialogStruct poll(){
		return dialogs.poll();
	}

	public void render(AssetManager assetManager){
		if(!dialogs.isEmpty()){
			float centerX = Gdx.graphics.getWidth()/2;
			spriteBatch.begin();
			patch.draw(spriteBatch, centerX - 256, 0, 768, 256);
			if(!dialogs.peek().portrait.isEmpty()){
				if(Properties.getBool("dialog.useportrait", false)){
					patch.draw(spriteBatch, centerX - 512, 0, 256, 256);
					try{
						final Texture tex = assetManager.get("data/textures/portrait/" + dialogs.peek().portrait + ".png");
						spriteBatch.draw(tex, centerX - 512, 0, 256, 256);
					}catch(GdxRuntimeException e){
						e.printStackTrace();
					}
				}
				font.draw(spriteBatch, dialogs.peek().portrait + ":", centerX - 170, TEXT_PORTRAIT_DRAW_HEIGHT);
			}
			font.drawMultiLine(spriteBatch, dialogs.peek().dialog, centerX - 224, TEXT_DRAW_HEIGHT);
			spriteBatch.end();
		}
	}

	/**
	 * @param lineWidth max number of characters the line may be before newline
	 * @return string with newlines inserted for words that would straddle 
	 * lines
	 */
	public static String splitRenderable(String dialog, int lineWidth){
		StringBuilder builder = new StringBuilder(),
				currentLine = new StringBuilder();
		if(dialog == null)
			return "";
		if(!dialog.contains(" "))
			return dialog;
		for(String dialogWord : dialog.split(" ")){
			if(dialogWord.length() + currentLine.length() + 1 >= lineWidth){
				builder.append("\n");
				currentLine = new StringBuilder();
			}
			currentLine.append(dialogWord + " ");
			builder.append(dialogWord + " ");
		}
		return builder.toString();
	}

	public class DialogStruct{
		public final String dialog, portrait;

		public DialogStruct(String dialog, String portrait){
			this.dialog = dialog;
			this.portrait = portrait;
		}
		public String toString(){
			return "[DialogStruct dialog:" + dialog + " portrait:" + portrait + "]";
		}
	}
}
