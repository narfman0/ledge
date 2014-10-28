package com.blastedstudios.ledge.plugin.quest.manifestation.beingremove;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

@PluginImplementation
public interface IBeingRemoveHandler extends Plugin{
	CompletionEnum removeBeing(String being);
}
