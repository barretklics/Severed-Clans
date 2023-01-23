package me.barret.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class UtilArmor {
	
	public static Material getHelmet(Player p)
	{
		Material helmet = Material.AIR;	
		if (p.getInventory().getHelmet() != null)
		{
			helmet = p.getInventory().getHelmet().getType();	
		}
		return helmet;
	}
	
	
	public static Material getChestplate(Player p)
	{
		Material chestplate = Material.AIR;	
		if (p.getInventory().getChestplate() != null)
		{
			chestplate = p.getInventory().getChestplate().getType();	
		}
		return chestplate;
	}
	
	
	public static Material getLeggings(Player p)
	{
		Material leggings = Material.AIR;	
		if (p.getInventory().getLeggings() != null)
		{
			leggings = p.getInventory().getLeggings().getType();	
		}
		return leggings;
	}
	
	
	public static Material getBoots(Player p)
	{
		Material boots = Material.AIR;	
		if (p.getInventory().getBoots() != null)
		{
			boots = p.getInventory().getBoots().getType();	
		}
		return boots;
	}
	
	

	
}
