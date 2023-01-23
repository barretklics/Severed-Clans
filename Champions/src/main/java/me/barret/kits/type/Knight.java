package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;

public class Knight extends Kit
{
	static String title = "Knight";
	static String prefix = ChatColor.GRAY + "Knight";

	
	
    public Knight()
    {
		super(	title, 
				prefix, 
				Material.IRON_HELMET, 
				Material.IRON_CHESTPLATE, 
				Material.IRON_LEGGINGS,
				Material.IRON_BOOTS);
	}

}
