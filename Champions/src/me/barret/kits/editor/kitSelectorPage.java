package me.barret.kits.editor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.barret.gui.Button;
import me.barret.gui.Gui;
import me.barret.kits.kitManager;

public class kitSelectorPage extends Gui{
	
	private static String brute = kitManager.getKitFromString("Brute").getKitPrefix();
	private static String knight = kitManager.getKitFromString("Knight").getKitPrefix();
	private static String mage = kitManager.getKitFromString("Mage").getKitPrefix();
	private static String mech = kitManager.getKitFromString("Mechanist").getKitPrefix();
	private static String ranger = kitManager.getKitFromString("Ranger").getKitPrefix();
	private static String ass = kitManager.getKitFromString("Assasin").getKitPrefix();	
	
	public kitSelectorPage(Player p) {
		
		
		super(p, "Kit Editor" + p.getUniqueId(), 54, new Button[] {
				
				new Button(9, new ItemStack(Material.DIAMOND_HELMET), brute),
				new Button(18, new ItemStack(Material.DIAMOND_CHESTPLATE), brute),
				new Button(27, new ItemStack(Material.DIAMOND_LEGGINGS), brute),
				new Button(36, new ItemStack(Material.DIAMOND_BOOTS), brute),
				
				new Button(10, new ItemStack(Material.IRON_HELMET), knight),
				new Button(19, new ItemStack(Material.IRON_CHESTPLATE), knight),
				new Button(28, new ItemStack(Material.IRON_LEGGINGS), knight),
				new Button(37, new ItemStack(Material.IRON_BOOTS), knight),
				
				new Button(12, new ItemStack(Material.GOLDEN_HELMET), mage),
				new Button(21, new ItemStack(Material.GOLDEN_CHESTPLATE), mage),
				new Button(30, new ItemStack(Material.GOLDEN_LEGGINGS), mage),
				new Button(39, new ItemStack(Material.GOLDEN_BOOTS), mage),
				
				new Button(14, new ItemStack(Material.NETHERITE_HELMET), mech),
				new Button(23, new ItemStack(Material.NETHERITE_CHESTPLATE), mech),
				new Button(32, new ItemStack(Material.NETHERITE_LEGGINGS), mech),
				new Button(41, new ItemStack(Material.NETHERITE_BOOTS), mech),
				
				new Button(16, new ItemStack(Material.CHAINMAIL_HELMET), ranger),
				new Button(25, new ItemStack(Material.CHAINMAIL_CHESTPLATE), ranger),
				new Button(34, new ItemStack(Material.CHAINMAIL_LEGGINGS), ranger),
				new Button(43, new ItemStack(Material.CHAINMAIL_BOOTS), ranger),
				
				new Button(17, new ItemStack(Material.LEATHER_HELMET), ass),
				new Button(26, new ItemStack(Material.LEATHER_CHESTPLATE), ass),
				new Button(35, new ItemStack(Material.LEATHER_LEGGINGS), ass),
				new Button(44, new ItemStack(Material.LEATHER_BOOTS), ass),
		});
		
	}
}

