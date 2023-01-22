package me.barret.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TickUpdateEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private final int ticks;
	
	
	
	public TickUpdateEvent(int a)
	{
		this.ticks = a;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
	
	public int getTicks()
	{
		return this.ticks;
	}

}