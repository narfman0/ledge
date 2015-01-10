package com.blastedstudios.ledge.ui.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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
		final Slider soundVolumeSlider = new Slider(0f, 1f, .05f, false, skin);
		soundVolumeSlider.setValue(Properties.getFloat("sound.volume", 1f));
		soundVolumeSlider.setColor(UIHelper.getColor(skin, "sound-volume", "volume", "secondary"));
		final Slider musicVolumeSlider = new Slider(0f, 1f, .05f, false, skin);
		musicVolumeSlider.setValue(Properties.getFloat("music.volume", 1f));
		musicVolumeSlider.setColor(UIHelper.getColor(skin, "music-volume", "volume", "secondary"));
		final Button acceptButton = new LedgeTextButton("Accept", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Properties.set("sound.volume", soundVolumeSlider.getValue()+"");
				Properties.set("music.volume", musicVolumeSlider.getValue()+"");
				listener.optionsClosed();
			}
		});
		final Button cancelButton = new LedgeTextButton("Cancel", skin, new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.optionsClosed();
			}
		});
		add("Sound volume: ");
		add(soundVolumeSlider);
		row();
		add("Music volume: ");
		add(musicVolumeSlider);
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