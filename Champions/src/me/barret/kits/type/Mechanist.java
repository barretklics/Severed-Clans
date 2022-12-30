package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;

public class Mechanist extends Kit
{
	static String title = "Mechanist";
	static String prefix = ChatColor.RED + "Mechanist";

	
	
    public Mechanist()
    {
		super(title, 
				prefix, 
				Material.NETHERITE_HELMET,
				Material.NETHERITE_CHESTPLATE, 
				Material.NETHERITE_LEGGINGS,
				Material.NETHERITE_BOOTS);
	}

}
