package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;


public class Assasin extends Kit
{
	static String title = "Assasin";
	static String prefix = ChatColor.YELLOW + "Assasin";
	
	
	
    public Assasin()
    {

		super(	title, 
				prefix, 
				Material.LEATHER_HELMET, 
				Material.LEATHER_CHESTPLATE, 
				Material.LEATHER_LEGGINGS,
				Material.LEATHER_BOOTS);
	}

	
	
	
	
	
	
	
}
