package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;


public class Assassin extends Kit
{
	static String title = "Assassin";
	static String prefix = ChatColor.YELLOW + "Assassin";
	
	
	
    public Assassin()
    {

		super(	title, 
				prefix, 
				Material.LEATHER_HELMET, 
				Material.LEATHER_CHESTPLATE, 
				Material.LEATHER_LEGGINGS,
				Material.LEATHER_BOOTS);
	}

	
	
	
	
	
	
	
}
