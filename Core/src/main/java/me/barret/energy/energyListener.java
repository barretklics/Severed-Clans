package me.barret.energy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.barret.cooldown.cooldownManager;
import me.barret.events.TickUpdateEvent;

public class energyListener implements Listener{
	@EventHandler
	public void iterate(TickUpdateEvent e)
	{
		energyManager.iterateEnergyBars();
	}

}
