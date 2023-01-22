package me.barret.kits.editor;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.barret.build.Build;
import me.barret.gui.Button;
import me.barret.skill.Skill;

public class SkillButton extends Button {


	Build b;
	Skill s;
	
	
	
	public SkillButton(int slot, ItemStack item, String name, List<String> lore, Build b, Skill s) {
		super (slot,item, name, lore); 
		this.b = b;
		this.s = s;
	}
	
	
	public Build getBuild() {
		return b;
	}
	public Skill getSkill() {
		return s;
	}
	
	
	
}
