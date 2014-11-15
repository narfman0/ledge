package com.blastedstudios.ledge.plugin.quest.manifestation.pathchange;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface IPathChangeHandler extends Plugin{
	CompletionEnum pathChange(String beingString, String pathString);
}
