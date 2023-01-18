package me.barret.kits;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.barret.build.BuildChangeEvent;
import me.barret.user.userManager;

public class kitChangeEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	private Player p;
	private UUID u;
	private Kit oldKit;
	private Kit newKit;
	
	
	/**
	 * 
	 * @param p
	 * @param u
	 * @param oldKit
	 * @param newKit
	 */
	public kitChangeEvent(Player p, UUID u, Kit oldKit, Kit newKit)
	{
		this.p = p;
		this.u = u;
		this.newKit = newKit;
		this.oldKit = oldKit;
		
		me.barret.user.user user = userManager.getUser(p.getUniqueId());
		//Bukkit.getPluginManager().callEvent(new BuildChangeEvent(p, p.getUniqueId(), user.getCurrentBuildForKit(oldKit), user.getCurrentBuild()));
		
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
	
	
	public Player getPlayer()
	{
		return this.p;
	}

	public UUID getPlayerUUID()
	{
		return this.u;
	}
	
	
	public Kit getOldKit() {
		return this.oldKit;
	}
	
	public Kit getNewKit() {
		return this.newKit;		
	}
	
	

}
