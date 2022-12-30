package me.barret.build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;

import me.barret.gui.Gui;
import me.barret.kits.Kit;
import me.barret.skill.Skill;
import me.barret.skill.Skill.SkillType;

public class Build {
	
	
	private Kit kit;
	
	private List<BuildSkill> BuildSkills = new ArrayList<>();
	
	int tokens = 12;
	
	
	

	
	
	//private Gui item_gui; for champions minigames
	
	
	
	
	
	public Build(Kit k, Skill sword, Skill axe, Skill bow, Skill a, Skill b, Skill c, int tokens) {
		this.kit = k;
		BuildSkills.add(new BuildSkill(sword, 1));
		BuildSkills.add(new BuildSkill(axe, 1));
		BuildSkills.add(new BuildSkill(bow, 1));
		BuildSkills.add(new BuildSkill(a, 1));
		BuildSkills.add(new BuildSkill(b, 1));
		BuildSkills.add(new BuildSkill(c, 1));
		this.tokens = tokens;
		
		
		//INITIALIZE GUi
		
		
		
	}
	

	public Kit getKit() {
		return kit;
	}
	
	
	public Skill getSkillFromType(SkillType type) {
		for (BuildSkill bs : BuildSkills) {
			if (bs.getSkill() == null) continue;
			if (bs.getSkill().getType().equals(type)) return bs.getSkill();
		}
		return null;
	}
	public BuildSkill getBuildSkillFromType(SkillType type) {
		for (BuildSkill bs : BuildSkills) {
			if (bs.getSkill() == null) return null;
			if (bs.getSkill().getType().equals(type)) return bs;
		}
		return null;
	}
	
	
	public Skill getSword()
	{
		return this.getSkillFromType(SkillType.SWORD);
	}
	public Skill getAxe()
	{
		return this.getSkillFromType(SkillType.AXE);
	}	
	public Skill getBow()
	{
		return this.getSkillFromType(SkillType.BOW);
	}
	public Skill getPassiveA()
	{
		return this.getSkillFromType(SkillType.PASSIVE_A);
	}	
	public Skill getPassiveB()
	{
		return this.getSkillFromType(SkillType.PASSIVE_B);
	}	
		
	public Skill getPassiveC()
	{
		return this.getSkillFromType(SkillType.PASSIVE_GLOBAL);
	}	
	
	
	
	
	
	
	public ArrayList<String> asList() {
		
		String _sword = "None";
		String _axe = "None";
		String _bow = "None";
		String _a = "None";
		String _b = "None";
		String _c = "None";
		
		//for (BuildSkill bs : BuildSkills) {
		//	if (bs.getSkill() == null);
		//	else System.out.println(bs.getSkill().getName());
		//}
		
		
		if (this.getSkillFromType(SkillType.SWORD) != null) {
			BuildSkill tmp = this.getBuildSkillFromSkill(this.getSkillFromType(SkillType.SWORD));
			
			_sword = tmp.getSkill().getName() + " " + tmp.getLevel();
		}
		if (this.getSkillFromType(SkillType.AXE) != null) {
			BuildSkill tmp = this.getBuildSkillFromSkill(this.getSkillFromType(SkillType.AXE));
			
			_axe = tmp.getSkill().getName() + " " + tmp.getLevel();
		}
		if (this.getSkillFromType(SkillType.BOW) != null) {
			BuildSkill tmp = this.getBuildSkillFromSkill(this.getSkillFromType(SkillType.BOW));
			
			_bow = tmp.getSkill().getName() + " " + tmp.getLevel();
		}
		if (this.getSkillFromType(SkillType.PASSIVE_A) != null) {
			BuildSkill tmp = this.getBuildSkillFromSkill(this.getSkillFromType(SkillType.PASSIVE_A));
			
			_a = tmp.getSkill().getName() + " " + tmp.getLevel();
		}
		if (this.getSkillFromType(SkillType.PASSIVE_B) != null) {
			BuildSkill tmp = this.getBuildSkillFromSkill(this.getSkillFromType(SkillType.PASSIVE_B));
			
			_b = tmp.getSkill().getName() + " " + tmp.getLevel();
		}
		if (this.getSkillFromType(SkillType.PASSIVE_GLOBAL) != null) {
			BuildSkill tmp = this.getBuildSkillFromSkill(this.getSkillFromType(SkillType.PASSIVE_GLOBAL));
			
			_c = tmp.getSkill().getName() + " " + tmp.getLevel();
		}
		

		String[] list =
		{
			ChatColor.GRAY + " ",	
						
			ChatColor.GRAY + "Sword Skill: " + ChatColor.WHITE + _sword,
			ChatColor.GRAY + "Axe Skill: " + ChatColor.WHITE + _axe,
			ChatColor.GRAY + "Bow Skill: " + ChatColor.WHITE + _bow,
			ChatColor.GRAY + "Passive A: " + ChatColor.WHITE + _a,
			ChatColor.GRAY + "Passive B: " + ChatColor.WHITE + _b,
			ChatColor.GRAY + "Global Passive C: " + ChatColor.WHITE + _c,					
			ChatColor.GRAY + " ",
		};
		
		return new ArrayList<String>(Arrays.asList(list));

	}
	
