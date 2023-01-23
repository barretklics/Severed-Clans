package me.barret;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ChampionsListener<Champions> implements Listener{
    private Champions instance;

	public ChampionsListener(Champions instance)
	{
		Bukkit.getPluginManager().registerEvents(this, (Plugin) instance);
		this.instance = instance;
    }
    protected Champions getInstance()
    {
        return instance;
    }
}