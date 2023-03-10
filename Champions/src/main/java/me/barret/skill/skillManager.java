package me.barret.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.barret.Champions;
import me.barret.kits.Kit;
import me.barret.skill.Assassin.Flash;
import me.barret.skill.Assassin.Omniscience;
import me.barret.skill.Assassin.PoisonArrow;
import me.barret.skill.Assassin.Quantize;
import me.barret.skill.Mage.DivineRay;
import me.barret.skill.Mechanist.GlueGlob;
import me.barret.skill.Mechanist.Orthogonal;
import me.barret.skill.Skill.SkillType;
import me.barret.skill.Mage.Blizzard;
import me.barret.skill.Mage.FireBlast;
import me.barret.skill.Mage.IcePrison;

public class skillManager {
	 private static HashMap<String, Skill> skills = new HashMap<>();
	    
		Champions i;
		
		public skillManager(Champions plugin)
		{
			this.i = plugin;
			
			

	    	//Brute
				//Sword
					//skills.put("Dwarf Toss", new DwarfToss(i));
			
			
			
	      //  skills.put("Test Skill", new testSkill(i));
	        
	        
	  
	        //Mage
	        	//Sword
	        		skills.put("Blizzard", new Blizzard(i));
					skills.put("Divine Ray", new DivineRay(i));
	        		
	        	//Axe
	        		skills.put("Ice Prison", new IcePrison(i));
	        		skills.put("Fire Blast", new FireBlast(i));
	        
	        //Knight
	        
	        	//Sword
	        		//skills.put("Hilt Smash", new HiltSmash(i));
	        
	        	//Axe
	        		//skills.put("Bulls Charge", new BullsCharge(i));
	        
	        
	        //Ranger
	        	//Sword
	        	//Axe
	        		//skills.put("Agility", new Agility(i));
	        		
	        		
	        		
	        
	        
	        //Assassin
	        	//Sword
					skills.put("Omniscience", new Omniscience(i));
	        		
	        		
	        	//Axe
	        		//skills.put("Leap", new Leap(i));
	        		//skills.put("Warp", new Warp(i));
					skills.put("Flash", new Flash(i));
					skills.put("Quantize", new Quantize(i));

				//Bow
					skills.put("Poison Arrow", new PoisonArrow(i));

			//Mechanist
				//Sword
					//




				//Axe
					skills.put("Orthogonal", new Orthogonal(i));
					skills.put("Glue Glob", new GlueGlob(i));

	        

	    }
		
		/**
		 * 
		 * @param tag
		 * @return Skill object of tag
		 */
		public static Skill getSkill(String tag)
		{
			return skills.get(tag);
		}
		
		
		public static List<Skill> getAllSkills(){
			List<Skill> rtn = new ArrayList<>();
			for (String s : skills.keySet()) {
				rtn.add(skills.get(s));
			}
			
			return rtn;
		}
		
		
		
		
		/**
		 * 
		 * @param kit
		 * @param skilltype
		 * @return
		 */
		public static List<Skill> getSkillsForKitAndType(Kit kit, SkillType skilltype){
			List<Skill> rtn = new ArrayList<>();
			for (String key : skills.keySet()) {
				Skill s = skills.get(key);
				if ((s.getKit() == kit) & (s.getType() == skilltype)) rtn.add(s);
			}
			
			return rtn;
		}
		
		
		
		
		
}
