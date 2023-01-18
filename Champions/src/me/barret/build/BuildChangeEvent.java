package me.barret.build;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.barret.kits.Kit;

public class BuildChangeEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	private Player p;
	private UUID u;
	private Build oldBuild;
	private Build newBuild;
	
	
	/**
	 * 
	 * @param p
	 * @param u
	 * @param oldBuild
	 * @param newBuild
	 */
	public BuildChangeEvent(Player p, UUID u, Build oldBuild, Build newBuild)
	{
		this.p = p;
		this.u = u;
		this.newBuild = newBuild;
		this.oldBuild = oldBuild;
		
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
	
	
	public Build getOldBuild() {
		return this.oldBuild;
	}
	
	public Build getNewBuild() {
		return this.newBuild;		
	}
	
	
	
	

}
