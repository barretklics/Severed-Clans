package me.barret.cooldown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.barret.events.TickUpdateEvent;

public class cooldownListener implements Listener{

	
	
	@EventHandler
	public void iterate(TickUpdateEvent e)
	{
		cooldownManager.iterateCooldowns();
	}
}
