package me.barret.kits.editor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;

import me.barret.build.Build;
import me.barret.build.BuildChangeEvent;
import me.barret.gui.Button;
import me.barret.gui.ButtonClickEvent;
import me.barret.gui.Gui;
import me.barret.kits.Kit;
import me.barret.kits.kitChangeEvent;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.Skill.SkillType;
import me.barret.user.user;
import me.barret.user.userManager;
import me.barret.utils.UtilSound;

public class kitSelector implements Listener{


	public void openKitSelector(Player p) {
		p.openInventory(new kitSelectorPage(p).getInventory());
	}
	
	
	public void openKitEditor(Player p, Kit k) {
		user u = userManager.getUser(p.getUniqueId());
		
		p.openInventory(new buildOverviewPage(p, k, u.getBuildsForKit(k)).getInventory());
		return;
	}
	
	
	public void openBuildEditor(Player p, Kit k, int i) {
		user u = userManager.getUser(p.getUniqueId());
		
		Build b = u.getBuildsForKit(k).get(i);
		
		p.openInventory(new buildEditorPage(p, b, i).getInventory());
		return;
	}
	
	
	
	
	
	
	@EventHandler
	public void onClickEnchantingTable(PlayerInteractEvent e)
	{
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (e.getClickedBlock().getType() != Material.ENCHANTING_TABLE) return;
		e.setCancelled(true);
		openKitSelector(e.getPlayer());
		return;
	}
	
	
	
	@EventHandler
	public void onKitSelectorInput(ButtonClickEvent e) {
		if (e.getButton() == null) return;
		if (!(e.getGui() instanceof kitSelectorPage)) return;
		
		String tag = ChatColor.stripColor(e.getButton().getName());
		
		Kit k = kitManager.getKitFromString(tag);
		if (k == null) return; //Critical error
		
		openKitEditor(e.getPlayer(), k);
		
		return;
	}
		
		

	
	
	@EventHandler
	public void onBuildOverviewInput(ButtonClickEvent e) {
		if (e.getButton() == null) return;
		if (!(e.getGui() instanceof buildOverviewPage)) return;
		Player p = e.getPlayer();
		String tag = e.getButton().getName();
		
		Gui g = e.getGui();
		
		Kit kit = ((buildOverviewPage) g).getKit();
		
		
		if (e.getButton().getItem().getType() == Material.ARROW) {
			openKitSelector(e.getPlayer());
			return;
		}
		
		
		
		
		
		
		
		int buildIndex = -1;
		String op = "nill";
		
		if (e.getButton().getItem().getType() == Material.RED_DYE) op = "Apply";
		if (e.getButton().getItem().getType() == Material.ORANGE_DYE) op = "Apply";
		if (e.getButton().getItem().getType() == Material.YELLOW_DYE) op = "Apply";
		if (e.getButton().getItem().getType() == Material.GREEN_DYE) op = "Apply";
		if (e.getButton().getItem().getType() == Material.BLUE_DYE) op = "Apply";
		
		
		
		if (e.getButton().getItem().getType() == Material.ANVIL) op = "Edit";
		if (e.getButton().getItem().getType() == Material.TNT) op = "Delete";
		
		
		if (tag.contains("Default")) buildIndex = 0;
		if (tag.contains("1")) buildIndex = 1;
		if (tag.contains("2")) buildIndex = 2;
		if (tag.contains("3")) buildIndex = 3;
		if (tag.contains("4")) buildIndex = 4;
		
		
		if (op.equalsIgnoreCase("Edit")) {
			openBuildEditor(e.getPlayer(), kit, buildIndex);
			return;
		}
		
		
		if (op.equalsIgnoreCase("Apply")) {
			
			user u = userManager.getUser(e.getPlayer().getUniqueId());
		
			
			Build oldBuild = u.getCurrentBuild(); //old build
			
			
			userManager.getUser(e.getPlayer().getUniqueId()).setActiveBuild(kit, buildIndex); //apply new build
			
			Build newBuild = u.getCurrentBuild();

			//ADD EVENT for build change
			



			openKitEditor(e.getPlayer(), kit);
			
			
			//no previous build, changes to new build
			if (oldBuild == null) {
				if (newBuild == null) return;
				Bukkit.getPluginManager().callEvent(new BuildChangeEvent(p, p.getUniqueId(), oldBuild, newBuild));
			}
			
			//error case
			if (newBuild == null) return;
			
			
			//Change of build
			if (oldBuild != newBuild) {
				Bukkit.getPluginManager().callEvent(new BuildChangeEvent(p, p.getUniqueId(), oldBuild, newBuild));
			}
			
			
			
			return;
		}
		
		return;

	}	
	
	@EventHandler
	public void onBuildEditorInput(ButtonClickEvent e) {
		if (e.getButton() == null) return;
		if (!(e.getGui() instanceof buildEditorPage)) return;
		
		buildEditorPage g =  (buildEditorPage) e.getGui();

		Kit kit = g.getKit();

		if (e.getButton().getItem().getType() == Material.ARROW) {
			openKitEditor(e.getPlayer(), kit);
			return;
		}
		

		//SKILLS TIME	
		if (e.getButton() instanceof SkillButton) {
			SkillButton button_ins = (SkillButton) e.getButton();
			Build b = g.getBuild();
			Build oldBuild = b;
			user u = userManager.getUser(e.getPlayer().getUniqueId());
			//System.out.println("Build Index from page: " + g.getIndex())
			Skill s = button_ins.getSkill();
			
			
			if (e.getClick() == ClickType.LEFT) {
				if (b.IncrementSkill(s)) UtilSound.playSoundToPlayer(e.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
				
				//System.out.println("Left Click Skill. Build =" + g.getIndex());
				openBuildEditor(e.getPlayer(), kit, g.getIndex());
			}
			
			if (e.getClick() == ClickType.RIGHT) {
				if (b.DecrementSkill(s));  UtilSound.playSoundToPlayer(e.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
				//System.out.println("Right Click Skill. Build =" + g.getIndex());
				openBuildEditor(e.getPlayer(), kit, g.getIndex());
			}
			
			
			
			
			//Call event if skills change
			Player p = e.getPlayer();
			if (oldBuild.asList() != u.getCurrentBuild().asList()){
				Bukkit.getPluginManager().callEvent(new BuildChangeEvent(p, p.getUniqueId(), oldBuild, u.getCurrentBuild()));
				

			}
			
			
			
		}
		
		
		
		
		
		
		
		return;
	}	
	
	
	
	
	
	
	
	
	
		
		
		
/*
		System.out.println("Button Click Event-\n" +
							"Player: " + e.getPlayer().getName() +
							"Inventory Name: " + e.getGui().getName() +
							"Button Name: " + e.getButton().getName() +
							"Slot: " + e.getSlot() +
							"\n");
	}
*/
	
}
