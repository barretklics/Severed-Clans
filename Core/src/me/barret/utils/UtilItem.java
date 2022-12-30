package me.barret.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UtilItem {
	
	
	
	
	
	public static ItemStack getSimpleItemStack(Material m, String name) {
		ItemStack i = new ItemStack(m);
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(name);
		i.setItemMeta(iMeta);	
		return i;
	}
	
	
	/**
	 * 
	 * 
	 * @param i
	 * @param name
	 * @return new itemstack
	 */
	public static ItemStack setDisplayName(ItemStack i, String name)
	{
		
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(name);
		i.setItemMeta(iMeta);	
		return i;
	}
	
	/**
	 * 
	 * 
	 * @param i
	 * @param line
	 * @return new itemstack
	 */
	public static ItemStack addLoreLine(ItemStack i, String line)
	{
		ItemMeta iMeta = i.getItemMeta();

		ArrayList<String> lore = (ArrayList<String>) iMeta.getLore();
		
		lore.add(line);		
		
		iMeta.setLore(lore);
		
		i.setItemMeta(iMeta);	
		
		return i;
	
	}
	
	/**
	 * 
	 * @param i
	 * @param string
	 * @return new itemstack
	 */
	public static ItemStack setLore(ItemStack i, List<String> string) {
		ItemMeta im = i.getItemMeta();
		im.setLore(string);
		i.setItemMeta(im);
		return i;
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 * @param i
	 * @return new itemstack
	 */
	public static ItemStack addGlow(ItemStack i)
	{
		ItemMeta meta = i.getItemMeta();
		meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
		i.setItemMeta(meta);
		i.addUnsafeEnchantment(Enchantment.LUCK, 1);
		return i;
	}
	
	
	
	
	public static boolean isSword(Material m)
	{
		if ( (m == Material.NETHERITE_SWORD) || (m == Material.DIAMOND_SWORD) || (m == Material.GOLDEN_SWORD) || (m == Material.IRON_SWORD))
		{
			return true;
		}
		return false;
	}
	
	
	public static boolean isAxe(Material m)
	{
		if ( (m == Material.NETHERITE_AXE) || (m == Material.DIAMOND_AXE) || (m == Material.GOLDEN_AXE) || (m == Material.IRON_AXE))
		{
			return true;
		}
		return false;
	}	
	
	public static boolean isBow(Material m)
	{
		if ( (m == Material.BOW) || (m == Material.CROSSBOW))
		{
			return true;
		}
		return false;
	}	
	
	public static boolean isBooster(Material m)
	{
		if ( (m == Material.NETHERITE_AXE) || (m == Material.GOLDEN_AXE) || (m == Material.CROSSBOW) || (m == Material.NETHERITE_SWORD) || (m == Material.GOLDEN_SWORD))
		{
			return true;
		}
		return false;	
		
	}
	
	
	
	
	public static ItemStack disposable(String name, Material m)
	{
		ItemStack item = new ItemStack(m, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(name);
		
		List<String> lore = new ArrayList<String>();

		lore.add(UUID.randomUUID().toString());
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);

		return item;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
