package me.barret.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class UtilInventory {
	public static Material getItemInMainHand(Player p)
	{
		if (p.getInventory().getItemInMainHand().getType() != null)
		{
			return p.getInventory().getItemInMainHand().getType();
		}
		
		return Material.AIR;
		
	}
}
