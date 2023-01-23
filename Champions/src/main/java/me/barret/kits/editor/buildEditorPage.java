package me.barret.kits.editor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.barret.build.Build;
import me.barret.build.BuildSkill;
import me.barret.gui.Button;
import me.barret.gui.Gui;
import me.barret.kits.Kit;
import me.barret.skill.Skill;
import me.barret.skill.Skill.SkillType;
import me.barret.utils.UtilItem;
import me.barret.skill.skillManager;
import me.barret.user.userManager;

public class buildEditorPage extends Gui{
	private Build b;
	private Kit kit;
	private int build_index = -1;
	
	public buildEditorPage(Player p, Build build, int build_index) {
		
		

		
		
		super(p, build.getKit().getKitName() + " Build " + build_index + p.getUniqueId(), 54, new Button[] {
				
				new Button(26, new ItemStack(build.getKit().getKitHelmet()), build.getKit().getKitPrefix()),
				new Button(35, new ItemStack(build.getKit().getKitChestplate()), build.getKit().getKitPrefix()),
				new Button(44, new ItemStack(build.getKit().getKitLeggings()), build.getKit().getKitPrefix()),
				new Button(53, new ItemStack(build.getKit().getKitBoots()), build.getKit().getKitPrefix()),
				
				new Button(7, new ItemStack(Material.ARROW), ChatColor.BOLD + "" + ChatColor.GREEN + "Back"),
				//new Button(8, new ItemStack(Material.GOLD_INGOT), ChatColor.GREEN + "" + ChatColor.BOLD + build.getTokens() + " skill tokens"),
				
				
				
				
				new Button(0, new ItemStack(Material.IRON_SWORD), ChatColor.BOLD + "" + ChatColor.GREEN + "Sword Skill"),
				new Button(9, new ItemStack(Material.IRON_AXE), ChatColor.BOLD + "" + ChatColor.GREEN + "Axe Skill"),
				new Button(18, new ItemStack(Material.BOW), ChatColor.BOLD + "" + ChatColor.GREEN + "Bow Skill"),
				new Button(27, new ItemStack(Material.RED_DYE), ChatColor.BOLD + "" + ChatColor.GREEN + "Passive A"),
				new Button(36, new ItemStack(Material.GREEN_DYE), ChatColor.BOLD + "" + ChatColor.GREEN + "Passive B"),
				new Button(45, new ItemStack(Material.BLUE_DYE), ChatColor.BOLD + "" + ChatColor.GREEN + "Global Passive C"),
				

			});
		this.b = userManager.getUser(p.getUniqueId()).getBuildsForKit(build.getKit()).get(build_index);
		this.build_index = build_index;
		kit = build.getKit();
		List<Skill> tmpSkillList = new ArrayList<>();
		
		ItemStack tokenItem = new ItemStack(Material.GOLD_INGOT);
		tokenItem.setAmount(b.getTokens());
		this.addButton(new Button(8, tokenItem, ChatColor.GREEN + "" + ChatColor.BOLD + build.getTokens() + " Skill Tokens"));	
		
		
		
		
		
		//BUILD IS CURRENT SKILLS
		
		for (int i = 0; i < 6; i++) {
			tmpSkillList = skillManager.getSkillsForKitAndType(kit, SkillType.valueOf(i));	
			for (int j = 1; j < 7; j++) {
			//for (int j = (i*9)+1; j < (i*9)+7; j++) {
				
				int gui_index = (i*9)+j;
				
				
				if (j <= tmpSkillList.size()) {
					Skill skill = tmpSkillList.get(j-1);
					ItemStack item = new ItemStack(Material.BOOK);
					
					BuildSkill bs = b.getBuildSkillFromSkill(skill);
					if (bs != null) {
						//System.out.println("FUCK:              " + skill.getName());
						item = new ItemStack(Material.BOOK);
						item = UtilItem.addGlow(item);
	
					//	System.out.println("Printing Gui---- Name: " + bs.getSkill().getName() + "Level: " + bs.getLevel());
						
						
					//	int level = bs.getLevel();
						
						
						this.addButton(new SkillButton(gui_index, item,  ChatColor.BOLD + "" + ChatColor.GREEN + skill.getName() + " (" + bs.getLevel() + "/" + skill.getMaxLevel() + ")", skill.getDescription(), build, skill));

					}
					else {
						
						
						this.addButton(new SkillButton(gui_index, item, ChatColor.BOLD + "" + ChatColor.GREEN + skill.getName() + " (0/" + skill.getMaxLevel() + ")", skill.getDescription(),build, skill));	
						
						//Has skill on
						
						
					}
					
					//if (skill.getName() == build.getSkillFromType(SkillType.valueOf(i)).getName()) {
					//	item = new ItemStack(Material.BOOK);
					//	item = UtilItem.addGlow(item);
					//	this.addButton(new Button(gui_index, item, skill.getName(), skill.getDescription()));
					//}
					
					
				}
			}
		}
		
		
		
		


		this.populate();
		
		
		
		
		/*
		
		tmpSkillList = skillManager.getSkillsForKitAndType(kit, SkillType.AXE);
		ItemStack item = new ItemStack(Material.BOOK);
		Skill skill = tmpSkillList.get(0);
		
		this.addButton(new Button(10, item, skill.getName(), skill.getDescription()));
		this.addButton(new Button(11, new ItemStack(Material.BOOK), "TEST"));
		
		
		this.populate();
		*/
		
		//Populate all
		/*
		for (int i = 0; i <= 5; i++) {
			tmpSkillList = skillManager.getSkillsForKitAndType(kit, SkillType.valueOf(i));	
			
			
			int maxValue = Math.min(tmpSkillList.size(), 6);
			
			
			//Iterate from left to right of skills per skillType. Avoids exceeding the rightmost value
			for (int j = 1; j <= maxValue; j++) {
				
				
				int index = (i * 9) + 1;
				ItemStack item = new ItemStack(Material.GREEN_DYE);
				Skill skill = tmpSkillList.get(j-1);
				
				
				this.addButton(new Button(index, item, skill.getName(), skill.getDescription()));
				
				
			}
		}
			
			*/
		
	
	
	
	};
	public Kit getKit() {
		return this.kit;
	}
	
	public int getIndex() {
		return this.build_index;
	}
	
	public Build getBuild() {
		return this.b;
	}
		//Populate with loaded skills

		
		
	
}