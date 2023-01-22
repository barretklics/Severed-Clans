package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;

public class Brute extends Kit
{
	static String title = "Brute";
	static String prefix = ChatColor.AQUA + "Brute";

	
	
    public Brute()
    {
		super(	title, 
				prefix, 
				Material.DIAMOND_HELMET, 
				Material.DIAMOND_CHESTPLATE, 
				Material.DIAMOND_LEGGINGS,
				Material.DIAMOND_BOOTS);
	}

}
