package com.blastedstudios.ledge.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.util.ui.LedgeTextButton;
import com.blastedstudios.ledge.util.ui.LedgeWindow;
import com.blastedstudios.ledge.util.ui.UIHelper;

class OptionsWindow extends LedgeWindow{
	public OptionsWindow(final Skin skin, final GDXGame game, 
			final IOptionsWindowListener listener) {
		super("", skin);
		final TextField soundVolumeField = new TextField(Properties.get("sound.volume", "1"), skin);
		try{
			soundVolumeField.setColor(UIHelper.getColor(skin, "sound-volume", "volume", "textfield", "secondary"));
		}catch(Exception e){}
		soundVolumeField.setMessageText("<volume 0-1>");
		soundVolumeField.setMaxLength(6);
		final TextField musicVolumeField = new TextField(Properties.get("music.volume", "1"), skin);
		try{
			musicVolumeField.setColor(UIHelper.getColor(skin, "music-volume", "volume", "textfield", "secondary"));
		}catch(Exception e){}
		musicVolumeField.setMessageText("<volume 0-1>");
		musicVolumeField.setMaxLength(6);
		final Button acceptButton = new LedgeTextButton("Accept", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				try{
					float sound = Float.parseFloat(soundVolumeField.getText());
					Properties.set("sound.volume", sound+"");
				}catch(Exception e){}
				try{
					float music = Float.parseFloat(musicVolumeField.getText());
					Properties.set("music.volume", music+"");
				}catch(Exception e){}
				listener.optionsClosed();
			}
		});
		final Button cancelButton = new LedgeTextButton("Cancel", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.optionsClosed();
			}
		});
		add("Sound volume: ");
		add(soundVolumeField);
		row();
		add("Music volume: ");
		add(musicVolumeField);
		row();
		add(acceptButton);
		add(cancelButton);
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
		pack();
	}
	
	interface IOptionsWindowListener{
		void optionsClosed();
	}
}