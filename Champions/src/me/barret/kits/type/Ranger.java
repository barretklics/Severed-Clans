package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;

public class Ranger extends Kit
{
	static String title = "Ranger";
	static String prefix = ChatColor.YELLOW + "Ranger";

	
	
    public Ranger()
    {
		super(	title, 
				prefix, 
				Material.CHAINMAIL_HELMET, 
				Material.CHAINMAIL_CHESTPLATE, 
				Material.CHAINMAIL_LEGGINGS,
				Material.CHAINMAIL_BOOTS);
	}

}
