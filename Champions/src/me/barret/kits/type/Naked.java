package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;

public class Naked extends Kit
{
	static String title = "Naked";
	static String prefix = ChatColor.GOLD + "Naked";

	
	
    public Naked()
    {
		super(title, 
				prefix, 
				Material.AIR,
				Material.AIR, 
				Material.AIR,
				Material.AIR);
	}

}