	public int getTokens() {
		return this.tokens;
	}
	

	
	
	
	
	
	
	//Skill Editor THings
	public boolean IncrementSkill(Skill s) {
		SkillType st = s.getType();
		BuildSkill bs = getBuildSkillFromSkill(s);

		//Does not have current skill
		if (bs == null) {
			

			
			Skill oldSkill = this.getSkillFromType(st);
			//Refund if cancelling other skill
			if (oldSkill != null) {
				
				System.out.println(oldSkill.getName());
				
				
				for (int i = 0; i < 6; i++) {
					try {
						this.DecrementSkill(oldSkill);
					}
					catch(Exception e) {
						System.out.println("Catch!");
					}

				}
				
				//System.out.println(this.getSkillFromType(st).getName());
				
				
				//this.tokens += this.getSkillFromType(st).getLevel();
				//BuildSkill tmp = this.getBuildSkillFromType(st);
				//tmp.setLevel(0);
				//tmp.setSkill(null);
				//this.BuildSkills.remove(tmp);
			}
	
			

			this.tokens--;
			BuildSkill b = new BuildSkill(s, 1);
			BuildSkills.add(b);
			
			//System.out.println("New Skill: " + b.getSkill().getName());
			//System.out.println("New Level: " + b.getLevel());

			
			
			//Delete old skill, set new skill
			//SkillType type = s.getType();
			return true;
		}
		
		
		else {

			if (bs.getLevel() == s.getMaxLevel()) return false; //Cap Level
			if (this.getTokens() <= 0) return false; //Build Tokens check
			
			//Increment Level
			bs.increment();
			//b.setLevel(3);
			this.tokens = this.tokens - 1;
			//System.out.println("New Level: " + bs.getLevel());
			
		//	BuildSkills.add(bs);
			
			
			
			
			
			
			return true;
			
			
			
		}


	}
	
	public boolean DecrementSkill(Skill s) {
		//SkillType st = s.getType();
		BuildSkill b = this.getBuildSkillFromSkill(s);
		if (b == null) return false;
		
		
		b.setLevel(b.getLevel() - 1);
		this.tokens = this.tokens + 1;
		
		if (b.getLevel() <= 0) {
			b.setLevel(0);
			b.setSkill(null);
			this.BuildSkills.remove(b);
			//this.nullifySkill(s);
		}
		return true;
	}
	
	
	
	
	
	/*
	public void removeSkill(Skill s) {
		if (this.hasSkill(s)) 
		{
			DecrementSkill(s);
			if (s.getLevel() == 0) {
				nullifySkill(s);
			}
		}
	}*/
	

	public BuildSkill getBuildSkillFromSkill(Skill s){
		if (s == null) return null;
		for (BuildSkill bs : BuildSkills) {
			if (bs.getSkill() == null) continue;
			if (bs.getSkill().getName() == s.getName()) return bs;
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	

}
