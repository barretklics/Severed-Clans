package me.barret.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UtilPlayer {
	public static Player getPlayerFromUUID(UUID uuid)
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p.getUniqueId() == uuid)
			{
				return p;
			}
		}	
		return null;

	}
}
