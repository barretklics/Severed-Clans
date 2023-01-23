package me.barret.kits.editor;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.barret.build.Build;
import me.barret.gui.Button;
import me.barret.gui.Gui;
import me.barret.kits.Kit;
import me.barret.user.userManager;
import me.barret.utils.UtilItem;

public class buildOverviewPage extends Gui{
	
	Kit kitOnPage;
	
	
	public buildOverviewPage(Player p, Kit k, List<Build> BuildList) {
		
		
		
		super(p,k.getKitName() + " Builds" + p.getUniqueId(), 54, new Button[] {
				new Button(0, new ItemStack(Material.ARROW), ChatColor.BOLD + "" + ChatColor.GREEN + "Back"),
			//	new Button(9, new ItemStack(Material.RED_DYE), "Default Build", BuildList.get(0).asList()),
						
				new Button(18, new ItemStack(k.getKitHelmet()), k.getKitPrefix()),
				new Button(27, new ItemStack(k.getKitChestplate()), k.getKitPrefix()),
				new Button(36, new ItemStack(k.getKitLeggings()), k.getKitPrefix()),
				new Button(45, new ItemStack(k.getKitBoots()), k.getKitPrefix()),
				
				
						
					
			//	new Button(11, new ItemStack(Material.ORANGE_DYE), "Build 1", BuildList.get(1).asList()),
			//	new Button(13, new ItemStack(Material.YELLOW_DYE), "Build 2", BuildList.get(2).asList()),
			//	new Button(15, new ItemStack(Material.GREEN_DYE), "Build 3", BuildList.get(3).asList()),
			//	new Button(17, new ItemStack(Material.BLUE_DYE), "Build 4", BuildList.get(4).asList()),
				
				
				new Button(29, new ItemStack(Material.ANVIL),  ChatColor.BOLD + "" + ChatColor.GREEN + "Edit Build 1"),
				new Button(31, new ItemStack(Material.ANVIL),  ChatColor.BOLD + "" + ChatColor.GREEN + "Edit Build 2"),
				new Button(33, new ItemStack(Material.ANVIL),  ChatColor.BOLD + "" + ChatColor.GREEN + "Edit Build 3"),
				new Button(35, new ItemStack(Material.ANVIL),  ChatColor.BOLD + "" + ChatColor.GREEN + "Edit Build 4"),
				

				new Button(47, new ItemStack(Material.TNT),  ChatColor.BOLD + "" + ChatColor.RED + "Delete Build 1"),
				new Button(49, new ItemStack(Material.TNT),  ChatColor.BOLD + "" + ChatColor.RED + "Delete Build 2"),
				new Button(51, new ItemStack(Material.TNT),  ChatColor.BOLD + "" + ChatColor.RED + "Delete Build 3"),
				new Button(53, new ItemStack(Material.TNT),  ChatColor.BOLD + "" + ChatColor.RED + "Delete Build 4"),
				
				
		});
		
		this.kitOnPage = k;
		
		
		
		
		ItemStack def = new ItemStack(Material.RED_DYE);
		ItemStack b1 = new ItemStack(Material.ORANGE_DYE);
		ItemStack b2 = new ItemStack(Material.YELLOW_DYE);
		ItemStack b3 = new ItemStack(Material.GREEN_DYE);
		ItemStack b4 = new ItemStack(Material.BLUE_DYE);
		
		if (userManager.getUser(p.getUniqueId()).getActiveBuildIndex(k) == 0) def = UtilItem.addGlow(def);
		if (userManager.getUser(p.getUniqueId()).getActiveBuildIndex(k) == 1) b1 = UtilItem.addGlow(b1);
		if (userManager.getUser(p.getUniqueId()).getActiveBuildIndex(k) == 2) b2 =UtilItem.addGlow(b2);
		if (userManager.getUser(p.getUniqueId()).getActiveBuildIndex(k) == 3) b3 =UtilItem.addGlow(b3);
		if (userManager.getUser(p.getUniqueId()).getActiveBuildIndex(k) == 4) b4 =UtilItem.addGlow(b4);
		
		this.addButton(new Button(9, def, ChatColor.BOLD + "" + ChatColor.GREEN + "Default Build", BuildList.get(0).asList()));
		
		this.addButton(new Button(11, b1, ChatColor.BOLD + "" + ChatColor.GREEN + "Build 1", BuildList.get(1).asList()));
		this.addButton(new Button(13, b2, ChatColor.BOLD + "" + ChatColor.GREEN + "Build 2", BuildList.get(2).asList()));
		this.addButton(new Button(15, b3, ChatColor.BOLD + "" + ChatColor.GREEN + "Build 3", BuildList.get(3).asList()));
		this.addButton(new Button(17, b4, ChatColor.BOLD + "" + ChatColor.GREEN + "Build 4", BuildList.get(4).asList()));
		/*
		this.addButton(new Button(9, new ItemStack(Material.RED_DYE), "Default Build", BuildList.get(0).asList()));
		
		this.addButton(new Button(11, new ItemStack(Material.ORANGE_DYE), "Build 1", BuildList.get(1).asList()));
		this.addButton(new Button(13, new ItemStack(Material.YELLOW_DYE), "Build 2", BuildList.get(2).asList()));
		this.addButton(new Button(15, new ItemStack(Material.GREEN_DYE), "Build 3", BuildList.get(3).asList()));
		this.addButton(new Button(17, new ItemStack(Material.BLUE_DYE), "Build 4", BuildList.get(4).asList()));
		*/
		this.populate();
		
	}
	public Kit getKit() {
		return this.kitOnPage;
	}
	
	
}
