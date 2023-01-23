package me.barret.kits.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.barret.kits.Kit;

public class Mage extends Kit
{
	static String title = "Mage";
	static String prefix = ChatColor.YELLOW + "Mage";

	
	
    public Mage()
    {
		super(	title, 
				prefix, 
				Material.GOLDEN_HELMET, 
				Material.GOLDEN_CHESTPLATE, 
				Material.GOLDEN_LEGGINGS,
				Material.GOLDEN_BOOTS);
	}

}
